package com.example.splashscreen;

public class getexportdata {
    private String checkin_date_;
    private String checkin_time_1;
    private String checkin_location_1;
    private String checkout_time_2;
    private String checkout_location_2;
    private String checkin_Adress1;
    private String checkout_Adress_2;
    private int hours;

    public getexportdata(){
    }

    public getexportdata(String checkin_date_, String checkin_time_1, String checkin_location_1, String checkin_Adress1, String checkout_time_2, String checkout_location_2, String checkout_Adress_2,int hours) {
        this.checkin_date_ = checkin_date_;
        this.checkin_time_1 = checkin_time_1;
        this.checkin_location_1 = checkin_location_1;
        this.checkin_Adress1 = checkin_Adress1;
        this.checkout_time_2 = checkout_time_2;
        this.checkout_location_2 = checkout_location_2;
        this.checkout_Adress_2 = checkout_Adress_2;
        this.hours = hours;
    }
    public String getCheckin_date_() {
        return checkin_date_;
    }

    public String getCheckin_time_1() {
        return checkin_time_1;
    }

    public String getCheckin_location_1() {
        return checkin_location_1;
    }

    public String getCheckin_Adress1() {
        return checkin_Adress1;
    }

    public String getCheckout_time_2() {
        return checkout_time_2;
    }

    public String getCheckout_location_2() {
        return checkout_location_2;
    }

    public String getCheckout_Adress_2() {
        return checkout_Adress_2;
    }

    public int getHours() {
        return hours;
    }


}
