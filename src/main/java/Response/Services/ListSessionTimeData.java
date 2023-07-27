package Response.Services;

import Response.model.CarRoutes;
import Response.model.Schedule;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class ListSessionTimeData {
    public Schedule parseJsonData(JsonObject jsonObject){
        Gson gson = new Gson();
        Schedule schedule = gson.fromJson(jsonObject.get("result"), Schedule.class);
        return schedule;
    }
}
