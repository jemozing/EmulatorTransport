package Response.Services;

import Response.model.ContiniumA;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ContiniunSessionData {

    public ContiniumA parseJsonData(JsonObject jsonObject){
        Gson gson = new Gson();
        return gson.fromJson(jsonObject.get("result"), ContiniumA.class);
    }
}
