package Response.Services;

import Response.model.Route;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RouteData {
    public Route parseJsonData(JsonObject jsonObject){
        Gson gson = new Gson();
        return gson.fromJson(jsonObject.get("result"), Route.class);
    }
}
