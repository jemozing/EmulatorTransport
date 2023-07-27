package Response.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import model.Point;

import java.util.ArrayList;
import java.util.List;

@Data
public class Route extends Response.model.Data {
    @SerializedName("type")
    int type;
    @SerializedName("path")
    ArrayList<Point> path;
    @SerializedName("points")
    ArrayList<BusStation> points;
    @SerializedName("path_by_dir")
    Path_by_dir path_by_dir;
    @SerializedName("points_by_dir")
    Points_by_dir points_by_dir;

    @Data
    public class Path_by_dir{

        @SerializedName("forward")
        ArrayList<Point> forward;
        @SerializedName("backward")
        ArrayList<Point> backward;
    }
    @Data
    public class Points_by_dir{
        @SerializedName("forward")
        ArrayList<BusStation> forward;
        @SerializedName("backward")
        ArrayList<BusStation> backward;
    }
    @Data
    public class BusStation {
        private int id;
        private int route_id;
        private int route_event_id;
        private String direction;
        private int order;
        private double lat;
        private double lon;
        private String name;
        private String display_type;
        private String display_value;
        private Event event;
        // Геттеры и сеттеры

        @Data
        public class Event {
            private int id;
            private String name;
            private String code;
            private int trigger_radius;
            private String handle;
            // Геттеры и сеттеры
        }
    }
}
