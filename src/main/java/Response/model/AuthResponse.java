package Response.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class AuthResponse extends Response.model.Data {
    @SerializedName("user")
    private User user;
    @SerializedName("token")
    private String token;
}
