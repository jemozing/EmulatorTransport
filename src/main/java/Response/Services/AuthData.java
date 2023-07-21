package Response.Services;

import Response.model.AuthResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthData {

    public AuthResponse parseJsonData(JsonObject authData){
        Gson gson = new Gson();
        AuthResponse auth;
        auth = gson.fromJson(authData.get("result"), AuthResponse.class);
        String[] s = auth.getToken().split("\\|");
        auth.setToken(s[1]);
        return auth;
    }
}
