package Requests.services;

import Requests.Requests;
import Requests.Constants;
import com.google.gson.JsonObject;

public class InformationAboutTheRouteOfTheCurrentSession extends Requests {
    public JsonObject request(String authKey){
        setUrl(Constants.URL + Constants.InformationAboutTheRouteOfTheCurrentSession);
        setUseAuthKey(true);
        setUseBodyParams(false);
        setAuthorizationKey(authKey);
        GET_request();
        return response;
    }
}
