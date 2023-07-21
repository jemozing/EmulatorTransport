package Requests.services;

import Requests.Requests;
import Requests.Constants;
import com.google.gson.JsonObject;

public class InformationAboutTheCurrentSession extends Requests {
    public JsonObject request(String authKey){
        setUrl(Constants.InformationAboutTheCurrentSession);
        setUseAuthKey(true);
        setUseBodyParams(false);
        setAuthorizationKey(authKey);
        GET_request();
        return response;
    }
}
