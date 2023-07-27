package Requests.services;

import Requests.Requests;
import Requests.Constants;
import com.google.gson.JsonObject;

public class ContiniumSessionTypeA extends Requests {

    public JsonObject request(String authKey, String car_id, String route_id, String terminus_id) {
        setUrl(Constants.URL + Constants.ContiniumSessionTypeA);
        setUseAuthKey(true);
        setUseBodyParams(true);
        setAuthorizationKey(authKey);
        setBodyParameters("{\"car_id\":\"" + car_id + "\",\"route_id\":\"" + route_id + "\",\"terminus_id\":\"" + terminus_id + "\"}");
        POST_request();
        return response;
    }

}
