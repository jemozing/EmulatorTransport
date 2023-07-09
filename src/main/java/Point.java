import java.math.BigDecimal;

public class Point {
    private BigDecimal p_longitude, p_latitude; //координаты точки
    private String name;

    public Point(BigDecimal p_longitude, BigDecimal p_latitude, String name) {
        this.p_longitude = p_longitude;
        this.p_latitude = p_latitude;
        this.name = name;
    }

    public BigDecimal getP_longitude() {
        return p_longitude;
    }

    public void setP_longitude(BigDecimal p_longitude) {
        this.p_longitude = p_longitude;
    }

    public BigDecimal getP_latitude() {
        return p_latitude;
    }

    public void setP_latitude(BigDecimal p_latitude) {
        this.p_latitude = p_latitude;
    }

    public String getName() {
        return name;
    }

    public boolean hasName() {
        if (name == null) {
            return false;
        } else return !name.isEmpty();
    }
}
