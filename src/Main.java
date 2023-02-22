import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpRequest request = new HttpRequest();
        DataBaseRequests dataBaseRequests = new DataBaseRequests();
        ConfigData.readConfigFile("config.csv");
        Iterator iter = ConfigData.getSettingRoutesData().iterator();
        ArrayList<String> phoneNumbers = new ArrayList<String>();
        ArrayList<String> pinCodes = new ArrayList<String>();
        for (int i = 0; i < ConfigData.getSettingRoutesData().get(0).getNumberOfCars(); i++) {
            phoneNumbers.add("7 000 000-00-0" + (i + 1));
            pinCodes.add("1111");
        }
        while (iter.hasNext()){
            SettingRoute setting = (SettingRoute) iter.next();
            dataBaseRequests.addData(setting.getRoute_id(), dataBaseRequests.readDataBase("out\\production\\EmulatorTransport/RouteFiles/" + setting.getRoute_id()  + ".csv"));
        }

        ExecutorService pool = Executors.newCachedThreadPool();
        for(int i = 0; i < ConfigData.getSettingRoutesData().get(0).getNumberOfCars(); i++){
            pool.execute(new Driver(dataBaseRequests.getData("04210").getRoute_forward(),
                    dataBaseRequests.getData("04210").getRoute_backward(),
                    dataBaseRequests.getData("04210").getName(),
                    60*i,
                    ConfigData.getSettingRoutesData().get(0).getUpdateFrequency(),
                    ConfigData.getSettingRoutesData().get(0).getSpeed(),
                    phoneNumbers.get(i),
                    pinCodes.get(i),
                    request));
        }
        //request.AuthorizationRequest("7 000 000-00-01", "1111");

    }


}