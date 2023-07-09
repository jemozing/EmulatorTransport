import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CarRoute {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<Terminus> getTerminus() {
        return terminus;
    }

    public void setTerminus(List<Terminus> terminus) {
        this.terminus = terminus;
    }

    // Вложенный класс City

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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStateId() {
            return stateId;
        }

        public void setStateId(int stateId) {
            this.stateId = stateId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }
    }

    // Вложенный класс Terminus

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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getRouteId() {
            return routeId;
        }

        public void setRouteId(int routeId) {
            this.routeId = routeId;
        }

        public int getRouteEventId() {
            return routeEventId;
        }

        public void setRouteEventId(int routeEventId) {
            this.routeEventId = routeEventId;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDisplayType() {
            return displayType;
        }

        public void setDisplayType(String displayType) {
            this.displayType = displayType;
        }

        public String getDisplayValue() {
            return displayValue;
        }

        public void setDisplayValue(String displayValue) {
            this.displayValue = displayValue;
        }
    }
}
