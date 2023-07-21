package Requests.services;

import Requests.Requests;
import Requests.Constants;
import com.google.gson.JsonObject;

public class CancelDeparture extends Requests {
    public JsonObject request(String authKey){
        setUrl(Constants.CancelDeparture);
        setUseAuthKey(true);
        setUseBodyParams(true);
        setAuthorizationKey(authKey);
        setBodyParameters("{}");
        POST_request();
        return response;
    }
}
