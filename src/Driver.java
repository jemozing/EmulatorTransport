import java.io.IOException;
import java.util.*;

import com.google.gson.JsonObject;
public class Driver implements Runnable{
    RouteBase route;
    Transport transport_route;
    int movementInterval;
    int UpdateFrequency;
    double speed;

    String phone_number;
    String pin_code;
    boolean direction = true;
    HttpRequest request;
    JsonObject response;
    String authorizationToken;
    public Driver(RouteBase route, int movementInterval, int UpdateFrequency, double speed, String phone_number, String pin_code, HttpRequest request){
        this.route = route;
        this.movementInterval = movementInterval;
        this.UpdateFrequency = UpdateFrequency;
        this.speed = speed;
        this.phone_number = phone_number;
        this.pin_code = pin_code;
        this.request = request;
    }
    @Override
    public void run() {
        try {
            Thread.currentThread().sleep(movementInterval * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            //Authorization driver
            response = request.AuthorizationRequest(phone_number, pin_code);
            authorizationToken = response.getAsJsonObject("result").getAsJsonPrimitive("token").getAsString();
            Scanner scanner = new Scanner(authorizationToken);
            scanner.useDelimiter("\\|");
            scanner.next();
            authorizationToken = scanner.next();
            System.out.println(authorizationToken);
            //request.StartSessionTypeARequest(authorizationToken, "123", "421", "321", "10");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        transport_route = new Transport(
                route.getRoute_forward().get(0).getP_longitude(),
                route.getRoute_forward().get(0).getP_latitude(),
                route.getRoute_forward().get(route.getRoute_forward().size() - 1).getP_latitude(),
                route.getRoute_forward().get(route.getRoute_forward().size() - 1).getP_longitude(),
                route.name,
                movementInterval,
                UpdateFrequency,
                speed)
        {
            @Override
            public void startForward(){
                setLongitude(getStart_longitude());
                setLatitude(getStart_latitude());
            }
            @Override
            public void startBackward(){
                setLongitude(getFinish_longitude());
                setLatitude(getFinish_latitude());
            }
        };

        transport_route.startForward();
        Iterator<Point> listIterator = route.getRoute_forward().iterator();
        Point lastPoint = listIterator.next(), currentPoint;
        System.out.println(lastPoint.getName()+ " " + lastPoint.getP_longitude() + " " + lastPoint.getP_latitude());
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0;
        //Цикл хождения по маршруту
        while (listIterator.hasNext()){
                currentPoint = listIterator.next();
                try {
                    long time = Math.round(Calculations.timeDistance(
                            lastPoint.getP_latitude().doubleValue(),
                            lastPoint.getP_longitude().doubleValue(),
                            currentPoint.getP_latitude().doubleValue(),
                            currentPoint.getP_longitude().doubleValue(),
                            transport_route.getTransport_speed()) * 60 * 60 * 1000);//Расчет времени до следующей точки
                    if (elapsedTime + time > 15*1000){
                        long timeAdd = 15*1000 - elapsedTime; //Сколько времени осталось до 15 секунд;
                        Point betweenPoint = Calculations.givePointFromDistance(
                                            lastPoint.getP_latitude().doubleValue(),
                                            lastPoint.getP_longitude().doubleValue(),
                                    time*speed/60/60/1000,
                                            currentPoint.getP_latitude().doubleValue(),
                                            currentPoint.getP_longitude().doubleValue());
                        startTime = System.currentTimeMillis();
                        try {
                            request.SendingLocationRequest(authorizationToken,betweenPoint.getP_latitude().toString(), betweenPoint.getP_longitude().toString());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        elapsedTime = 0;
                    }
                    if(elapsedTime + time == 15*1000) {

                    }
                    Thread.currentThread().sleep(time);

                    if (currentPoint.hasName()) {
                        System.out.println(currentPoint.getName() + " " + currentPoint.getP_longitude() + " " + currentPoint.getP_latitude());
                        System.out.println("Стою 10 секунд");
                        Thread.currentThread().sleep(10 * 1000);
                    } else {
                        System.out.println("Pass the point" + " " + currentPoint.getP_longitude() + " " + currentPoint.getP_latitude() + "  Времени потрачено: " + time / 1000);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            lastPoint = currentPoint;
            elapsedTime = System.currentTimeMillis() - startTime;
        }
    }
}
