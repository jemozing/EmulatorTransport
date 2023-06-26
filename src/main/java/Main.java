import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws IOException {
        logger.info("Эмулятор транспорта версия 1");
        logger.info("Во избежании проблем советую приготовить огнетушитель");
        logger.info("Приятной DDOS атаки!");
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
            dataBaseRequests.addData(setting.getRoute_id(), dataBaseRequests.readDataBase("src/main/java/RouteFiles/" + setting.getRoute_id()  + ".csv"));
        }
        //Основные потоки с водителями
        ExecutorService pool = Executors.newCachedThreadPool();
        for(int i = 0; i < ConfigData.getSettingRoutesData().get(0).getNumberOfCars(); i++){
            pool.execute(new Driver(
                    dataBaseRequests.getData("04210"),
                    ConfigData.getSettingRoutesData().get(0).getMovementInterval()*i,
                    ConfigData.getSettingRoutesData().get(0).getUpdateFrequency(),
                    ConfigData.getSettingRoutesData().get(0).getSpeed(),
                    phoneNumbers.get(i),
                    pinCodes.get(i),
                    request,
                    i%2==0,
                    logger));
        }
        //request.AuthorizationRequest("7 000 000-00-01", "1111");

    }

    public static Logger getLogger() {
        return logger;
    }
}