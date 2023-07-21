package Requests.services;

import Requests.Requests;
import Requests.Constants;
import com.google.gson.JsonObject;

public class StopSession extends Requests {
    public JsonObject request(String authKey, String reason_optional){
        setUrl(Constants.StopSession);
        setUseAuthKey(true);
        setUseBodyParams(true);
        setAuthorizationKey(authKey);
        setBodyParameters("{\"reason\":\""+reason_optional+"\"}");
        POST_request();
        return response;
    }
}
