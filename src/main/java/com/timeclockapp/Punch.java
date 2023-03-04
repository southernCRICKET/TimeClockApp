package com.timeclockapp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Punch {
    static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm");
    static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("MM/dd/yyyy");



    LocalDateTime punch;
    String type;
    String time;
    String date;
    int setIdentifier;


    public Punch(){
        this.punch=null;
        type="type";
        time="time";
        date="date";
    }

    public Punch(String type, String time, String date, int setIdentifier){
        String[] dateTokens = date.split("/");
        String[] timeTokens = time.split(":");
        this.punch= LocalDateTime.of(Integer.valueOf(dateTokens[2]), Integer.valueOf(dateTokens[0]),
                Integer.valueOf(dateTokens[1]), Integer.valueOf(timeTokens[0]), Integer.valueOf(timeTokens[1]));
        this.time=time;
        this.date=date;
        this.type=type;
        this.setIdentifier=setIdentifier;
    }

    public void performPunch(String button, LocalDateTime t, int id){
        this.punch = t;
        String type="";
        switch (button) {
            case "clockInButton" -> type = "In";
            case "clockOutButton" -> type = "Out";
            case "toLunchButton" -> type = "Going to Lunch";
            case "fromLunchButton" -> type = "Back From Lunch";
        }
        this.type=type;
        this.time=t.format(TIME);
        this.date=t.format(DATE);
        this.setIdentifier=id;
    }

    public String getType() {
        return type;
    }
    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }

}
