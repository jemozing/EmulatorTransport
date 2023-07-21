package Requests.model;

@lombok.Data
public class Authorization implements Data{
    String token;
    public Authorization(String token){
        this.token = token;
    }
    @Override
    public String getData() {
        return token;
    }
}
