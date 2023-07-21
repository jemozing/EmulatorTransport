package Response.model;

@lombok.Data
public class ContiniumA extends Response.model.Data {
    private int driver_id;
    private String route_id;
    private String car_id;
    private String start_point_id;
    private String start_at;
    private String status;
    private String updated_at;
    private String created_at;
    private int id;

    // Геттеры и сеттеры
}
