import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class DataBase {

    HashMap <Integer, RouteBase> baseData = new HashMap<>();

    public void readDataBase(String pathname){
        try {
            File file = new File(pathname);
            System.out.println(file.getAbsolutePath());
            Scanner sc = new Scanner(file);
            sc.useDelimiter(",");
            while (sc.hasNext()){
                System.out.println(sc.next());
            }
            sc.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void addData(int id){
        baseData.put(id, new RouteBase());
    }
}

class RouteBase{
    int city_id;
    int user_id;
    int number;
    String name = "";
    ArrayList<Point> coordinates = new ArrayList<>();

    public void addCoordinates(Point point){
        coordinates.add(point);
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
}

class Point{
    private double p_longitude,p_latitude; //координаты точки
    Point(double p_longitude,double p_latitude){
        this.p_latitude = p_latitude;
        this.p_longitude = p_longitude;
    }

    public double getP_longitude() {
        return p_longitude;
    }

    public void setP_longitude(double p_longitude) {
        this.p_longitude = p_longitude;
    }

    public double getP_latitude() {
        return p_latitude;
    }

    public void setP_latitude(double p_latitude) {
        this.p_latitude = p_latitude;
    }
}
