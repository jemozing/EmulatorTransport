package Requests.services;

import Requests.Requests;
import Requests.Constants;
import com.google.gson.JsonObject;

public class StartSessionTypeA extends Requests {
    public JsonObject request(String authKey, String car_id, String route_id, String terminus_id, String time){
        setUrl(Constants.StartSessionTypeA);
        setUseAuthKey(true);
        setUseBodyParams(true);
        setAuthorizationKey(authKey);
        setBodyParameters("{\"car_id\":\""+car_id+"\",\"route_id\":\""+route_id+"\",\"terminus_id\":\""+terminus_id+"\",\"time\":\""+time+"\"}");
        POST_request();
        return response;
    }
}
