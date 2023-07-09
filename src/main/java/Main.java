import lombok.extern.slf4j.Slf4j;
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
        PropertyConfigurator.configure("log4j.properties");

        log.info("Эмулятор транспорта версия 1");
        log.info("Во избежании проблем советую приготовить огнетушитель");
        log.info("Приятной DDOS атаки!");

        HttpRequest request = new HttpRequest();
        DataBaseRequests dataBaseRequests = new DataBaseRequests();
        ConfigData.readConfigFile("config.csv");
        Iterator iter = ConfigData.getSettingRoutesData().iterator();
        ArrayList<Account> accounts = AccountCSVReader.CSVRead();
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
                    accounts.get(i).getLogin(),
                    accounts.get(i).getPassword(),
                    request,
                    i%2==0));
        }
    }
}