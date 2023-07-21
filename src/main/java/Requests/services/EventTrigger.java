package Requests.services;

import Requests.Requests;
import Requests.Constants;
import com.google.gson.JsonObject;

public class EventTrigger extends Requests {
    public JsonObject request(String authKey, String messageEvent){
        setUrl(Constants.EventTrigger);
        setUseAuthKey(true);
        setUseBodyParams(true);
        setAuthorizationKey(authKey);
        setBodyParameters("{\"event_id\":\"" + messageEvent + "\"}");
        POST_request();
        return response;
    }
}
