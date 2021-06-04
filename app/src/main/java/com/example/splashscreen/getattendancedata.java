package com.example.splashscreen;

public class getattendancedata {
    private int hours;
    private String checkin_date_;

    public getattendancedata() {
    }

    public getattendancedata(int hours, String checkin_date_) {
        this.hours = hours;
        this.checkin_date_ = checkin_date_;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getCheckin_date_() {
        return checkin_date_;
    }

    public void setCheckin_date_(String checkin_date_) {
        this.checkin_date_ = checkin_date_;
    }
}
