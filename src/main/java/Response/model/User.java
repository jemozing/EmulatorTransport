package Response.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class User extends Response.model.Data {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("phone")
    private String phone;
    @SerializedName("pin_code")
    private String pinCode;
    @SerializedName("published")
    private int published;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
}

