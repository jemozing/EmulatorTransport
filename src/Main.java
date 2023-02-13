import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        ArrayList<Transport> transport_route = new ArrayList<>();
        transport_route.add(new Transport(1,1,1,1,"20",1,1,1) {});
        transport_route.get(0).setName("20");
        ConfigData configData = new ConfigData();
        configData.readConfigFile("config.csv");
        HttpRequest request = new HttpRequest();
        request.PostRequest();
        request.GetRequest();
        DataBaseRequests dataBaseRequests = new DataBaseRequests();
        dataBaseRequests.readDataBase("C:\\Users\\ponch\\IdeaProjects\\EmulatorTransport\\out\\production\\EmulatorTransport/RouteFiles/mbus_421.csv");
        //System.out.println(timeDistance(73.3017573000000055571945267729461193084716796875,55.07409440899999708562972955405712127685546875,73.29325199999999540523276664316654205322265625,55.071810999999996738551999442279338836669921875,20));
    }
    public static double timeDistance(double lat1, double lon1, double lat2, double lon2,  double speed){
        double R = 6371000; // Earth radius in meters
        double lat = toRad(lat2 - lat1);
        double lon = toRad(lon2 - lon1);
        double a = Math.sin(lat/2) * Math.sin(lat/2) +
                Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
                        Math.sin(lon/2) * Math.sin(lon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = R *c;
        return distance/1000/speed;
    }
    private static Double toRad(Double value) {
        return value * Math.PI / 180;
    }

}