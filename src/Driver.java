import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
public class Driver implements Runnable{
    RouteBase route;
    Transport transport_route;
    int movementInterval;
    int UpdateFrequency;
    double speed;

    String phone_number;
    String pin_code;
    boolean directionRoute = true;
    HttpRequest request;
    JsonObject response;
    String authorizationToken;
    String carId, routeId, terminusId, startTime;// Id машины, маршрута, терминала, начальное время
    int sessionId = 0 , currentSessionId = 0;
    public Driver(RouteBase route, int movementInterval, int UpdateFrequency, double speed, String phone_number, String pin_code, HttpRequest request, boolean directionRoute){
        this.route = route;
        this.movementInterval = movementInterval;
        this.UpdateFrequency = UpdateFrequency;
        this.speed = speed;
        this.phone_number = phone_number;
        this.pin_code = pin_code;
        this.request = request;
        this.directionRoute = directionRoute;

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
        // И начинается обход маршрута по точкам с обозначение промежуточных точек
        try {
            Thread.currentThread().sleep(movementInterval * 1000);//начальная задержка между отправками водителей
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            authorizationDriver();//авторизация
            ListDriversCars();//получение списка машин доступных для водителя
            ListRoutes();//получения списка маршрутов
            ListOfSessions();//получение сессии
            StartSessionA();//старт сессии //тут надо пытаться ловить время
            sessionId = informationAboutSession();//получение Id сессии
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //создание транспорта для водителя с указанным параметрами и добавление функций
        transport_route = new Transport(
                route.getRoute_forward().get(0).getP_longitude(),
                route.getRoute_forward().get(0).getP_latitude(),
                route.getRoute_forward().get(route.getRoute_forward().size() - 1).getP_latitude(),
                route.getRoute_forward().get(route.getRoute_forward().size() - 1).getP_longitude(),
                route.name,
                route.getNumber(),
                UpdateFrequency,
                speed) {
            @Override
            public void startForward() {//установка начальной позиции
                setLongitude(getStart_longitude());
                setLatitude(getStart_latitude());
            }

            @Override
            public void startBackward() {//установка начальной позиции
                setLongitude(getFinish_longitude());
                setLatitude(getFinish_latitude());
            }
        };

        Iterator<Point> routeIterator;//итератор маршрута
        while(true) {
            try {
                currentSessionId = informationAboutSession();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (directionRoute) {//Определение направления маршрута
                transport_route.startForward();
                routeIterator = route.getRoute_forward().iterator();
            } else {
                transport_route.startBackward();
                routeIterator = route.getRoute_backward().iterator();
            }
            //текущая точка, следующая точка, промежуточная точка
            Point currentPoint = routeIterator.next(), nextPoint = null, intermediatePoint = null;
            System.out.println(currentPoint.getName() + " " + currentPoint.getP_longitude() + " " + currentPoint.getP_latitude());
            long startTimeTimer = System.currentTimeMillis();//начальное время
            long elapsedTime = 0; //прошедшее время
            long timeAdd = 0;
            //Цикл хождения по маршруту
            //
            while (routeIterator.hasNext()) {
                if (sessionId == currentSessionId) {
                    if (intermediatePoint == null) {
                        nextPoint = routeIterator.next();
                    }
                    //Расчет времени до следующей точки
                    long timeToNextPoint = Math.round(Calculations.timeDistance(
                            currentPoint.getP_latitude().doubleValue(),
                            currentPoint.getP_longitude().doubleValue(),
                            nextPoint.getP_latitude().doubleValue(),
                            nextPoint.getP_longitude().doubleValue(),
                            transport_route.getTransport_speed()));

                    if (elapsedTime + timeToNextPoint > transport_route.getUpdate_time() * 1000L) {
                        //Сколько времени транспорт будет ехать до 15 секунд
                        timeAdd = transport_route.getUpdate_time() * 1000L - elapsedTime;
                        //Расчет координаты точки через некоторое время
                        intermediatePoint = Calculations.givePointFromDistance(
                                currentPoint.getP_latitude().doubleValue(),
                                currentPoint.getP_longitude().doubleValue(),
                                (timeToNextPoint / 1000) * (speed / 3.6),
                                nextPoint.getP_latitude().doubleValue(),
                                nextPoint.getP_longitude().doubleValue());
                        transport_route.setCurrentCoordinates(intermediatePoint);
                        currentPoint = intermediatePoint;
                        elapsedTime = 0;
                    } else if (elapsedTime + timeToNextPoint < transport_route.getUpdate_time() * 1000L) {
                        transport_route.setCurrentCoordinates(nextPoint);
                        currentPoint = nextPoint;
                        elapsedTime += timeToNextPoint;
                        intermediatePoint = null;

                    } else {
                        transport_route.setCurrentCoordinates(nextPoint);
                        currentPoint = nextPoint;
                        elapsedTime = 0;
                        intermediatePoint = null;
                    }

                    try {
                        if(elapsedTime != 0){
                            Thread.currentThread().sleep(elapsedTime);//ожидание
                        }else {
                            Thread.currentThread().sleep(timeAdd);//ожидание
                        }
                        if (elapsedTime == 0) {
                            sendLocation(transport_route.getCurrentCoordinates());
                            System.out.println(transport_route.getCurrentCoordinates().getP_longitude() + " " + transport_route.getCurrentCoordinates().getP_latitude());
                            startTimeTimer = System.currentTimeMillis();
                        }
                    } catch (InterruptedException | IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (currentPoint.hasName()) {
                        System.out.println(currentPoint.getName() + " " + currentPoint.getP_longitude() + " " + currentPoint.getP_latitude());
                        System.out.println("Стою 15 секунд");
                        try {
                            Thread.currentThread().sleep(15 * 1000);
                            sendLocation(transport_route.getCurrentCoordinates());
                            elapsedTime = 0;
                        } catch (InterruptedException | IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        //System.out.println("Pass the point" + " " + nextPoint.getP_longitude() + " " + nextPoint.getP_latitude() + "  Времени потрачено: " + timeToNextPoint / 1000);
                    }
                }
            }
            //вот это смена маршрута, смена ид сессии и проверка времени чтобы отправится обратно
            directionRoute = !directionRoute;
            String dateString = "2023.";
            try {
                response = request.InformationAboutTheCurrentSessionRequest(authorizationToken);
                sessionId = response.getAsJsonObject("result").get("id").getAsInt();
                currentSessionId = sessionId;
                dateString += response.getAsJsonObject("result").get("start_at").getAsString();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            DateFormat dateFormat = new SimpleDateFormat("YYYY.dd.MM HH:mm");
            Date date = null;
            try {
                date = dateFormat.parse(dateString);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            long serverTime = (long)date.getTime();
            while(serverTime > System.currentTimeMillis()){
                try {
                    Thread.currentThread().sleep(serverTime - System.currentTimeMillis());
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            try {
                StartSessionA();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private void authorizationDriver() throws IOException{
        //Authorization driver
        //дописать порядок авторизации и вынести в отдельную функцию
        response = request.AuthorizationRequest(phone_number, pin_code);
        authorizationToken = response.getAsJsonObject("result").getAsJsonPrimitive("token").getAsString();
        Scanner scanner = new Scanner(authorizationToken);
        scanner.useDelimiter("\\|");
        scanner.next();
        authorizationToken = scanner.next();
        System.out.println(authorizationToken);
        //request.StartSessionTypeARequest(authorizationToken, "123", "421", "321", "10");
    }
    private void ListDriversCars() throws IOException {
        //List of driver's cars
        response = request.ListOfDriversCarsRequest(authorizationToken);
        JsonArray jArray = response.getAsJsonArray("result");
        carId = jArray.get(0).getAsJsonObject().get("id").toString();
    }
    private void ListRoutes() throws IOException {
        response = request.ListOfRoutesForTheSelectedCarRequest(authorizationToken, carId);
        JsonArray jArray = response.getAsJsonArray("result");
        routeId = jArray.get(0).getAsJsonObject().get("id").toString();
        terminusId = jArray.get(0).getAsJsonObject().get("terminus").getAsJsonArray().get(0).getAsJsonObject().get("id").toString();
    }
    private void ListOfSessions() throws IOException {
        response = request.ListOfSessionTimesToGetStartedRequest(authorizationToken, carId, routeId, terminusId);
        JsonObject jo = response.getAsJsonObject("result");
        startTime = jo.getAsJsonObject().get("times").getAsJsonArray().get(0).getAsJsonObject().get("time").toString();
    }
    private void StartSessionA() throws IOException {
        request.StartSessionTypeARequest(authorizationToken, carId, routeId, terminusId, startTime);
    }

    private void sendLocation(Point currentPoint) throws IOException {
        request.SendingLocationRequest(authorizationToken, currentPoint.getP_latitude().toString(), currentPoint.getP_longitude().toString());
    }
    private int informationAboutSession() throws IOException {
        response = request.InformationAboutTheCurrentSessionRequest(authorizationToken);
        return response.getAsJsonObject("result").get("id").getAsInt();

    }
    private void stopSession() throws IOException{
        request.StopSessionRequest(authorizationToken, "Stopping");

    }
}
