import java.io.IOException;
import java.util.*;

import com.google.gson.JsonObject;
public class Driver implements Runnable{
    Transport transport_route;
    List<Point> route_forward = new ArrayList<Point>();
    List<Point> route_backward = new ArrayList<Point>();
    String name;
    int movementInterval;
    int UpdateFrequency;
    double speed;

    String phone_number;
    String pin_code;
    boolean direction = true;
    HttpRequest request;
    JsonObject response;
    String authorizationToken;
    public Driver(List<Point> route_forward, List<Point> route_backward, String name, int movementInterval, int UpdateFrequency, double speed, String phone_number, String pin_code, HttpRequest request) {
        this.route_forward = route_forward;
        this.route_backward = route_backward;
        this.name = name;
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
                route_forward.get(0).getP_longitude(),
                route_forward.get(0).getP_latitude(),
                route_forward.get(route_forward.size() - 1).getP_latitude(),
                route_forward.get(route_forward.size() - 1).getP_longitude(),
                name,
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
        Iterator<Point> listIterator = route_forward.iterator();
        Point lastPoint = listIterator.next(), currentPoint;
        System.out.println(lastPoint.getName()+ " " + lastPoint.getP_longitude() + " " + lastPoint.getP_latitude());
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0;
        while (listIterator.hasNext()){
                currentPoint = listIterator.next();
                try {
                    long time = Math.round(Calculations.timeDistance(
                            lastPoint.getP_latitude().doubleValue(),
                            lastPoint.getP_latitude().doubleValue(),
                            currentPoint.getP_latitude().doubleValue(),
                            currentPoint.getP_latitude().doubleValue(),
                            transport_route.getTransport_speed()) * 60 * 60 * 1000);
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
            if (elapsedTime >= 15*1000) {
                //System.out.println("Время потрачено: " + elapsedTime);
                startTime = System.currentTimeMillis();
                try {
                    request.SendingLocationRequest(authorizationToken,currentPoint.getP_latitude().toString(), currentPoint.getP_longitude().toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                elapsedTime = 0;
            }
                lastPoint = currentPoint;
                elapsedTime = System.currentTimeMillis() - startTime;
        }
    }
}
