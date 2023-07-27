package Response.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class InformAbotSessionAndOther extends Response.model.Data {

    private String direction;
    private int session_id;
    private double[] location;
    private double[] location_closest;
    private double heading;
    private boolean online;
    private String date;
    private Car car;
    private double distance;
    private double distance_total;
    private double distance_recommend;
    private double minimum;
    private int key;
    private String last_special_point;
    private int index;
    private NextLocation next;
    private int distance_status;


    @Data
    public static class NextLocation {
        private double distance;
        private double[][] path;
        private double[] location;
        private double heading;
        private Car car;
        private String last_special_point;
    }
}
