import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class ConfigData {
    private static final Logger logger = LoggerFactory.getLogger(ConfigData.class);
    private static ArrayList<SettingRoute> settingRoutesData = new ArrayList<SettingRoute>(); //Список с настройками маршрутов

    @SuppressWarnings("ReassignedVariable")
    public static void readConfigFile(String pathname) { //Чтение файла с маршрутами
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(pathname));
            logger.debug("Файл найден");
        } catch (FileNotFoundException e) {
            logger.error(e.toString() + "\n Файл не найден");
            throw new RuntimeException(e);
        }
        scanner.nextLine();
        scanner.useDelimiter(System.getProperty("line.separator"));
        while (scanner.hasNext()) {
            //парсим строку в объект Employee
            SettingRoute setting = parseCSVLine(scanner.next());
            settingRoutesData.add(setting);
            logger.debug(setting.toString());
            logger.debug("Настройки успешно прочитаны");
        }
        logger.debug(Integer.toString(settingRoutesData.size()));
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
