package com.timeclockapp;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;


public class Day {
    static final int MINUTES_IN_HOUR=60;
    static final double FACTOR_FOR_TRUNCATING_DOUBLE=100.0;
    static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");

    //Punch inStamp, outStamp, lunchOut, lunchIn;
    Punch punch;
    double hoursWorked;
    int currentSet, clockInFlag, clockOutFlag, toLunchFlag, fromLunchFlag;
    DayOfWeek dayOfWeek;
    ObservableList<Punch> punches;


    public Day(DayOfWeek day){
        /*
        this.outStamp= new Punch(1);
        this.inStamp=new Punch(0);
        this.lunchIn=new Punch(1);
        this.lunchOut=new Punch(1);*/
        this.punch = new Punch();
        this.clockInFlag=0;
        this.clockOutFlag=1;
        this.toLunchFlag=1;
        this.fromLunchFlag=1;
        this.punches= FXCollections.observableArrayList();
        this.hoursWorked=0.0;
        this.dayOfWeek=day;
        this.currentSet=0;
    }

    public void performPunch(String buttonId){
        switch (buttonId) {
            case "clockInButton":
                if(this.clockInFlag==0) {
                    this.punch.performPunch(buttonId,LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), this.currentSet);
                    this.clockInFlag=1;
                    this.clockOutFlag=0;
                    this.toLunchFlag=0;
                    this.punches.add(this.punch);
                }
                else
                    System.out.println("Already clocked in");
                break;
            case "clockOutButton":
                if(this.clockOutFlag==0){
                    if(this.toLunchFlag==1 && this.fromLunchFlag==0) {
                        this.punch.performPunch("fromLunchButton", LocalDateTime.now().plusMinutes(30).truncatedTo(ChronoUnit.MINUTES), this.currentSet);
                        this.punches.add(this.punch);
                        this.punch=new Punch();
                    }
                    this.punch.performPunch(buttonId,LocalDateTime.now().plusHours(6).truncatedTo(ChronoUnit.MINUTES),this.currentSet);
                    resetFlags();
                    this.punches.add(this.punch);
                    this.currentSet++;
                    setHoursWorked();
                }
                else
                    System.out.println("Invalid/missing Punch");
                break;
            case "toLunchButton":
                if(toLunchFlag==0) {
                    this.punch.performPunch(buttonId,LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), this.currentSet);
                    this.punches.add(this.punch);
                    this.toLunchFlag=1;
                    this.fromLunchFlag=0;
                }
                else
                    System.out.println("Invalid/missing Punch");
                break;
            case "fromLunchButton":
                if(this.fromLunchFlag==0){
                    this.punch.performPunch(buttonId,LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), this.currentSet);
                    this.fromLunchFlag=1;
                    this.toLunchFlag=0;
                    this.punches.add(this.punch);
                }
                else
                    System.out.println("Invalid/missing Punch");
                break;
        }
        this.punch=new Punch();
    }

    public void setHoursWorked(){
        double origHours = this.hoursWorked;
        int i = ((Punch) punches.toArray()[0]).setIdentifier;
        ArrayList<Punch> set = new ArrayList<>();
        for(Punch p : punches)
        {
            System.out.println(String.valueOf(p.setIdentifier));
            if(i!=p.setIdentifier){
                calculateHoursForSet(set);
                set.clear();
                i=p.setIdentifier;
            }
            set.add(p);
        }
        calculateHoursForSet(set);
        this.hoursWorked-=origHours;
    }

    //need to go through set and depending on size of set do one or the other. ALso need to assign local variables
    public void calculateHoursForSet(ArrayList<Punch> set){
        boolean lunch = false;
        Punch in = set.get(0);
        Punch out, lIn = null,lOut = null;
        if(set.size()==4)
            lunch=true;
        if(!lunch)
            out = set.get(1);
        else {
            lOut = set.get(1);
            lIn = set.get(2);
            out = set.get(3);
        }
        double tH = Double.valueOf(out.punch.getHour()) - in.punch.getHour();
        double tM = Math.round((Double.valueOf(out.punch.getMinute()) - in.punch.getMinute())/ MINUTES_IN_HOUR*FACTOR_FOR_TRUNCATING_DOUBLE)/FACTOR_FOR_TRUNCATING_DOUBLE;
        if(tH<0) {
            tH+=12;
            tH*=-1;
        }
        this.hoursWorked += tH+tM;

        if(lunch)
            this.hoursWorked-= ((Double.valueOf(lIn.punch.getHour()) - lOut.punch.getHour()) +
                    (Math.round((Double.valueOf(lIn.punch.getMinute()) - lOut.punch.getMinute())/ MINUTES_IN_HOUR*FACTOR_FOR_TRUNCATING_DOUBLE)/FACTOR_FOR_TRUNCATING_DOUBLE));
           /* double tLH = Double.valueOf(lIn.punch.getHour()) - lOut.punch.getHour();
            double tLM = Math.round((Double.valueOf(lIn.punch.getMinute()) - lOut.punch.getMinute())/ MINUTES_IN_HOUR*FACTOR_FOR_TRUNCATING_DOUBLE)/FACTOR_FOR_TRUNCATING_DOUBLE;
            this.hoursWorked -= (tLH+tLM);*/
    }

    public void resetFlags(){
        this.clockInFlag=0;
        this.clockOutFlag=1;
        this.toLunchFlag=1;
        this.fromLunchFlag=1;
    }

    public void printPunches(){
        for(var p : punches) {
            System.out.println("Type: "+p.type);
            System.out.println("Date: "+p.date);
            System.out.println("Time: "+p.time);
        }
    }

    public void loadDay(String[] punchTokens){

    }

    /*
    public void addPunch(){
        Scanner sc = new Scanner(System.in);
        do {
            System.out.print("In, out, or Lunch: ");
            String punch = sc.nextLine();
                System.out.print("Enter date and time(MM-dd-yyyy HH:mm): ");
                String t = sc.nextLine();
                LocalDateTime time = LocalDateTime.parse(t, DTF);
                if (punch.toUpperCase().compareTo("IN") == 0) {
                    this.inStamp.punch = time;
                    return;
                } else if (punch.toUpperCase().compareTo("OUT") == 0) {
                    this.outStamp.punch = time;
                    setHoursWorked();
                    return;
                } else if (punch.toUpperCase().compareTo("LUNCH") == 0) {
                    if (this.lunchOut.punch == null) {
                        this.lunchOut.punch = time;
                    }
                    else {
                        this.lunchIn.punch = time;
                    }
                    return;
                } else
                    System.out.println("Invalid punch id");
        }while(true);
    }


    public String outputBuilder(){
        String output="";
        output+="Time Punches for " + dayOfWeek.toString() + ", " + inStamp.punch.getMonth().toString() + " the " + inStamp.punch.getDayOfMonth()+"\n";
        if(inStamp.punch!=null)
            output+= "Clock-in: "+inStamp.punch.format(DTF)+"\n";
        if(lunchOut.punch!=null)
            output+= "Out to Lunch: "+lunchOut.punch.format(DTF)+"\n";
        if(lunchIn.punch!=null)
            output+= "Back from Lunch: "+lunchIn.punch.format(DTF)+"\n";
        if(outStamp.punch!=null)
            output+= "Clock-out: " + outStamp.punch.format(DTF) + "\n";
        output+="Hours Worked: " + String.valueOf(hoursWorked)+"\n\n";
        return output;
    }*/
}
