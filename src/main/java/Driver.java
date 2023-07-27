import Requests.model.RequestsData;
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
    public Driver(SettingRoute settingRoute, Account account, boolean direction, int id){
        driverData.setAccount(account);
        driverData.setDirectionRoute(direction);
        driverData.setSettingRoute(settingRoute);
        this.id = id;
    }

    ServerResponse serverResponse = new ServerResponse();
    RequestsData requestsData = new RequestsData();
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
            Thread.sleep(id * driverData.getSettingRoute().getMovementInterval() * 1000L);
            driverData.setSpeed(driverData.getSettingRoute().getSpeed());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        driverData.setToken(((AuthResponse) serverResponse.authorization(driverData.getAccount().getPhone(), driverData.getAccount().getPin_code())).getToken());
        driverData.setCar_id(Integer.toString(((Car) serverResponse.ListOfDriversCars(driverData.getToken())).getId()));
        CarRoutes carRoutes = (CarRoutes) serverResponse.ListOfRoutesForTheSelectedCar(driverData.getToken(), driverData.getCar_id(), driverData.getSettingRoute().getRoute());
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
        log.info("Начинаю поездку");
        driverData.setRoute(RouteToRouteBase((Route) serverResponse.InformationAboutTheRouteOfTheCurrentSession(driverData.getToken())));

        //создание транспорта для водителя с указанным параметрами и добавление функций
        driverData.setTransport_route(new Transport(
                new Point(driverData.getRoute().getRoute_forward().get(0).getLon(),
                        driverData.getRoute().getRoute_forward().get(0).getLat()),
                new Point(driverData.getRoute().getRoute_forward().get(driverData.getRoute().getRoute_forward().size() - 1).getLat(),
                        driverData.getRoute().getRoute_forward().get(driverData.getRoute().getRoute_forward().size() - 1).getLon()),
                driverData.getRoute().getName(),
                driverData.getRoute().getNumber(),
                driverData.getSettingRoute().getUpdateFrequency(),
                driverData.getSettingRoute().getSpeed()));

        Iterator<Point> routeIterator;//итератор маршрута
        while(true) {
                session = (Session) serverResponse.InformationAboutTheCurrentSession(driverData.getToken());
                driverData.setCurrentSessionId(Integer.toString(session.getId()));
                driverData.setCurrentSessionStatus(session.getStatus());

            if (driverData.isDirectionRoute()) {//Определение направления маршрута
                driverData.getTransport_route().setCurrentPoint(driverData.getTransport_route().getStartPoint());
                routeIterator = driverData.getRoute().getRoute_forward().iterator();
            } else {
                driverData.getTransport_route().setCurrentPoint(driverData.getTransport_route().getFinishPoint());
                routeIterator = driverData.getRoute().getRoute_backward().iterator();
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
            Point currentPoint = routeIterator.next(), nextPoint = null, intermediatePoint = null;
            log.info("Начинаю поездку с конечной точки");
            log.info("Текущая точка: " + currentPoint.getName() + " " + currentPoint.getLon() + " " + currentPoint.getLat());
            long startTimeTimer = System.currentTimeMillis();//начальное время
            long elapsedTime = 0; //прошедшее время
            long timeAdd = 0;

            //Цикл хождения по маршруту
            while (routeIterator.hasNext()) {
                if (driverData.getCurrentSessionStatus().equals("ON_ROUTE")) {
                    if (intermediatePoint == null) {
                        nextPoint = routeIterator.next();
                    }
                    //Расчет времени до следующей точки
                    long timeToNextPoint = Math.round(Calculations.timeDistance(
                            currentPoint.getLat(),
                            currentPoint.getLon(),
                            nextPoint.getLat(),
                            nextPoint.getLon(),
                            driverData.getTransport_route().getTransport_speed()));

                    if (elapsedTime + timeToNextPoint > driverData.getTransport_route().getUpdate_time() * 1000L) {
                        //Сколько времени транспорт будет ехать до 15 секунд
                        timeAdd = driverData.getTransport_route().getUpdate_time() * 1000L - elapsedTime;
                        //Расчет координаты точки через некоторое время
                        intermediatePoint = Calculations.givePointFromDistance(
                                currentPoint.getLat(),
                                currentPoint.getLon(),
                                (timeAdd / 1000) * (driverData.getTransport_route().getTransport_speed() / 3.6),
                                nextPoint.getLat(),
                                nextPoint.getLon());
                        driverData.getTransport_route().setCurrentPoint(intermediatePoint);
                        currentPoint = intermediatePoint;
                        elapsedTime = 0;
                    } else if (elapsedTime + timeToNextPoint < driverData.getTransport_route().getUpdate_time() * 1000L) {
                        driverData.getTransport_route().setCurrentPoint(nextPoint);
                        currentPoint = nextPoint;
                        elapsedTime += timeToNextPoint;
                        intermediatePoint = null;
                    } else {
                        driverData.getTransport_route().setCurrentPoint(nextPoint);
                        currentPoint = nextPoint;
                        elapsedTime = 0;
                        intermediatePoint = null;
                    }

                    try {
                        if(elapsedTime != 0){
                            Thread.sleep(elapsedTime);//ожидание
                        }else {
                            Thread.sleep(timeAdd);//ожидание
                        }
                        if (elapsedTime == 0) {
                            session = (Session) serverResponse.InformationAboutTheCurrentSession(driverData.getToken());
                            serverResponse.InformationAboutDistanceAndOtherStatistic(driverData.getToken());
                            if(session.getStatus().equals("ON_QUEUE")){
                                try {
                                    Thread.sleep(session.getSeconds_left());
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            if(session.getStatus().equals("WAIT_TO_START")){
                                try {
                                    Thread.sleep(session.getSeconds_left());
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            if(session.getStatus().equals("ON_ROUTE")){
                                if (serverResponse.SendingLocation(driverData.getToken(), driverData.getTransport_route().getCurrentPoint())){
                                    log.info("Текущая геопозиция: " + driverData.getTransport_route().getCurrentPoint().getLon() + " " + driverData.getTransport_route().getCurrentPoint().getLat());
                                }else {
                                    log.error("Ошибка отправки местоположения");
                                }
                                startTimeTimer = System.currentTimeMillis();
                            }

                        }
                    } catch (InterruptedException e) {
                        log.error(e.getMessage());
                        throw new RuntimeException(e);
                    }
                    if (currentPoint.hasName()) {
                        log.info("Текущая остановка: " + currentPoint.getName() + " " + currentPoint.getLon() + " " + currentPoint.getLat());
                        log.info("Стою 15 секунд");
                        try {
                            Thread.sleep(15L * 1000L);
                            if (serverResponse.SendingLocation(driverData.getToken(), driverData.getTransport_route().getCurrentPoint())){
                            } else {
                                log.error("Ошибка отправки местоположения");
                            }
                            elapsedTime = 0;
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }
                else{
                    log.info("Некоректный статус");
                    break;
                }
            }
            //вот это смена маршрута, смена ид сессии и проверка времени чтобы отправится обратно
            driverData.setDirectionRoute(!driverData.isDirectionRoute());
            driverData.setTerminusId(Integer.toString(carRoutes.getTerminus().get(driverData.isDirectionRoute()? 1 : 0).getId()));
            String dateString = "";
                session = (Session) serverResponse.InformationAboutTheCurrentSession(driverData.getToken());
                driverData.setCurrentSessionId(Integer.toString(session.getId()));
                driverData.setCurrentSessionStatus(session.getStatus());

            if (session.getStatus().equals("ON_ROUTE")){
                log.debug("Уже на обратном маршруте");
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

    private RouteBase RouteToRouteBase(Route route){
        RouteBase routeBase = new RouteBase();
        routeBase.setRoute_forward(route.getPath_by_dir().getForward());
        routeBase.setRoute_backward(route.getPath_by_dir().getBackward());
        Iterator<Point> iterForward = routeBase.getRoute_forward().iterator();
        Iterator<Route.BusStation> iterBusForward = route.getPoints_by_dir().getForward().iterator();
        if (iterForward.hasNext() && iterBusForward.hasNext()){
            Point point = iterForward.next();
            Route.BusStation busStation = iterBusForward.next();
            while (iterForward.hasNext() && iterBusForward.hasNext()){
                if(point.getLat() == busStation.getLat() && point.getLon() == busStation.getLon()){
                    point.setName(busStation.getName());
                    point = iterForward.next();
                    busStation = iterBusForward.next();
                } else {
                    point = iterForward.next();
                }

            }
        }

        Iterator<Point> iterBackward = routeBase.getRoute_backward().iterator();
        Iterator<Route.BusStation> iterBusBackward = route.getPoints_by_dir().getBackward().iterator();
        if (iterBackward.hasNext() && iterBusBackward.hasNext()){
            Point point = iterBackward.next();
            Route.BusStation busStation = iterBusBackward.next();
            while (iterBackward.hasNext() && iterBusBackward.hasNext()){
                if(point.getLat() == busStation.getLat() && point.getLon() == busStation.getLon()){
                    point.setName(busStation.getName());
                    point = iterBackward.next();
                    busStation = iterBusBackward.next();
                } else {
                    point = iterBackward.next();
                }
            }
        }
        log.debug("Парсинг маршрута с сервера успешен?");
        return routeBase;
    }
}
