import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

public class Driver implements Runnable{
    RouteBase route;
    Transport transport_route;
    int movementInterval;
    int UpdateFrequency;
    double speed;
    String phone_number;
    String pin_code;
    boolean directionRoute;
    HttpRequest request;
    JsonObject response;
    String authorizationToken;
    String carId, routeId, terminusId, startTime;// Id машины, маршрута, терминала, начальное время
    private static final Logger logger = LoggerFactory.getLogger(Driver.class);
    String sessionId = "0";
    String currentSessionId = "0";
    String currentSessionStatus = "0";
    String[] data = new  String[3];
    String[] sessionData = new String[3];
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
            Thread.sleep(movementInterval * 1000L);//начальная задержка между отправками водителей
        } catch (InterruptedException e) {
            logger.error(e.toString());
        }
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
                logger.debug("Остановка начатой сессии");
            }
            ContiniumSessionA();
            sessionData = informationAboutSession();
            if (sessionData[1].equals("ON_ROUTE")){
                logger.debug("Уже на маршруте");
            }
            if (sessionData[1].equals("ON_QUEUE")){
                //seconds_left - это время до начала поездки у маршрута, нужно просто ее взять и ожидать это время
                try {
                    Thread.sleep(Integer.parseInt(sessionData[2])*1000L - 2000);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }
            }
            if (sessionData[1].equals("WAIT_TO_START")){
                //Ждем время для начала отправки машины
                    try {
                        Thread.sleep(Integer.parseInt(sessionData[2])*1000L - 2000);
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                    }
            }

            //StartSessionA();//старт сессии //тут надо пытаться ловить время
            sessionId = informationAboutSession()[0];//получение Id сессии*/

           /* data = StartSessionA();//старт сессии
            currentSessionId = data[0];
            currentSessionStatus = data[1];*/

        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        //создание транспорта для водителя с указанным параметрами и добавление функций
        transport_route = new Transport(
                route.getRoute_forward().get(0).getP_longitude(),
                route.getRoute_forward().get(0).getP_latitude(),
                route.getRoute_forward().get(route.getRoute_forward().size() - 1).getP_latitude(),
                route.getRoute_forward().get(route.getRoute_forward().size() - 1).getP_longitude(),
                route.getName(),
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
                transport_route.startForward();
                routeIterator = route.getRoute_forward().iterator();
            } else {
                transport_route.startBackward();
                routeIterator = route.getRoute_backward().iterator();
            }
            //текущая точка, следующая точка, промежуточная точка
            Point currentPoint = routeIterator.next(), nextPoint = null, intermediatePoint = null;
            logger.info("Начинаю поездку с конечной точки");
            logger.info("Текущая точка: " + currentPoint.getName() + " " + currentPoint.getP_longitude() + " " + currentPoint.getP_latitude());
            long startTimeTimer = System.currentTimeMillis();//начальное время
            long elapsedTime = 0; //прошедшее время
            long timeAdd = 0;
            //Цикл хождения по маршруту
            while (routeIterator.hasNext()) {
                if (currentSessionStatus.equals("WAIT_TO_START") || currentSessionStatus.equals("ON_ROUTE") || currentSessionStatus.equals("ON_QUEUE")) {
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
                            Thread.sleep(elapsedTime);//ожидание
                        }else {
                            Thread.sleep(timeAdd);//ожидание
                        }
                        if (elapsedTime == 0) {
                            sendLocation(transport_route.getCurrentCoordinates());
                            logger.info("Текущая геопозиция: " + transport_route.getCurrentCoordinates().getP_longitude() + " " + transport_route.getCurrentCoordinates().getP_latitude());
                            startTimeTimer = System.currentTimeMillis();
                        }
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                        throw new RuntimeException(e);
                    }
                    if (currentPoint.hasName()) {
                        logger.info("Текущая остановка: " + currentPoint.getName() + " " + currentPoint.getP_longitude() + " " + currentPoint.getP_latitude());
                        logger.info("Стою 15 секунд");
                        try {
                            Thread.sleep(15L * 1000L);
                            sendLocation(transport_route.getCurrentCoordinates());
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
                    logger.info("Некоректный статус");
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
                logger.debug("Уже на маршруте");
            }
            if (sessionData[1].equals("ON_QUEUE")){
                //seconds_left - это время до начала поездки у маршрута, нужно просто ее взять и ожидать это время
                try {
                    Thread.sleep(Integer.parseInt(sessionData[2])*1000L - 2000);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }
            }
            if (sessionData[1].equals("WAIT_TO_START")){
                //Ждем время для начала отправки машины
                try {
                    Thread.sleep(Integer.parseInt(sessionData[2])*1000L - 2000);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }
            }
            StartSessionA();
        }

    }

    private void authorizationDriver(){
        //Authorization driver
        //дописать порядок авторизации и вынести в отдельную функцию
        try {
            response = request.AuthorizationRequest(phone_number, pin_code);
            authorizationToken = response.getAsJsonObject("result").getAsJsonPrimitive("token").getAsString();
            Scanner scanner = new Scanner(authorizationToken);
            scanner.useDelimiter("\\|");
            scanner.next();
            authorizationToken = scanner.next();
            logger.debug("Токен авторизации: " + authorizationToken);
        }
        catch (NullPointerException nullE){
            logger.error("Не удалость получить данные с сервера: " + nullE.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
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
            logger.error("Не удалость получить данные с сервера: " + nullE.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    private void ListRoutes(){
        try {
            response = request.ListOfRoutesForTheSelectedCarRequest(authorizationToken, carId);
            JsonArray jArray = response.getAsJsonArray("result");
            routeId = jArray.get(84).getAsJsonObject().get("id").toString();
            terminusId = jArray.get(84).getAsJsonObject().get("terminus").getAsJsonArray().get(0).getAsJsonObject().get("id").toString();
        }
        catch (NullPointerException nullE){
                logger.error("Не удалость получить данные с сервера: " + nullE.getMessage());
            }
        catch (IOException e) {
                logger.error(e.getMessage());
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
            logger.error("Не удалость получить данные с сервера: " + nullE.getMessage());
        }
        catch (IOException e) {
            logger.error(e.getMessage());
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
            logger.error("Не удалость получить данные с сервера: " + nullE.getMessage());
        }
        catch (IOException e) {
            logger.error(e.getMessage());
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
            logger.error("Не удалость получить данные с сервера: " + nullE.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return s;
    }
    private void sendLocation(Point currentPoint){
        try {
            request.SendingLocationRequest(authorizationToken, currentPoint.getP_latitude().toString(), currentPoint.getP_longitude().toString());
        }
        catch (IOException e){
            logger.error(e.getMessage());
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
            logger.error("Не удалость получить данные с сервера: " + nullE.getMessage());
            s = new String[]{"0", "error", "0"};
            return s;
        }
        catch (IOException e) {
            logger.error(e.getMessage());
            s = new String[]{"0", "error", "0"};
            return s;
        }
    }
    private void stopSession(){
        try {
            request.StopSessionRequest(authorizationToken, "Stopping");
        }
        catch (IOException e){
            logger.error(e.getMessage());
        }
    }
}
