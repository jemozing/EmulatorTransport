import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        DataBaseRequests dataBaseRequests = new DataBaseRequests();
        ConfigData.readConfigFile("config.csv");
        Iterator iter = ConfigData.getSettingRoutesData().iterator();
        while (iter.hasNext()){
            SettingRoute setting = (SettingRoute) iter.next();
            dataBaseRequests.addData(setting.getRoute_id(), dataBaseRequests.readDataBase("C:\\Users\\ponch\\IdeaProjects\\EmulatorTransport\\out\\production\\EmulatorTransport/RouteFiles/" + setting.getRoute_id()  + ".csv"));
        }
        Driver driver = new Driver(dataBaseRequests.getData("04210").getRoute_forward(), dataBaseRequests.getData("04210").getRoute_backward(), dataBaseRequests.getData("04210").getName(), ConfigData.getSettingRoutesData().get(0).getNumberOfCars(),
                ConfigData.getSettingRoutesData().get(0).getUpdateFrequency(),
                ConfigData.getSettingRoutesData().get(0).getSpeed());
        Thread thread = new Thread(driver);
        thread.start();

        HttpRequest request = new HttpRequest();
        //request.AuthorizationRequest("7 000 000-00-01", "1111");

    }


}