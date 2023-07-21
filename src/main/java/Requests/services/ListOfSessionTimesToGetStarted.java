package Requests.services;

import Requests.Requests;
import Requests.Constants;
import com.google.gson.JsonObject;

public class ListOfSessionTimesToGetStarted extends Requests {
    public JsonObject request(String authKey, String car_id, String route_id, String terminus_id){
        setUrl(Constants.ListOfSessionTimesToGetStarted +
                "?route_id=" + route_id +
                "&car_id=" + car_id +
                "&terminus_id=" + terminus_id);
        setUseAuthKey(true);
        setUseBodyParams(false);
        setAuthorizationKey(authKey);
        GET_request();
        return response;
    }
}
