package Response.Services;

import Response.model.InformAbotSessionAndOther;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class InformAbotSessionAndOtherData {
    public InformAbotSessionAndOther parseJsonData(JsonObject jsonObject){
        Gson gson = new Gson();
        ArrayList<InformAbotSessionAndOther> sessions = new ArrayList<>();
        for (int i = 0; i < jsonObject.getAsJsonArray("result").size(); i++){
            sessions.add(gson.fromJson(jsonObject.getAsJsonArray("result").get(i), InformAbotSessionAndOther.class));
        }
        return sessions.get(0);
    }
}
