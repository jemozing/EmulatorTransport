package Response.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@lombok.Data
public class CarRoutes extends Response.model.Data {
    @SerializedName("id")
    private int id;

    @SerializedName("type")
    private int type;

    @SerializedName("name")
    private String name;

    @SerializedName("number")
    private String number;

    @SerializedName("city")
    private City city;

    @SerializedName("terminus")
    private List<Terminus> terminus;

    // Геттеры и сеттеры

    // Вложенный класс City

    @lombok.Data
    public static class City {
        @SerializedName("id")
        private int id;

        @SerializedName("state_id")
        private int stateId;

        @SerializedName("name")
        private String name;

        @SerializedName("slug")
        private String slug;

        // Геттеры и сеттеры
    }

    // Вложенный класс Terminus

    @lombok.Data
    public static class Terminus {
        @SerializedName("id")
        private int id;

        @SerializedName("route_id")
        private int routeId;

        @SerializedName("route_event_id")
        private int routeEventId;

        @SerializedName("direction")
        private String direction;

        @SerializedName("order")
        private int order;

        @SerializedName("lat")
        private double lat;

        @SerializedName("lon")
        private double lon;

        @SerializedName("name")
        private String name;

        @SerializedName("display_type")
        private String displayType;

        @SerializedName("display_value")
        private String displayValue;

        // Геттеры и сеттеры
    }
}
