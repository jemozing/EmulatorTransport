import Response.model.Route;
import model.Account;
import lombok.Data;
import model.SettingRoute;
import model.Transport;

@Data
public class DriverData {
    private Account account;
    private String token;
    private String car_id;
    private Route route;
    //private Transport transport_route;
    //private SettingRoute settingRoute;
    private int movementInterval;
    private int updateFrequency;
    private double speed;
    private boolean directionRoute;
    private String route_name;
    private String routeId, terminusId, startTime;// Id машины, маршрута, терминала, начальное время
    private String sessionId = "0";
    private String currentSessionId = "0";
    private String currentSessionStatus = "0";
}
