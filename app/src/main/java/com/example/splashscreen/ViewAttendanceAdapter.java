package com.example.splashscreen;


public class ViewAttendanceAdapter  {
    private String checkin_date_;
    private String checkin_time_1;
    private String checkout_time_2;
    private Double minutes;

    public ViewAttendanceAdapter(){

    }

    public ViewAttendanceAdapter(String checkin_date_,String checkin_time_1,String checkout_time_2,Double minutes){
        this.checkin_date_=checkin_date_;
        this.checkin_time_1 = checkin_time_1;
        this.checkout_time_2=checkout_time_2;
        this.minutes=minutes;
    }

    public String getCheckin_date_() {
        return checkin_date_;
    }

    public String getCheckin_time_1() {
        return checkin_time_1;
    }

    public String getCheckout_time_2() {
        return checkout_time_2;
    }

    public Double getMinutes() {
        return minutes;
    }
}
