package Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import model.Account;
import model.SettingRoute;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

@Slf4j
public class Config {
    private static ArrayList<SettingRoute> settingRoutesData = new ArrayList<SettingRoute>(); //Список с настройками маршрутов

    @SuppressWarnings("ReassignedVariable")
    public static void readConfigFile(String pathname) { //Чтение файла с маршрутами
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(pathname));
            log.debug("Файл найден");
        } catch (FileNotFoundException e) {
            log.error(e.toString() + "\n Файл не найден");
            throw new RuntimeException(e);
        }
        scanner.nextLine();
        scanner.useDelimiter(System.getProperty("line.separator"));
        while (scanner.hasNext()) {
            //парсим строку в объект Employee
            SettingRoute setting = parseCSVLine(scanner.next());
            settingRoutesData.add(setting);
            log.debug(setting.toString());
            log.debug("Настройки успешно прочитаны");
        }
        log.debug(Integer.toString(settingRoutesData.size()));
        scanner.close();
    }

    private static SettingRoute parseCSVLine(String line) {
        int route_id;
        String route;
        int numberOfCars;
        int movementInterval;
        int updateFrequency;
        double speed;
        Scanner scanner = new Scanner(line);
        scanner.useDelimiter("\\s*,\\s*");
        route_id = scanner.nextInt();
        route = scanner.next();
        numberOfCars = scanner.nextInt();
        movementInterval = scanner.nextInt();
        updateFrequency = scanner.nextInt();
        speed = scanner.nextDouble();
        return new SettingRoute(route_id, route, numberOfCars, movementInterval, updateFrequency, speed);
    }

    public static ArrayList<SettingRoute> getSettingRoutesData() {
        return settingRoutesData;
    }

    public static void readConfig(String pathname){
        ArrayList<SettingRoute> settings = new ArrayList<>();
        Gson gson = new Gson();
        JsonObject jsonObject;
        try (FileReader reader = new FileReader(pathname)) {
            jsonObject = (JsonObject) JsonParser.parseReader(reader);
            for (int i = 0; i < jsonObject.getAsJsonArray("config").size(); i++){
                settings.add(gson.fromJson(jsonObject.getAsJsonArray("config").get(i), SettingRoute.class));
            }
            settingRoutesData = settings;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
