package Response.model;

import lombok.Data;

@Data
public class Error extends Response.model.Data {
    String message;
}
