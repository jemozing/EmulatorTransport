package Requests.model;

import lombok.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
@Data
public class Account implements Requests.model.Data {
    private String login;
    private String password;

    public Account(String login, String password) {
        this.login = login;
        this.password = password;
    }
    @Override
    public String getData(){
        return "{\"phone\":\""+ login +"\",\"pin_code\":\""+ password  +"\"}";
    }
}