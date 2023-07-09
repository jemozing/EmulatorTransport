import java.util.ArrayList;
import java.util.List;

public class RouteBase {
    private int id;
    private int city_id;
    private int user_id;
    private int number;
    private String name = "";
    private List<Point> route_forward = new ArrayList<>();
    private List<Point> route_backward = new ArrayList<>();

    public void addCoordinatesForward(Point point) {
        route_forward.add(point);
    }

    public void addCoordinatesBackward(Point point) {
        route_backward.add(point);
    }

    public List<Point> getRoute_backward() {
        return route_backward;
    }

    public List<Point> getRoute_forward() {
        return route_forward;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RouteBase{" +
                "id=" + id +
                ", city_id=" + city_id +
                ", user_id=" + user_id +
                ", number=" + number +
                ", name='" + name + '\'' +
                '}';
    }
}
