import Response.model.*;
import Service.Calculations;
import model.*;
import Response.Services.ServerResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;

@Slf4j
public class Driver implements Runnable{
    DriverData driverData = new DriverData();
    int id;
    ServerResponse serverResponse = new ServerResponse();
    Calculations calculations = new Calculations();
    Point currentPoint, nextPoint, intermediatePoint;
    Route.BusStation busStation;
    int passedBusStation = 0;
    public Driver(SettingRoute settingRoute, Account account, boolean direction, int id){
        driverData.setAccount(account);
        driverData.setDirectionRoute(direction);
        this.id = id;
        driverData.setSpeed(settingRoute.getSpeed());
        driverData.setMovementInterval(settingRoute.getMovementInterval());
        driverData.setUpdateFrequency(settingRoute.getUpdateFrequency());
        driverData.setRoute_name(settingRoute.getRoute());
    }
    @Override
    public void run() {
        //Как это все работает:
        //сначала производится задержка потока в зависимости от того когда водитель поедет от начала.
        //потом происходить авторизация водителя на сервере через определенный порядок запросов
        //загружается в класс водителя транспорт на котором он будет ездить, у него есть параметры:
        // начальные координаты (долгота и широта)
        // конечные координаты (долгота и широта)
        // название маршрута
        // интервал движения между маршрутами
        // время обновления (частота отправки данных на сервер)
        // скорость транспорта
        // После задается начальная координата транспорта
        // И начинается обход маршрута по точкам с обозначением промежуточных точек

        try {
            Thread.sleep(id * driverData.getMovementInterval() * 1000L);
            driverData.setSpeed(driverData.getSpeed());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        driverData.setToken(((AuthResponse) serverResponse.authorization(driverData.getAccount().getPhone(), driverData.getAccount().getPin_code())).getToken());
        driverData.setCar_id(Integer.toString(((Car) serverResponse.ListOfDriversCars(driverData.getToken())).getId()));
        CarRoutes carRoutes = (CarRoutes) serverResponse.ListOfRoutesForTheSelectedCar(driverData.getToken(), driverData.getCar_id(), driverData.getRoute_name());
        driverData.setRouteId(Integer.toString(carRoutes.getId()));
        driverData.setTerminusId(Integer.toString(carRoutes.getTerminus().get(driverData.isDirectionRoute()? 1 : 0).getId()));
        Schedule schedule = (Schedule) serverResponse.ListOfSessionTimesToGetStarted(driverData.getToken(), driverData.getCar_id(), driverData.getRouteId(), driverData.getTerminusId());
        driverData.setStartTime(schedule.getTimes().get(0).getTime());
        Session session = (Session) serverResponse.InformationAboutTheCurrentSession(driverData.getToken());
        if(session.isState()){
            serverResponse.StopSession(driverData.getToken(), "");
            log.debug("Остановка начатой сессии");
        }
        serverResponse.ContiniumSessionTypeA(driverData.getToken(), driverData.getCar_id(), driverData.getRouteId(), driverData.getTerminusId());
        log.info("Первичные запросы успешны");
        session = (Session) serverResponse.InformationAboutTheCurrentSession(driverData.getToken());
        if (session.getStatus().equals("ON_ROUTE")){
            log.debug("Уже на маршруте");
        }
        if (session.getStatus().equals("ON_QUEUE")){
            //seconds_left - это время до начала поездки у маршрута, нужно просто ее взять и ожидать это время
            try {
                Thread.sleep(session.getSeconds_left()*1000L);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }
        if (session.getStatus().equals("WAIT_TO_START")){
            //Ждем время для начала отправки машины
            try {
                Thread.sleep(session.getSeconds_left()*1000L);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }

        driverData.setRoute((Route) serverResponse.InformationAboutTheRouteOfTheCurrentSession(driverData.getToken()));

        //создание транспорта для водителя с указанным параметрами и добавление функций
        /*driverData.setTransport_route(new Transport(
                new Point(driverData.getRoute().getPoints_by_dir().getForward().get(0).getLon(),
                        driverData.getRoute().getPoints_by_dir().getForward().get(0).getLat()),
                new Point(driverData.getRoute().getPoints_by_dir().getBackward().get(0).getLat(),
                        driverData.getRoute().getPoints_by_dir().getBackward().get(0).getLon()),
                driverData.getSettingRoute().getRoute(),
                driverData.getSettingRoute().getRoute_id(),
                driverData.getSettingRoute().getUpdateFrequency(),
                driverData.getSettingRoute().getSpeed()));*/

        Iterator<Point> routeIterator;//итератор маршрута
        Iterator<Route.BusStation> busStationIterator;
        while(true) {
            log.info("Начинаю поездку");
                session = (Session) serverResponse.InformationAboutTheCurrentSession(driverData.getToken());
                driverData.setCurrentSessionId(Integer.toString(session.getId()));
                driverData.setCurrentSessionStatus(session.getStatus());

            if (driverData.isDirectionRoute()) {//Определение направления маршрута
                //driverData.getTransport_route().setCurrentPoint(driverData.getTransport_route().getStartPoint());
                routeIterator = driverData.getRoute().getPath_by_dir().getForward().iterator();
                busStationIterator = driverData.getRoute().getPoints_by_dir().getForward().iterator();
            } else {
                //driverData.getTransport_route().setCurrentPoint(driverData.getTransport_route().getFinishPoint());
                routeIterator = driverData.getRoute().getPath_by_dir().getBackward().iterator();
                busStationIterator = driverData.getRoute().getPoints_by_dir().getBackward().iterator();
            }
            while (session.getStatus().equals("ON_QUEUE")){
                try {
                    log.debug("жду изменения статуса");
                    Thread.sleep(100000);
                    session = (Session) serverResponse.InformationAboutTheCurrentSession(driverData.getToken());
                    driverData.setCurrentSessionStatus(session.getStatus());
                    if(session.getStatus().equals("ON_QUEUE")){
                        serverResponse.StopSession(driverData.getToken(), "Перезапуск водителя");
                        log.debug("Перезапуск сессии");
                        serverResponse.ContiniumSessionTypeA(driverData.getToken(), driverData.getCar_id(), driverData.getRouteId(), driverData.getTerminusId());
                        session = (Session) serverResponse.InformationAboutTheCurrentSession(driverData.getToken());
                        driverData.setCurrentSessionStatus(session.getStatus());
                    }

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            //текущая точка, следующая точка, промежуточная точка
            busStation = busStationIterator.next();
            currentPoint = busStation.getPoint();
            nextPoint = null;
            intermediatePoint = null;
            log.info("Начинаю поездку с конечной точки");
            log.info("Текущая точка: " + busStation.getName() + " " + currentPoint.getLon() + " " + currentPoint.getLat());
            if (serverResponse.SendingLocation(driverData.getToken(), busStation.getPoint())){
                passedBusStation++;
                log.info("Текущая геопозиция: " + busStation.getLon() + " " + busStation.getLat());
                currentPoint = routeIterator.next();
            }else {
                log.error("Ошибка отправки местоположения");
            }
            busStation = busStationIterator.next();
            long startTimeTimer = System.currentTimeMillis();
            long elapsedTime = 0; //прошедшее время
            long timeAdd = 0;

            //Цикл хождения по маршруту
            while (routeIterator.hasNext()) {
                if (System.currentTimeMillis() - startTimeTimer > 20000L){
                    session = (Session) serverResponse.InformationAboutTheCurrentSession(driverData.getToken());
                    //serverResponse.InformationAboutDistanceAndOtherStatistic(driverData.getToken());
                    startTimeTimer = System.currentTimeMillis();
                }
                if(busStation.getEvent().getCode().equals("bus_stop") || busStation.getEvent().getCode().equals("bus_terminus")){
                    if (calculations.DistanceBetweenPoints(currentPoint, busStation.getPoint()) < 60.0 && busStationIterator.hasNext()) {
                        try {
                            if (serverResponse.SendingLocation(driverData.getToken(), currentPoint)){
                                long sleepTimeToBusSt = Math.round(calculations.timeDistance(currentPoint.getLat(),
                                        currentPoint.getLon(),busStation.getLat(),busStation.getLon(),
                                        driverData.getSpeed()));
                                Thread.sleep(sleepTimeToBusSt);
                            } else {
                                log.error("Ошибка отправки местоположения");
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            if (serverResponse.SendingLocation(driverData.getToken(), busStation.getPoint())){
                                passedBusStation++;
                                Thread.sleep(15L * 1000L);
                                log.info("Текущая остановка: " + busStation.getName() + " " + busStation.getLon() + " " + busStation.getLat());
                                log.info("Стою 15 секунд");
                            } else {
                                log.error("Ошибка отправки местоположения");
                            }
                            elapsedTime = 0;
                            currentPoint = busStation.getPoint();
                            busStation = busStationIterator.next();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    passedBusStation++;
                    busStation = busStationIterator.next();
                }
                if (driverData.getCurrentSessionStatus().equals("ON_ROUTE")) {
                    if (intermediatePoint == null) {
                        nextPoint = routeIterator.next();
                    }
                    //Расчет времени до следующей точки
                    long timeToNextPoint = Math.round(calculations.timeDistance(
                            currentPoint.getLat(),
                            currentPoint.getLon(),
                            nextPoint.getLat(),
                            nextPoint.getLon(),
                            driverData.getSpeed()));

                    if (elapsedTime + timeToNextPoint > driverData.getUpdateFrequency() * 1000L) {
                        //Сколько времени транспорт будет ехать до 15 секунд
                        timeAdd = driverData.getUpdateFrequency() * 1000L - elapsedTime;
                        //Расчет координаты точки через некоторое время
                        intermediatePoint = calculations.givePointFromDistance(
                                currentPoint.getLat(),
                                currentPoint.getLon(),
                                (timeAdd / 1000.0) * (driverData.getSpeed() / 3.6),
                                nextPoint.getLat(),
                                nextPoint.getLon());
                        currentPoint = intermediatePoint;
                        elapsedTime = 0;
                    } else if (elapsedTime + timeToNextPoint < driverData.getUpdateFrequency() * 1000L) {
                        currentPoint = nextPoint;
                        elapsedTime += timeToNextPoint;
                        intermediatePoint = null;
                    } else {
                        currentPoint = nextPoint;
                        elapsedTime = 0;
                        intermediatePoint = null;
                    }
                    try {
                        if(elapsedTime != 0){
                            Thread.sleep(timeToNextPoint);//ожидание
                        }else {
                            Thread.sleep(timeAdd);//ожидание
                        }
                        if (elapsedTime == 0) {
                                if (serverResponse.SendingLocation(driverData.getToken(), currentPoint)){
                                    log.info("Текущая геопозиция: " + currentPoint.getLon() + " " + currentPoint.getLat());
                                }else {
                                    log.error("Ошибка отправки местоположения");
                                }
                        }
                    } catch (InterruptedException e) {
                        log.error(e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
                else if(session.getStatus().equals("ON_QUEUE")){
                    try {
                        Thread.sleep(session.getSeconds_left());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if(session.getStatus().equals("WAIT_TO_START")){
                    try {
                        Thread.sleep(session.getSeconds_left());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else if(!session.isState()){
                    serverResponse.ContiniumSessionTypeA(driverData.getToken(), driverData.getCar_id(), driverData.getRouteId(), driverData.getTerminusId());
                } else {
                    log.error("Некоретный статус");
                }
            }
            if(busStationIterator.hasNext()){
                while (busStationIterator.hasNext()){
                    busStation = busStationIterator.next();
                    if(busStation.getEvent().getCode().equals("bus_terminus")){
                        if (serverResponse.SendingLocation(driverData.getToken(), busStation.getPoint())){
                            passedBusStation++;
                            log.info("Текущая геопозиция: " + busStation.getLon() + " " + busStation.getLat());
                        }else {
                            log.error("Ошибка отправки местоположения");
                        }
                    }
                }
            } else {
                if(busStation.getEvent().getCode().equals("bus_terminus")){
                    if (serverResponse.SendingLocation(driverData.getToken(), busStation.getPoint())){
                        passedBusStation++;
                        log.info("Текущая геопозиция: " + busStation.getLon() + " " + busStation.getLat());
                    }else {
                        log.error("Ошибка отправки местоположения");
                    }
                }
            }

            //вот это смена маршрута, смена ид сессии и проверка времени чтобы отправится обратно
            driverData.setDirectionRoute(!driverData.isDirectionRoute());
            driverData.setTerminusId(Integer.toString(carRoutes.getTerminus().get(driverData.isDirectionRoute()? 1 : 0).getId()));
            if (!driverData.isDirectionRoute()){
                if(passedBusStation == driverData.getRoute().getPoints_by_dir().getForward().size()){
                    log.info("Направление FORWARD, Все остановки пройдены успешено: "+ passedBusStation + "/"+driverData.getRoute().getPoints_by_dir().getForward().size());
                    passedBusStation=0;
                }else {
                    log.info("Направление FORWARD, Остановки пройдены неуспешено: "+ passedBusStation + "/"+driverData.getRoute().getPoints_by_dir().getForward().size());
                    passedBusStation=0;
                }
            }else {
                if(passedBusStation == driverData.getRoute().getPoints_by_dir().getBackward().size()){
                    log.info("Направление BACKWARD, Все остановки пройдены успешено: "+ passedBusStation + "/" + driverData.getRoute().getPoints_by_dir().getBackward().size());
                    passedBusStation=0;
                }else {
                    log.info("Направление BACKWARD, Остановки пройдены неуспешено: "+ passedBusStation + "/" + driverData.getRoute().getPoints_by_dir().getBackward().size());
                    passedBusStation=0;
                }
            }
            session = (Session) serverResponse.InformationAboutTheCurrentSession(driverData.getToken());
            driverData.setCurrentSessionId(Integer.toString(session.getId()));
            driverData.setCurrentSessionStatus(session.getStatus());

            if (session.getStatus().equals("ON_ROUTE")){
                log.info("Смена направления");
            }
            if (session.getStatus().equals("ON_QUEUE")){
                //seconds_left - это время до начала поездки у маршрута, нужно просто ее взять и ожидать это время
                try {
                    Thread.sleep(session.getSeconds_left()*1000L);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
            }
            if (session.getStatus().equals("WAIT_TO_START")){
                //Ждем время для начала отправки машины
                try {
                    Thread.sleep(session.getSeconds_left()*1000L);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
            }
            if(!session.isState()){
                serverResponse.ContiniumSessionTypeA(driverData.getToken(), driverData.getCar_id(), driverData.getRouteId(), driverData.getTerminusId());
            }
        }
    }
}
