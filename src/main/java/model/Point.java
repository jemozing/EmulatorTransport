package model;

import java.math.BigDecimal;

public class Point {
    private BigDecimal lonB, latB; //координаты точки
    private double lon, lat;
    private String name;

    public Point(BigDecimal lonB, BigDecimal latB, String name) {
        this.lonB = lonB;
        this.latB = latB;
        this.name = name;
    }
    public Point(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
        this.name = "";
    }

    public BigDecimal getLonB() {
        return lonB;
    }

    public void setLonB(BigDecimal lonB) {
        this.lonB = lonB;
    }

    public BigDecimal getLatB() {
        return latB;
    }

    public void setLatB(BigDecimal latB) {
        this.latB = latB;
    }

    public String getName() {
        return name;
    }

    public boolean hasName() {
        if (name == null) {
            return false;
        } else return !name.isEmpty();
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setName(String name) {
        this.name = name;
    }
}
