package com.example.splashscreen;

public class getattendancedata {
    private int minutes;
    private String checkin_date_;

    public getattendancedata() {
    }

    public getattendancedata(int minutes, String checkin_date_) {
        this.minutes = minutes;
        this.checkin_date_ = checkin_date_;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getCheckin_date_() {
        return checkin_date_;
    }

    public void setCheckin_date_(String checkin_date_) {
        this.checkin_date_ = checkin_date_;
    }
}
