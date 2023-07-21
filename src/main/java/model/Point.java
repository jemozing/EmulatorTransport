package model;

import java.math.BigDecimal;

public class Point {
    private BigDecimal lon, lat; //координаты точки
    private String name;

    public Point(BigDecimal lon, BigDecimal lat, String name) {
        this.lon = lon;
        this.lat = lat;
        this.name = name;
    }
    public Point(double lon, double lat) {
        this.lon = new BigDecimal(lon);
        this.lat = new BigDecimal(lat);
        this.name = "";
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public String getName() {
        return name;
    }

    public boolean hasName() {
        if (name == null) {
            return false;
        } else return !name.isEmpty();
    }
}
