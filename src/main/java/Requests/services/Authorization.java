package Requests.services;

import Requests.Requests;
import Requests.Constants;
import com.google.gson.JsonObject;

public class Authorization extends Requests {
    public JsonObject request(String phone, String pin_code){
        setUrl(Constants.URL + Constants.Authorization);
        setBodyParameters("{\"phone\":\""+ phone +"\",\"pin_code\":\""+ pin_code  +"\"}");
        setUseAuthKey(false);
        setUseBodyParams(true);
        POST_request();
        return response;
    }
}
