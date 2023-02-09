import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class DataBaseRequests {

    HashMap <Integer, RouteBase> baseData = new HashMap<>();

    public void readDataBase(String pathname){
        try {
            File file = new File(pathname);
            System.out.println(file.getAbsolutePath());
            Scanner sc = new Scanner(file);
            sc.nextLine();
            sc.useDelimiter(",");
            for(int i = 0; i < 5; i++){
            if(sc.hasNext()){
                System.out.println(sc.next());
            }}
            Pattern pattern = Pattern.compile("\\{|}|\"|\\[|]|");
            //sc.useDelimiter("\\{\"\"name\"\":");
            ArrayList<String> routesList = new ArrayList<>();
            //ArrayList<String> routesList2 = new ArrayList<>();
            StringBuilder routeList = new StringBuilder("");
            String[] animals = pattern.split(sc.nextLine());
            System.out.println(animals.length);
            Arrays.asList(animals).forEach(animal -> routeList.append(animal));
            //System.out.println(routelist.toString());
            sc = new Scanner(routeList.toString());
            sc.useDelimiter(",|name:|coordinates:");
            while (sc.hasNext()){
                String s = sc.next();
                if(!s.equals("")) {
                    System.out.println(s);
                    routesList.add(s);
                }

                //routesList1.add(sc.next());
            }
            /*Iterator<String> iter = routesList1.iterator();
            while (iter.hasNext()){
               sc = new Scanner(iter.next());
               sc.useDelimiter("\"\",");
                while (sc.hasNext()){
                    routesList2.add(sc.next());
                }
            }
            routesList1.clear();
            iter = routesList2.iterator();
            while (iter.hasNext()){
                sc = new Scanner(iter.next());
                sc.useDelimiter("\"\"coordinates\"\":\\[");
                while (sc.hasNext()){
                    routesList1.add(sc.next());
                }
            }
            routesList2.clear();
            iter = routesList1.iterator();
            while (iter.hasNext()){
                sc = new Scanner(iter.next());
                sc.useDelimiter("]},\\{");
                while (sc.hasNext()){
                    System.out.println(sc.next());
                }
            }*/
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
