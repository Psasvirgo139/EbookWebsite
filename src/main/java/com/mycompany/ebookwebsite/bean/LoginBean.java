package com.mycompany.ebookwebsite.bean;

import java.io.Serializable;

public class LoginBean implements Serializable {

    private String usernameOrEmail;
    private String password;
    private String error;   // thông báo lỗi nếu login fail

    // constructor rỗng – yêu cầu JavaBean
    public LoginBean() {
    }


    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
