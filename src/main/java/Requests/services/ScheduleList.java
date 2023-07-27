package Requests.services;

import Requests.Requests;
import Requests.Constants;
import com.google.gson.JsonObject;

public class ScheduleList extends Requests {
    public JsonObject request(String authKey, int route_id){
        setUrl(Constants.URL + Constants.ScheduleList);
        setUseAuthKey(true);
        setUseBodyParams(true);
        setAuthorizationKey(authKey);
        setBodyParameters("{\"route_id\":\"" + route_id + "\"}");
        GET_request();
        return response;
    }
}
