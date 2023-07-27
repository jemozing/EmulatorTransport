package Requests.services;

import Requests.Requests;
import Requests.Constants;
import Requests.model.RequestsData;
import com.google.gson.JsonObject;

public class ListOfDriversCars extends Requests {
    public JsonObject request(String token){
        setUrl(Constants.URL + Constants.ListOfDriversCars);
        setUseAuthKey(true);
        setUseBodyParams(false);
        setAuthorizationKey(token);
        GET_request();
        return response;
    }
}
