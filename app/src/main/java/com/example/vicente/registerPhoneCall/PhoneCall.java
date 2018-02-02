package com.example.vicente.registerPhoneCall;

/**
 * Created by vicente on 27/01/18.
 */

public class PhoneCall {
    private String state;
    private String numberPhone;
    private String time;
    private double duration;

    public PhoneCall(int state, String numberPhone, String time, double duration) {
        setState(state);
        this.numberPhone = numberPhone;
        this.time = time;
        this.duration = duration;
    }

    public PhoneCall(int state, String numberPhone, String time) {
        setState(state);
        this.numberPhone = numberPhone;
        this.time = time;
        this.duration = -1;
    }

    public String getState() {
        return state;
    }

    public int getStateInt(){
        int ste = -1;
        switch (state) {
            case "IDLE":
                ste = 0;
                break;
            case "RINGING":
                ste = 1;
                break;
            case "OFFHOOK":
                ste = 2;
                break;
        }
        return ste;
    }

    public void setState(int state) {
        this.state = null;
        if (state == 0) {
            this.state = "IDLE";
        } else if (state == 1) {
            this.state = "RINGING";
        } else if (state == 2) {
            this.state = "OFFHOOK";
        }
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "PhoneCall{" +
                "state='" + state + '\'' +
                ", numberPhone='" + numberPhone + '\'' +
                ", time='" + time + '\'' +
                ", duration=" + duration +
                '}';
    }
}
