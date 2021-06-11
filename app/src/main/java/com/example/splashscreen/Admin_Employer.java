package com.example.splashscreen;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Admin_Employer implements Serializable {

    //@Exclude private String id;


    private String fname;
    private String UserID;
    private String isEmployer;
    private String email;
    private String phone;
    private String pass;

    public Admin_Employer() {
        //empty constructor needed
    }

    /*public Admin_Employer( String fname, String userID, String isUser, String email, String phone, String pass) {
        this.fname = fname;
        this.UserID = userID;
        this.isEmployer = isEmployer;
        this.email = email;
        this.phone = phone;
        this.pass = pass;
    }*/

    public Admin_Employer(String fname, String email, String pass, String phone, String userID) {
        this.fname = fname;
        this.UserID = userID;
        this.email = email;
        this.phone = phone;
        this.pass = pass;
    }

    public Admin_Employer(String fname, String email, String phone, String userID) {
        this.fname = fname;
        this.UserID = userID;
        this.email = email;
        this.phone = phone;
        //this.pass = pass;
    }

    /*public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }*/

    public String getFname() {
        return fname;
    }

    public String getUserID() {
        return UserID;
    }

    public String getIsEmployer() {
        return isEmployer;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPass() { return pass; }


}

