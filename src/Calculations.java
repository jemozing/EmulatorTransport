public class Calculations {
    public static double timeDistance(double lat1, double lon1, double lat2, double lon2,  double speed){
        double R = 6371000; // Earth radius in meters
        double lat = toRad(lat2 - lat1);
        double lon = toRad(lon2 - lon1);
        double a = Math.sin(lat/2) * Math.sin(lat/2) +
                Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
                        Math.sin(lon/2) * Math.sin(lon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = R *c;
        return distance/1000/speed;
    }
    private static Double toRad(Double value) {
        return value * Math.PI / 180;
    }
}
