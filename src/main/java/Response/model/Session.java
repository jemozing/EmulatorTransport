package Response.model;

@lombok.Data
public class Session extends Response.model.Data {
    private int id;
    private String status;
    private String start_at;
    private int seconds_left;
    boolean state;
    // Геттеры и сеттеры
}
