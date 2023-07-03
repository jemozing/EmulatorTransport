import org.slf4j.Logger;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class ConfigData {
    static Logger logger = Main.getLogger();
    private static ArrayList<SettingRoute> settingRoutesData = new ArrayList<SettingRoute>(); //Список с настройками маршрутов

    public static void readConfigFile(String pathname) throws IOException { //Чтение файла с маршрутами
        Scanner scanner = new Scanner(new File(pathname));
        scanner.nextLine();
        scanner.useDelimiter(System.getProperty("line.separator"));
        while (scanner.hasNext()) {
            //парсим строку в объект Employee
            SettingRoute setting = parseCSVLine(scanner.next());
            settingRoutesData.add(setting);
            logger.info(setting.toString());
        }
        logger.info(Integer.toString(settingRoutesData.size()));
        scanner.close();
    }

    private static SettingRoute parseCSVLine(String line) {
        String route_id;
        int route;
        int numberOfCars;
        int movementInterval;
        int updateFrequency;
        double speed;
        Scanner scanner = new Scanner(line);
        scanner.useDelimiter("\\s*,\\s*");
        route_id = scanner.next();
        route = scanner.nextInt();
        numberOfCars = scanner.nextInt();
        movementInterval = scanner.nextInt();
        updateFrequency = scanner.nextInt();
        speed = scanner.nextDouble();
        return new SettingRoute(route_id, route, numberOfCars, movementInterval, updateFrequency, speed);
    }

    public static ArrayList<SettingRoute> getSettingRoutesData() {
        return settingRoutesData;
    }
}
