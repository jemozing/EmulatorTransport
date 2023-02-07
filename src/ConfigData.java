import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class ConfigData {
    ArrayList<SettingRoute> settingRoutesData = new ArrayList<>(); //Список с настройками маршрутов
    public void readConfigFile(String pathname) throws IOException { //Чтение файла с маршрутами
        Scanner scanner = new Scanner(new File(pathname));
        scanner.nextLine();
        scanner.useDelimiter(System.getProperty("line.separator"));
        while(scanner.hasNext()){
            //парсим строку в объект Employee
            SettingRoute setting = parseCSVLine(scanner.next());
            settingRoutesData.add(setting);
            System.out.println(setting.toString());
        }
        System.out.println(settingRoutesData.size());
        scanner.close();
    }
    private SettingRoute parseCSVLine(String line) {
        int route;
        int numberOfCars;
        int movementInterval;
        int updateFrequency;
        double speed;
        Scanner scanner = new Scanner(line);
        scanner.useDelimiter("\\s*,\\s*");
        route = scanner.nextInt();
        numberOfCars = scanner.nextInt();
        movementInterval = scanner.nextInt();
        updateFrequency = scanner.nextInt();
        speed = scanner.nextDouble();
        return new SettingRoute(route,numberOfCars,movementInterval,updateFrequency,speed);
    }

    public class SettingRoute {
        private int route;
        private int numberOfCars;
        private int movementInterval;
        private int updateFrequency;
        private double speed;

        public SettingRoute(int route, int numberOfCars, int movementInterval, int updateFrequency, double speed) {
            this.route = route;
            this.numberOfCars = numberOfCars;
            this.movementInterval = movementInterval;
            this.updateFrequency = updateFrequency;
            this.speed = speed;
        }

        @Override
        public String toString(){
            return "RouteName=" + this.route + "::NumberOfCars=" + this.numberOfCars + "::MovementInterval=" + this.movementInterval + "::Update Frequency=" + this.updateFrequency + "::Speed=" + this.speed;
        }

        public int getRoute() {
            return route;
        }

        public void setRoute(int route) {
            this.route = route;
        }

        public int getNumberOfCars() {
            return numberOfCars;
        }

        public void setNumberOfCars(int numberOfCars) {
            this.numberOfCars = numberOfCars;
        }

        public int getMovementInterval() {
            return movementInterval;
        }

        public void setMovementInterval(int movementInterval) {
            this.movementInterval = movementInterval;
        }

        public int getUpdateFrequency() {
            return updateFrequency;
        }

        public void setUpdateFrequency(int updateFrequency) {
            this.updateFrequency = updateFrequency;
        }

        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }
    }
}
