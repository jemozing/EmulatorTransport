package Requests.services;

import Requests.Requests;
import Requests.Constants;
import Requests.model.Data;
import com.google.gson.JsonObject;

public class ListOfDriversCars extends Requests {
    public JsonObject request(Data data){
        setUrl(Constants.ListOfDriversCars);
        setUseAuthKey(true);
        setUseBodyParams(false);
        setAuthorizationKey(data.getData());
        GET_request();
        return response;
    }
}
