import Response.Services.ServerResponse;
import Service.AccountCSVReader;
import Service.Config;
import Service.DataBaseRequests;
import lombok.extern.slf4j.Slf4j;
import Requests.model.Account;
import model.SettingRoute;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Main {
    public static void main(String[] args) throws IOException {
        // Создаем папку "Logs", если она не существует
        File logsDir = new File("Logs");
        if (!logsDir.exists()) {
            logsDir.mkdir();
        }
        // Генерируем имя файла лога с текущим временем и числом
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_S");
        String logFileName = "Logs/log_" + dateFormat.format(new Date()) + ".log";
        // Устанавливаем путь к файлу лога в системное свойство
        System.setProperty("logFilename", logFileName);
        // Загружаем конфигурацию Log4j
        PropertyConfigurator.configure("src/main/resources/log4j.properties");

        log.info("Эмулятор транспорта версия 1");
        log.info("Во избежании проблем советую приготовить огнетушитель");
        log.info("Приятной DDOS атаки!");

        HttpRequestOld request = new HttpRequestOld();
        DataBaseRequests dataBaseRequests = new DataBaseRequests();
        //Service.Config.readConfigFile("src/main/resources/config.csv");
        Config.readConfig("src/main/resources/config.json");
        Iterator iter = Config.getSettingRoutesData().iterator();
        ArrayList<Account> accounts = AccountCSVReader.CSVRead();
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.authorization(accounts.get(0));

        while (iter.hasNext()){
            SettingRoute setting = (SettingRoute) iter.next();
            dataBaseRequests.addData(setting.getRoute_id(), dataBaseRequests.readDataBase("src/main/java/RouteFiles/" + setting.getRoute_id()  + ".csv"));
        }
        //Основные потоки с водителями
        ExecutorService pool = Executors.newCachedThreadPool();
        /*for (int j = 0; j < Service.Config.getSettingRoutesData().size(); j++){
            for (int i = 0; i < Service.Config.getSettingRoutesData().get(j).getNumberOfCars(); i++) {
                pool.execute(new DriverOld(
                        Service.Config.getSettingRoutesData().get(j),
                        accounts.get(i),
                        i % 2 == 0));
            }
        }*/
        for(int i = 0; i < Config.getSettingRoutesData().get(0).getNumberOfCars(); i++){
            pool.execute(new DriverOld(
                    dataBaseRequests.getData("04210"),
                    Config.getSettingRoutesData().get(0).getMovementInterval()*i,
                    Config.getSettingRoutesData().get(0).getUpdateFrequency(),
                    Config.getSettingRoutesData().get(0).getSpeed(),
                    accounts.get(i).getLogin(),
                    accounts.get(i).getPassword(),
                    request,
                    i%2==0));
        }
    }
}