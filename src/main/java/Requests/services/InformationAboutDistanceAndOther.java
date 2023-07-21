package Requests.services;

import Requests.Requests;
import Requests.Constants;
import com.google.gson.JsonObject;

public class InformationAboutDistanceAndOther extends Requests {
    public JsonObject request(String authKey){
        setUrl(Constants.InformationAboutDistanceAndOther);
        setUseAuthKey(true);
        setUseBodyParams(false);
        setAuthorizationKey(authKey);
        GET_request();
        return response;
    }
}
