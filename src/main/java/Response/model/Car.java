package Response.model;
@lombok.Data
public class Car extends Response.model.Data {
    private int id;
    private String state_number;
    private String garage_number;
    private int use_garage_number;
    private String token;
    private Pivot pivot;
    private boolean available;

    @lombok.Data
    public static class Pivot {
        private int driver_id;
        private int car_id;
    }
}
