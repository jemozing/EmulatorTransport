import java.math.BigDecimal;

public abstract class Transport {

    public Transport(BigDecimal start_longitude, BigDecimal start_latitude, BigDecimal finish_longitude, BigDecimal finish_latitude, String name, int number_transport, int update_time, double transport_speed) {
        this.start_longitude = start_longitude; //стартовая координата долготы
        this.start_latitude = start_latitude;//стартовая координата широты
        this.finish_longitude = finish_longitude;//конечная координата долготы
        this.finish_latitude = finish_latitude;//конечная координата широты
        this.name = name;//имя маршрута
        this.number_transport = number_transport;//номер маршрута
        this.update_time = update_time;//время обновления
        this.transport_speed = transport_speed;//скорость транспорта
    }

    private BigDecimal longitude,latitude; //текущие координаты транспорта
    private BigDecimal start_longitude,start_latitude;
    private BigDecimal finish_longitude,finish_latitude;
    private String name;
    private int number_transport;
    private int update_time;
    private double transport_speed;

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getStart_longitude() {
        return start_longitude;
    }

    public void setStart_longitude(BigDecimal start_longitude) {
        this.start_longitude = start_longitude;
    }

    public BigDecimal getStart_latitude() {
        return start_latitude;
    }

    public void setStart_latitude(BigDecimal start_latitude) {
        this.start_latitude = start_latitude;
    }

    public BigDecimal getFinish_longitude() {
        return finish_longitude;
    }

    public void setFinish_longitude(BigDecimal finish_longitude) {
        this.finish_longitude = finish_longitude;
    }

    public BigDecimal getFinish_latitude() {
        return finish_latitude;
    }

    public void setFinish_latitude(BigDecimal finish_latitude) {
        this.finish_latitude = finish_latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber_transport() {
        return number_transport;
    }

    public void setNumber_transport(int number_transport) {
        this.number_transport = number_transport;
    }

    public int getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(int update_time) {
        this.update_time = update_time;
    }

    public double getTransport_speed() {
        return transport_speed;
    }

    public Point getCurrentCoordinates() {
        return new Point(longitude,latitude, "");
    }
    public void setCurrentCoordinates(BigDecimal longitude, BigDecimal latitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }
    public void setCurrentCoordinates(Point currentPoint){
        this.longitude = currentPoint.getLon();
        this.latitude = currentPoint.getLat();
    }
    public void setTransport_speed(double transport_speed) {
        this.transport_speed = transport_speed;
    }

    public abstract void startForward();

    public abstract void startBackward();
}
