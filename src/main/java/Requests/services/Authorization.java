package Requests.services;

import Requests.Requests;
import Requests.Constants;
import com.google.gson.JsonObject;

public class Authorization extends Requests {
    public JsonObject request(String data){
        setUrl(Constants.Authorization);
        setBodyParameters(data);
        setUseAuthKey(false);
        setUseBodyParams(true);
        POST_request();
        return response;
    }
}
