package Requests.services;

import Requests.Requests;
import Requests.Constants;
import com.google.gson.JsonObject;

public class SendingLocation extends Requests {
    public JsonObject request(String authKey, String latiude, String longtiude){
        setUrl(Constants.URL + Constants.SendingLocation);
        setUseAuthKey(true);
        setUseBodyParams(true);
        setAuthorizationKey(authKey);
        setBodyParameters("{\"lat\":\""+latiude+"\",\"lon\":\""+longtiude+"\"}");
        POST_request();
        return response;
    }
}
