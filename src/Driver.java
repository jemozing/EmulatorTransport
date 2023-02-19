import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Driver implements Runnable{
    ArrayList<Transport> transport_route = new ArrayList<Transport>();
    List<Point> route_forward = new ArrayList<Point>();
    List<Point> route_backward = new ArrayList<Point>();
    String name;
    int NumberOfCars;
    int UpdateFrequency;
    double speed;

    public Driver(List<Point> route_forward, List<Point> route_backward, String name, int NumberOfCars, int UpdateFrequency, double speed){
        this.route_forward = route_forward;
        this.route_backward = route_backward;
        this.name = name;
        this.NumberOfCars = NumberOfCars;
        this.UpdateFrequency = UpdateFrequency;
        this.speed = speed;
    }
    @Override
    public void run() {
        transport_route.add(new Transport(
                route_forward.get(0).getP_longitude(),
                route_forward.get(0).getP_latitude(),
                route_forward.get(route_forward.size() - 1).getP_latitude(),
                route_forward.get(route_forward.size() - 1).getP_longitude(),
                name,
                NumberOfCars,
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
        });
        transport_route.get(0).startForward();
        Iterator<Point> listIterator = route_forward.iterator();
        Point lastPoint = listIterator.next(), currentPoint;
        System.out.println(lastPoint.getName()+ " " + lastPoint.getP_longitude() + " " + lastPoint.getP_latitude());
        while (listIterator.hasNext()){
            currentPoint = listIterator.next();
            try {
                long time = Math.round(Calculations.timeDistance(
                        lastPoint.getP_latitude().doubleValue(),
                        lastPoint.getP_latitude().doubleValue(),
                        currentPoint.getP_latitude().doubleValue(),
                        currentPoint.getP_latitude().doubleValue(),
                        transport_route.get(0).getTransport_speed()) * 60 * 60 * 1000);
                Thread.currentThread().sleep(time);
                if (currentPoint.hasName()){
                    System.out.println(currentPoint.getName() + " " + currentPoint.getP_longitude() + " " + currentPoint.getP_latitude());
                    System.out.println("Стою 10 секунд");
                    Thread.currentThread().sleep(10*1000);
                }
                else {
                    System.out.println("Pass the point"+ " " + currentPoint.getP_longitude() + " " + currentPoint.getP_latitude() + "  Времени потрачено: " + time / 1000);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            lastPoint = currentPoint;
        }
    }
}
