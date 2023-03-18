import java.math.BigDecimal;

public class Calculations {
    public static double timeDistance(double lat1, double lon1, double lat2, double lon2,  double speed){
        double R = 6371; // Earth radius in kilometers
        double lat = toRad(lat2 - lat1);
        double lon = toRad(lon2 - lon1);
        double a = Math.sin(lat/2) * Math.sin(lat/2) +
                Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
                        Math.sin(lon/2) * Math.sin(lon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = R *c;
        return distance/speed;

        /**  This function is for calculating the time if you know two points on the map
        *   Point 1 have lat1 = latitude and lon1 = longitude
        *   Point 2 have lat2 = latitude and lon2 = longitude
        **/
    }
    public static Point givePointFromDistance(double lat, double lon, double distance, double lat2, double lon2) {
        double R = 6371;
        double azimuth = azimuth(lat, lon, lat2, lon2);
        double pointLat = Math.asin(Math.sin(lat)*Math.cos(distance/R)+Math.cos(lat)*Math.sin(distance/R)*Math.cos(azimuth));
        double pointLon = lon + Math.atan2(Math.sin(azimuth)*Math.sin(distance/R)*Math.cos(lat), Math.cos(distance/R)-Math.sin(lat)*Math.sin(pointLat));
        return new Point(new BigDecimal(pointLat), new BigDecimal(pointLon), "");
        /**
         * This function calculates coordinates knowing the distance from the initial coordinates,
         * the initial coordinates and the azimuth
         * lat = latitude
         * lon = longitude
         * distance = being the distance travelled
         * azimuth = is the bearing (clockwise from north)
         */
    }
    private static double azimuth(double lat1, double lon1, double lat2, double lon2){
        return Math.atan2(Math.sin(lon2-lon1)*Math.cos(lat2), Math.cos(lat1)*Math.sin(lat2) - Math.sin(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1));
        /**
         * This function calculates the azimuth knowing the coordinates of two points
         * Point 1 have lat1 = latitude and lon1 = longitude
         * Point 2 have lat2 = latitude and lon2 = longitude
         */
    }
    private static Double toRad(Double value) {
        return value * Math.PI / 180;
    }
}

