import Requests.model.Account;
import Response.Services.ServerResponse;
import Service.Calculations;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

@Slf4j
public class DriverOld implements Runnable{
    RouteBase route;
    Transport transport_route;
    int movementInterval;
    int UpdateFrequency;
    double speed;
    Account account;
    boolean directionRoute;
    HttpRequestOld request;
    JsonObject response;
    String authorizationToken;
    String carId, routeId, terminusId, startTime;// Id машины, маршрута, терминала, начальное время
    String sessionId = "0";
    String currentSessionId = "0";
    String currentSessionStatus = "0";
    String[] data = new  String[3];
    String[] sessionData = new String[3];
    ServerResponse serverResponse = new ServerResponse();
    public DriverOld(RouteBase route, int movementInterval, int UpdateFrequency, double speed, String phone_number, String pin_code, HttpRequestOld request, boolean directionRoute){
        this.route = route;
        this.movementInterval = movementInterval;
        this.UpdateFrequency = UpdateFrequency;
        this.speed = speed;
        this.account.setLogin(phone_number);
        this.account.setPassword(pin_code);
        this.request = request;
        this.directionRoute = directionRoute;
    }
    public DriverOld(SettingRoute settingRoute, Account account, boolean directionRoute){
        this.movementInterval = settingRoute.getMovementInterval();
        this.UpdateFrequency = settingRoute.getUpdateFrequency();
        this.speed = settingRoute.getSpeed();
        this.directionRoute = directionRoute;
        this.account = account;
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
            authorizationDriver();//авторизация
            ListDriversCars();//получение списка машин доступных для водителя
            ListRoutes();//получения списка маршрутов
            data = ListOfSessions();//получение сессии и запись времени
            startTime = data[0];
            String dateString = data[1] + " " + data[0];
            data = informationAboutSession();//получение Id сессии
            sessionId = data[0];
            if(!sessionId.equals("0")){
                stopSession();
                log.debug("Остановка начатой сессии");
            }
            ContiniumSessionA();
            infAboutTheCurSessionRoute(authorizationToken, route);
            sessionData = informationAboutSession();
            if (sessionData[1].equals("ON_ROUTE")){
                log.debug("Уже на маршруте");
            }
            if (sessionData[1].equals("ON_QUEUE")){
                //seconds_left - это время до начала поездки у маршрута, нужно просто ее взять и ожидать это время
                try {
                    Thread.sleep(Integer.parseInt(sessionData[2])*1000L - 2000);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
            }
            if (sessionData[1].equals("WAIT_TO_START")){
                //Ждем время для начала отправки машины
                    try {
                        Thread.sleep(Integer.parseInt(sessionData[2])*1000L - 2000);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage());
                    }
            }

            //StartSessionA();//старт сессии //тут надо пытаться ловить время
            sessionId = informationAboutSession()[0];//получение Id сессии*/

           /* data = StartSessionA();//старт сессии
            currentSessionId = data[0];
            currentSessionStatus = data[1];*/

        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        //создание транспорта для водителя с указанным параметрами и добавление функций
        transport_route = new Transport(
                new Point(route.getRoute_forward().get(0).getLon(),
                        route.getRoute_forward().get(0).getLat(),""),
                new Point(route.getRoute_forward().get(route.getRoute_forward().size() - 1).getLat(),
                        route.getRoute_forward().get(route.getRoute_forward().size() - 1).getLon(), ""),
                route.getName(),
                route.getNumber(),
                UpdateFrequency,
                speed);

        Iterator<Point> routeIterator;//итератор маршрута
        String[] curSession;
        while(true) {
            try {
                curSession = informationAboutSession();
                currentSessionId = curSession[0];
                currentSessionStatus = curSession[1];
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (directionRoute) {//Определение направления маршрута
                transport_route.setCurrentPoint(transport_route.getStartPoint());
                routeIterator = route.getRoute_forward().iterator();
            } else {
                transport_route.setCurrentPoint(transport_route.getFinishPoint());
                routeIterator = route.getRoute_backward().iterator();
            }
            //текущая точка, следующая точка, промежуточная точка
            Point currentPoint = routeIterator.next(), nextPoint = null, intermediatePoint = null;
            log.info("Начинаю поездку с конечной точки");
            log.info("Текущая точка: " + currentPoint.getName() + " " + currentPoint.getLon() + " " + currentPoint.getLat());
            long startTimeTimer = System.currentTimeMillis();//начальное время
            long elapsedTime = 0; //прошедшее время
            long timeAdd = 0;
            try {
                currentSessionStatus = informationAboutSession()[1];
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //Цикл хождения по маршруту
            while (routeIterator.hasNext()) {
                if (currentSessionStatus.equals("ON_ROUTE")) {
                    if (intermediatePoint == null) {
                        nextPoint = routeIterator.next();
                    }
                    //Расчет времени до следующей точки
                    long timeToNextPoint = Math.round(Calculations.timeDistance(
                            currentPoint.getLat().doubleValue(),
                            currentPoint.getLon().doubleValue(),
                            nextPoint.getLat().doubleValue(),
                            nextPoint.getLon().doubleValue(),
                            transport_route.getTransport_speed()));

                    if (elapsedTime + timeToNextPoint > transport_route.getUpdate_time() * 1000L) {
                        //Сколько времени транспорт будет ехать до 15 секунд
                        timeAdd = transport_route.getUpdate_time() * 1000L - elapsedTime;
                        //Расчет координаты точки через некоторое время
                        intermediatePoint = Calculations.givePointFromDistance(
                                currentPoint.getLat().doubleValue(),
                                currentPoint.getLon().doubleValue(),
                                (timeToNextPoint / 1000) * (speed / 3.6),
                                nextPoint.getLat().doubleValue(),
                                nextPoint.getLon().doubleValue());
                        transport_route.setCurrentPoint(intermediatePoint);
                        currentPoint = intermediatePoint;
                        elapsedTime = 0;
                    } else if (elapsedTime + timeToNextPoint < transport_route.getUpdate_time() * 1000L) {
                        transport_route.setCurrentPoint(nextPoint);
                        currentPoint = nextPoint;
                        elapsedTime += timeToNextPoint;
                        intermediatePoint = null;
                    } else {
                        transport_route.setCurrentPoint(nextPoint);
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
                            sendLocation(transport_route.getCurrentPoint());
                            log.info("Текущая геопозиция: " + transport_route.getCurrentPoint().getLon() + " " + transport_route.getCurrentPoint().getLat());
                            startTimeTimer = System.currentTimeMillis();
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
                            sendLocation(transport_route.getCurrentPoint());
                            elapsedTime = 0;
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    try {
                        sessionData = informationAboutSession();
                        if(sessionData[1].equals("ON_QUEUE")){

                        }
                        if(sessionData[1].equals("WAIT_TO_START")){

                        }
                        if(sessionData[1].equals("ON_ROUTE")){

                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
                else{
                    log.info("Некоректный статус");
                    break;
                }
            }
            //вот это смена маршрута, смена ид сессии и проверка времени чтобы отправится обратно
            directionRoute = !directionRoute;
            String dateString = "";
            try {
                sessionData = informationAboutSession();
                sessionId = sessionData[0];
                currentSessionId = sessionId;
                currentSessionStatus = sessionData[1];
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (sessionData[1].equals("ON_ROUTE")){
                log.debug("Уже на маршруте");
            }
            if (sessionData[1].equals("ON_QUEUE")){
                //seconds_left - это время до начала поездки у маршрута, нужно просто ее взять и ожидать это время
                try {
                    Thread.sleep(Integer.parseInt(sessionData[2])*1000L - 2000);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
            }
            if (sessionData[1].equals("WAIT_TO_START")){
                //Ждем время для начала отправки машины
                try {
                    Thread.sleep(Integer.parseInt(sessionData[2])*1000L - 2000);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
            }
            StartSessionA();
        }

    }

    private void authorizationDriver(){
        //Authorization driver
        //дописать порядок авторизации и вынести в отдельную функцию
        try {
            response = request.AuthorizationRequest(account);
            authorizationToken = response.getAsJsonObject("result").getAsJsonPrimitive("token").getAsString();
            Scanner scanner = new Scanner(authorizationToken);
            scanner.useDelimiter("\\|");
            scanner.next();
            authorizationToken = scanner.next();
            log.debug("Токен авторизации: " + authorizationToken);
        }
        catch (NullPointerException nullE){
            log.error("Не удалость получить данные с сервера: " + nullE.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    private void  ListDriversCars(){
        try {
            //List of driver's cars
            response = request.ListOfDriversCarsRequest(authorizationToken);
            JsonArray jArray = response.getAsJsonArray("result");
            carId = jArray.get(0).getAsJsonObject().get("id").toString();
        }
        catch (NullPointerException nullE){
            log.error("Не удалость получить данные с сервера: " + nullE.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    private void ListRoutes(){
        try {
            response = request.ListOfRoutesForTheSelectedCarRequest(authorizationToken, carId);
            JsonArray jArray = response.getAsJsonArray("result");
            routeId = jArray.get(84).getAsJsonObject().get("id").toString();
            if(directionRoute)
                terminusId = jArray.get(84).getAsJsonObject().get("terminus").getAsJsonArray().get(0).getAsJsonObject().get("id").toString();
            else
                terminusId = jArray.get(84).getAsJsonObject().get("terminus").getAsJsonArray().get(1).getAsJsonObject().get("id").toString();
        }
        catch (NullPointerException nullE){
                log.error("Не удалость получить данные с сервера: " + nullE.getMessage());
            }
        catch (IOException e) {
                log.error(e.getMessage());
            }
    }
    private String[] ListOfSessions(){
        String[] s = new String[2];
        try {
            response = request.ListOfSessionTimesToGetStartedRequest(authorizationToken, carId, routeId, terminusId);
            JsonObject jo = response.getAsJsonObject("result");
            s[0] = jo.getAsJsonObject().get("times").getAsJsonArray().get(0).getAsJsonObject().get("time").getAsString();
            s[1] = jo.getAsJsonObject().get("times").getAsJsonArray().get(0).getAsJsonObject().get("date").getAsString();
        }
        catch (NullPointerException nullE){
            log.error("Не удалость получить данные с сервера: " + nullE.getMessage());
        }
        catch (IOException e) {
            log.error(e.getMessage());
        }
        return s;
    }
    private String[] StartSessionA() {
        String[] s = new String[3];
        try {
            response = request.StartSessionTypeARequest(authorizationToken, carId, routeId, terminusId, startTime);
            s[0] = response.getAsJsonObject("result").getAsJsonPrimitive("id").getAsString();
            s[1] = response.getAsJsonObject("result").getAsJsonPrimitive("status").getAsString();
            s[2] = response.getAsJsonObject("result").get("start_at").getAsString();
        }
        catch (NullPointerException nullE){
            log.error("Не удалость получить данные с сервера: " + nullE.getMessage());
        }
        catch (IOException e) {
            log.error(e.getMessage());
        }
        return s;
    }
    private String[] ContiniumSessionA() {
        String[] s = new String[3];
        try {
            response = request.ContiniumSessionTypeARequest(authorizationToken,carId,routeId,terminusId);
            s[0] = response.getAsJsonObject("result").getAsJsonPrimitive("id").getAsString();
            s[1] = response.getAsJsonObject("result").getAsJsonPrimitive("status").getAsString();
            s[2] = response.getAsJsonObject("result").get("start_at").getAsString();
        }catch (NullPointerException nullE){
            log.error("Не удалость получить данные с сервера: " + nullE.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return s;
    }
    private void sendLocation(Point currentPoint){
        try {
            request.SendingLocationRequest(authorizationToken, currentPoint.getLat().toString(), currentPoint.getLon().toString());
        }
        catch (IOException e){
            log.error(e.getMessage());
        }
    }
    private String[] informationAboutSession() throws IOException {
        String[] s = new String[3];
        try {
            response = request.InformationAboutTheCurrentSessionRequest(authorizationToken);
            s[0] = response.getAsJsonObject("result").getAsJsonPrimitive("id").getAsString();
            s[1] = response.getAsJsonObject("result").getAsJsonPrimitive("status").getAsString();
            s[2] = response.getAsJsonObject("result").get("seconds_left").getAsString();
            return s;
        }
        catch (ClassCastException e) {
            s = new String[]{"0", "error", "0"};
            return s;
        }
        catch (NullPointerException nullE){
            log.error("Не удалость получить данные с сервера: " + nullE.getMessage());
            s = new String[]{"0", "error", "0"};
            return s;
        }
        catch (IOException e) {
            log.error(e.getMessage());
            s = new String[]{"0", "error", "0"};
            return s;
        }
    }
    private void stopSession(){
        try {
            request.StopSessionRequest(authorizationToken, "Stopping");
        }
        catch (IOException e){
            log.error(e.getMessage());
        }
    }
    private void infAboutTheCurSessionRoute(String AuthorizationKey, RouteBase route){

        try {
            Gson gson = new Gson();
            response = request.InformationAboutTheRouteOfTheCurrentSessionRequest(AuthorizationKey);
            ArrayList<Point> forward = new ArrayList<Point>();
            ArrayList<Point> backward = new ArrayList<Point>();
            JsonArray forw = response.getAsJsonObject("result").getAsJsonObject("path_by_dir").getAsJsonArray("forward");
            JsonArray back = response.getAsJsonObject("result").getAsJsonObject("path_by_dir").getAsJsonArray("backward");


            for(int i = 0; i < forw.size(); i++){
                forward.add(new Point(forw.get(i).getAsJsonObject().getAsJsonPrimitive("lon").getAsDouble(),forw.get(i).getAsJsonObject().getAsJsonPrimitive("lat").getAsDouble()));
            }
            for(int i = 0; i < back.size(); i++){
                backward.add(new Point(back.get(i).getAsJsonObject().getAsJsonPrimitive("lon").getAsDouble(),back.get(i).getAsJsonObject().getAsJsonPrimitive("lat").getAsDouble()));
            }
            route.setRoute_forward(forward);
            route.setRoute_backward(backward);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
