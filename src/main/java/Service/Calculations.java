package Service;

import java.math.BigDecimal;
import model.Point;
//класс для формул
public class Calculations {
    private static final double Radius = 6371.0;
    public double timeDistance(double lat1, double lon1, double lat2, double lon2,  double speed){
        double distance = DistanceBetweenPoints(lat1, lon1, lat2, lon2);
        return 1000*distance/(speed/3.6);

        /*  This function is for calculating the time if you know two points on the map
           model.Point 1 have lat1 = latitude and lon1 = longitude
           model.Point 2 have lat2 = latitude and lon2 = longitude
        */
    }
    public double DistanceBetweenPoints(double lat1, double lon1, double lat2, double lon2){
        double lat = toRad(lat2 - lat1);
        double lon = toRad(lon2 - lon1);
        double a = Math.sin(lat/2) * Math.sin(lat/2) +
                Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
                        Math.sin(lon/2) * Math.sin(lon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = Radius * c;
        return distance * 1000;
    }
    public double DistanceBetweenPoints(Point p1, Point p2){
        return DistanceBetweenPoints(p1.getLat(), p1.getLon(), p2.getLat(), p2.getLon());
    }
    public double DistanceBetweenPoints(Point p1, double lat2, double lon2){
        return DistanceBetweenPoints(p1.getLat(), p1.getLon(), lat2, lon2);
    }

    public Point givePointFromDistance(double lat, double lon, double distance, double lat2, double lon2) {
        double azimuth = azimuth(lat, lon, lat2, lon2);
        double pointLat = Math.asin(Math.sin(toRad(lat))*Math.cos(distance/(Radius*1000))+Math.cos(toRad(lat))*Math.sin(distance/(Radius*1000))*Math.cos(azimuth));
        double pointLon =  Math.atan2(Math.sin(azimuth)*Math.sin(distance/(Radius*1000))*Math.cos(toRad(lat)), Math.cos(distance/(Radius*1000))-Math.sin(toRad(lat))*Math.sin(pointLat));
        return new Point(Math.toDegrees(pointLon)+lon, Math.toDegrees(pointLat));
        /*
          This function calculates coordinates knowing the distance from the initial coordinates,
          the initial coordinates and the azimuth
          lat = latitude
          lon = longitude
          distance = being the distance travelled
          azimuth = is the bearing (clockwise from north)
         */
    }
    private double azimuth(double lat1, double lon1, double lat2, double lon2){
        double x = Math.cos(toRad(lat1))*Math.sin(toRad(lat2)) - Math.sin(toRad(lat1))*Math.cos(toRad(lat2))*Math.cos(toRad(lon2-lon1));
        double y = Math.sin(toRad(lon2-lon1))*Math.cos(toRad(lat2));
        return Math.atan2(y,x);
        /*
          This function calculates the azimuth knowing the coordinates of two points
          model.Point 1 have lat1 = latitude and lon1 = longitude
          model.Point 2 have lat2 = latitude and lon2 = longitude
         */
    }
    private static Double toRad(Double value) {
        return Math.toRadians(value);
    }
}

