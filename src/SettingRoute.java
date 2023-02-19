public class SettingRoute {

    private String route_id;
    private int route;
    private int numberOfCars;
    private int movementInterval;
    private int updateFrequency;
    private double speed;

    public SettingRoute(String route_id, int route, int numberOfCars, int movementInterval, int updateFrequency, double speed) {
        this.route_id = route_id;
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

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }
}