package Response.Services;

import Response.model.Car;
import Response.model.Schedule;
import Response.model.Session;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class SessionData {
    public Session parseJsonData(JsonObject serverResponse){
        Gson gson = new Gson();
        Session session = new Session();
        if(serverResponse.get("result").toString().equals("false")){
            session.setState(false);
        } else {
           session = gson.fromJson(serverResponse.get("result"), Session.class);
           session.setState(true);
        }
        return session;
    }
}
