package model;

import lombok.Data;
import model.Point;

import java.math.BigDecimal;

@Data
public class Transport {
    private Point currentPoint; //текущие координаты транспорта
    private Point startPoint;
    private Point finishPoint;
    private String name;
    private int number_transport;
    private int update_time;
    private double transport_speed;

    public Transport (Point startPoint, Point finishPoint, String name, int number_transport, int update_time, double transport_speed){
        this.startPoint = startPoint;
        this.finishPoint = finishPoint;
        this.name = name;
        this.number_transport = number_transport;
        this.update_time = update_time;
        this.transport_speed = transport_speed;
    }
}
