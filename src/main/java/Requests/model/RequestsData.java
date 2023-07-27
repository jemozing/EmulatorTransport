package Requests.model;

import lombok.Data;

@Data
public class RequestsData {
    private String phone;
    private String pin_code;
    private String event_id;
    private String route_id;
    private String car_id;
    private String authToken;
    private String terminus_id;
    private String lat, lon;
    private String reason;
    private String time;
    private String schedule_id;
}
