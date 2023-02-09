import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        ArrayList<Transport> transport_route = new ArrayList<>();
        transport_route.add(new Transport(1,1,1,1,"20",1,1,1) {});
        transport_route.get(0).setName("20");
        ConfigData configData = new ConfigData();
        //configData.readConfigFile("config.csv");
        HttpRequest request = new HttpRequest();
        //request.GetRequest();
        DataBaseRequests dataBaseRequests = new DataBaseRequests();
        dataBaseRequests.readDataBase("C:\\Users\\ponch\\IdeaProjects\\EmulatorTransport\\out\\production\\EmulatorTransport/RouteFiles/mbus_421.csv");
    }
    public void haversineFormula(double lat2, double lat1, double lon1, double lon2){
        double R = 6371000; // Earth radius in meters
        double Δlat = lat2 - lat1;
        double Δlong = lon2 - lon1;
        double a = Math.sin(Δlat/2) * Math.sin(Δlat/2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(Δlong/2) * Math.sin(Δlong/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = R *c;
    }
}