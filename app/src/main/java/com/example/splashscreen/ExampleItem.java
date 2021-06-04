package com.example.splashscreen;

import java.io.Serializable;

public class ExampleItem implements Serializable {

    private String Employee_Pic;
    private String fname;
    private String userID;
    private String email;
    private String phone;

    public ExampleItem() {
        //empty constructor needed
    }

    public ExampleItem(String employee_Pic, String fname, String userID, String email, String phone) {
        this.Employee_Pic = employee_Pic;
        this.fname = fname;
        this.userID = userID;
        this.email = email;
        this.phone = phone;
    }

    public String getEmployee_Pic() {
        return Employee_Pic;
    }

    public String getFname() {
        return fname;
    }

    public String getUserID() {
        return userID;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}