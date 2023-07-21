package Requests.services;

import Requests.Requests;
import Requests.Constants;
import com.google.gson.JsonObject;

public class ListOfRoutesForTheSelectedCar extends Requests {
    public JsonObject request(String authKey, String car_id){
        setUrl(Constants.ListOfRoutesForTheSelectedCar + car_id + "/routes");
        setUseAuthKey(true);
        setUseBodyParams(false);
        setAuthorizationKey(authKey);
        GET_request();
        return response;
    }
}
