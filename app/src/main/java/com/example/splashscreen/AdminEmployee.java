package com.example.splashscreen;

import java.io.Serializable;

public class AdminEmployee implements Serializable {

    private String fname;
    private String email;
    private String pass;
    private String phone;
    private String UserID;

    public AdminEmployee() {
        //NULL CONSTRUCTOR
    }

    public AdminEmployee(String fname, String email, String pass, String phone, String userID) {
        this.fname = fname;
        this.email = email;
        this.pass = pass;
        this.phone = phone;
        UserID = userID;
    }

    public String getFname() {
        return fname;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }

    public String getPhone() {
        return phone;
    }

    public String getUserID() {
        return UserID;
    }
}
