package com.bydesign.hmicontroller.model;

import java.text.SimpleDateFormat;

/**
 * Created by PARIKSH on 1/18/2016.
 */
public class Logs_Model {

    private String event;
    private String operation_string;
    private String username;


    //Constructor

    public Logs_Model()
    {
        //empty constructor....
    }

    //TimeStamp Generator
    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTimeStamp; // Find todays date
            currentTimeStamp = dateFormat.format(new java.util.Date());

            return currentTimeStamp;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    //Getters And Setters


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOperation_string() {
        return operation_string;
    }

    public void setOperation_string(String operation_string) {
        this.operation_string = operation_string;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
