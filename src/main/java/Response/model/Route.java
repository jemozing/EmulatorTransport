package Response.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class Route extends Response.model.Data {
    @SerializedName("type")
    int type;
    @SerializedName("path")
    List<Point> path;
    @SerializedName("points")
    List<BusStation> points;
    @SerializedName("path_by_dir")
    Path_by_dir path_by_dir;
    @SerializedName("points_by_dir")
    Points_by_dir points_by_dir;

    @Data
    public class Path_by_dir{

        @SerializedName("forward")
        List<Point> forward;
        @SerializedName("backward")
        List<Point> backward;
    }
    @Data
    public class Points_by_dir{
        @SerializedName("forward")
        List<BusStation> forward;
        @SerializedName("backward")
        List<BusStation> backward;
    }
    @Data
    public class Point{
        double lon;
        double lat;
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
