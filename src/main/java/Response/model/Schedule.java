package Response.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class Schedule extends Response.model.Data {
    private boolean isToday;
    private String today;
    private String date;
    @SerializedName("times")
    private List<times> times;

    // Геттеры и сеттеры

    @Data
    public class times {
        private String time;
        private String date;
        private String datetime;
        private boolean available;
        // Геттеры и сеттеры
    }
}


