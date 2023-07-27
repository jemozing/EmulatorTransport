import lombok.Getter;
import lombok.Setter;
import model.Account;
import lombok.Data;
import model.RouteBase;
import model.SettingRoute;
import model.Transport;

@Data
public class DriverData {
    private Account account;
    private String token;
    private String car_id;
    private RouteBase route;
    private Transport transport_route;
    private SettingRoute settingRoute;
    private int movementInterval;
    private int UpdateFrequency;
    private double speed;
    private boolean directionRoute;
    private String routeId, terminusId, startTime;// Id машины, маршрута, терминала, начальное время
    private String sessionId = "0";
    private String currentSessionId = "0";
    private String currentSessionStatus = "0";
}
