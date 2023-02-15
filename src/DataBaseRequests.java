import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class DataBaseRequests {

    HashMap <Integer, RouteBase> baseData = new HashMap<>();

    public RouteBase readDataBase(String pathname) throws IOException {
        RouteBase routeBase = new RouteBase();
        File CSVfile = new File(pathname);
        Scanner sc = new Scanner(CSVfile);
        sc.nextLine();
        sc.useDelimiter(",");
        routeBase.setId(sc.nextInt());
        routeBase.setCity_id(sc.nextInt());
        routeBase.setUser_id(sc.nextInt());
        routeBase.setNumber(sc.nextInt());
        routeBase.setName(sc.next());

        sc.useDelimiter("}]\"");
        StringBuilder route_forward_buffer = new StringBuilder(sc.next());
        StringBuilder route_backward_buffer = new StringBuilder(sc.next());
        route_forward_buffer.delete(0,2);
        route_forward_buffer.append("}]");
        route_backward_buffer.delete(0,2);
        route_backward_buffer.append("}]");
        sc.close();

        JsonParser jsonParser = new JsonParser();
        JsonArray json_forward = (JsonArray) jsonParser.parse(route_forward_buffer.toString().replace("\"\"", "\""));
        JsonArray json_backward = (JsonArray) jsonParser.parse(route_backward_buffer.toString().replace("\"\"", "\""));
        System.out.println(json_forward.toString());
        JsonObject object;
        String name;
        for(JsonElement element : json_forward){
            object = element.getAsJsonObject();
            if(object.keySet().size() == 2){
                name = object.get("name").getAsString();
            }
            else{
                name = null;
            }
            routeBase.addCoordinatesForward(new Point(
                    object.get("coordinates").getAsJsonArray().get(0).getAsBigDecimal(),
                    object.get("coordinates").getAsJsonArray().get(1).getAsBigDecimal(),
                    name
            ));
        }
        for(JsonElement element : json_backward){
            object = element.getAsJsonObject();
            if(object.keySet().size() == 2){
                name = object.get("name").getAsString();
            }
            else{
                name = null;
            }
            routeBase.addCoordinatesBackward(new Point(
                    object.get("coordinates").getAsJsonArray().get(0).getAsBigDecimal(),
                    object.get("coordinates").getAsJsonArray().get(1).getAsBigDecimal(),
                    name
            ));
        }
        return routeBase;
    }
    public void addData(int id){
        baseData.put(id, new RouteBase());
    }
}

class RouteBase{
    int id;
    int city_id;
    int user_id;
    int number;
    String name = "";
    List<Point> route_forward = new ArrayList<>();
    List<Point> route_backward = new ArrayList<>();

    public void addCoordinatesForward(Point point){
        route_forward.add(point);
    }
    public void addCoordinatesBackward(Point point){
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

class Point{
    private BigDecimal p_longitude,p_latitude; //координаты точки
    String name;

    public Point(BigDecimal p_longitude, BigDecimal p_latitude, String name) {
        this.p_longitude = p_longitude;
        this.p_latitude = p_latitude;
        this.name = name;
    }

    public BigDecimal getP_longitude() {
        return p_longitude;
    }

    public void setP_longitude(BigDecimal p_longitude) {
        this.p_longitude = p_longitude;
    }

    public BigDecimal getP_latitude() {
        return p_latitude;
    }

    public void setP_latitude(BigDecimal p_latitude) {
        this.p_latitude = p_latitude;
    }
}
