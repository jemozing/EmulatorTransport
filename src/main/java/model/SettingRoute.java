package model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class SettingRoute {

    private int route_id;
    private String route;
    private int numberOfCars;
    private int movementInterval;
    private int updateFrequency;
    private double speed;
    private ArrayList<Account> accounts;

    public SettingRoute(int route_id, String route, int numberOfCars, int movementInterval, int updateFrequency, double speed) {
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
}