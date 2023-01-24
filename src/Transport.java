public abstract class Transport {

    public Transport(double start_longitude, double start_latitude, double finish_longitude, double finish_latitude, String name, int number_transport, int update_time, double transport_speed) {
        this.start_longitude = start_longitude; //стартовая координата долготы
        this.start_latitude = start_latitude;//стартовая координата широты
        this.finish_longitude = finish_longitude;//конечная координата долготы
        this.finish_latitude = finish_latitude;//конечная координата широты
        this.name = name;//имя маршрута
        this.number_transport = number_transport;//номер маршрута
        this.update_time = update_time;//время обновления
        this.transport_speed = transport_speed;//скорость транспорта
    }

    private double longitude,latitude; //текущие координаты транспорта
    private double start_longitude,start_latitude;
    private double finish_longitude,finish_latitude;
    private String name;
    private int number_transport;
    private int update_time;
    private double transport_speed;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getStart_longitude() {
        return start_longitude;
    }

    public void setStart_longitude(double start_longitude) {
        this.start_longitude = start_longitude;
    }

    public double getStart_latitude() {
        return start_latitude;
    }

    public void setStart_latitude(double start_latitude) {
        this.start_latitude = start_latitude;
    }

    public double getFinish_longitude() {
        return finish_longitude;
    }

    public void setFinish_longitude(double finish_longitude) {
        this.finish_longitude = finish_longitude;
    }

    public double getFinish_latitude() {
        return finish_latitude;
    }

    public void setFinish_latitude(double finish_latitude) {
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

    public void setTransport_speed(double transport_speed) {
        this.transport_speed = transport_speed;
    }
}
