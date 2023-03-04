package com.timeclockapp;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Week {
    final static DateTimeFormatter DATE = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    HashMap<String, Day> days;
    double weeklyHours;
    String currentWeek;
    DayOfWeek currentDay;

    public Week(){
        this.days=new HashMap<>();
        this.days.put("MONDAY", new Day(DayOfWeek.MONDAY));
        this.days.put("TUESDAY", new Day(DayOfWeek.TUESDAY));
        this.days.put("WEDNESDAY", new Day(DayOfWeek.WEDNESDAY));
        this.days.put("THURSDAY", new Day(DayOfWeek.THURSDAY));
        this.days.put("FRIDAY", new Day(DayOfWeek.FRIDAY));
        this.days.put("SATURDAY", new Day(DayOfWeek.SATURDAY));
        this.days.put("SUNDAY", new Day(DayOfWeek.SUNDAY));
        this.weeklyHours=0.0;
        this.currentDay=LocalDateTime.now().getDayOfWeek();
        this.currentWeek=findCurrentWeek(LocalDateTime.now());
    }

    public void performPunch(String buttonId){
        LocalDateTime cT = LocalDateTime.now();
        this.currentDay=cT.getDayOfWeek();
        this.currentWeek=findCurrentWeek(cT);
        days.get(currentDay.toString()).performPunch(buttonId);
        if(buttonId.compareTo("clockOutButton")==0)
            calculateWeeklyHours();
    }

    public void calculateWeeklyHours(){
        double total=0;
        for(Day d : this.days.values())
            total+=d.hoursWorked;
        this.weeklyHours=total;
    }
/*
    public void addPunch(String day){
        this.days.get(day.toUpperCase()).addPunch();
    }
*/
    public String findCurrentWeek(LocalDateTime cT){
        int value=0;
        int day;
        this.currentDay= cT.getDayOfWeek();
        switch (cT.getDayOfWeek()){
            case TUESDAY -> value=1;
            case WEDNESDAY -> value=2;
            case THURSDAY -> value=3;
            case FRIDAY -> value=4;
            case SATURDAY -> value=5;
            case SUNDAY -> value=6;
        }
        day = cT.minusDays(value).getDayOfMonth();
        if(cT.getDayOfMonth()-value<0)
            return "Week of " + cT.minusMonths(1).getMonth().toString() + " the " + String.valueOf(day)+dateSuffix(day);
        return "Week of " + cT.getMonth().toString() + " the " + String.valueOf(day)+dateSuffix(day);
    }

    private String dateSuffix(int day){
        ArrayList<Integer> st = new ArrayList<Integer>(Arrays.asList(1,21,31));
        ArrayList<Integer> rd = new ArrayList<Integer>(Arrays.asList(3,23));
        ArrayList<Integer> nd = new ArrayList<Integer>(Arrays.asList(2,22));
        ArrayList<Integer> th = new ArrayList<Integer>(Arrays.asList(4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,24,25,26,27,28,29,30));

        if(st.contains(day))
            return "st";
        else if (rd.contains(day))
            return "rd";
        else if(nd.contains(day))
            return "nd";
        else if(th.contains(day))
            return "th";
        return "";
    }

    public void loadWeek(ArrayList<String> tokens){
        String[] dayTokens;
        for(String s: tokens){
            s=s.strip();
            dayTokens=s.split(",");
            //days.get(LocalDateTime.parse(dayTokens[2], DATE).getDayOfWeek().toString());
            System.out.println(LocalDate.parse(dayTokens[2], DATE).getDayOfWeek().toString());
        }
    }
}

