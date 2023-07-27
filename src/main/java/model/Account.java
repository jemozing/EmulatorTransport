package model;

import lombok.Data;

@Data
public class Account {
    private String phone;
    private String pin_code;

    public Account(String phone, String pin_code) {
        this.phone = phone;
        this.pin_code = pin_code;
    }
}