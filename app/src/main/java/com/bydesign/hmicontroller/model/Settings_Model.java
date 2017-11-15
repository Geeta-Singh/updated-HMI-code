package com.bydesign.hmicontroller.model;

/**
 * Created by PARIKSHIT on 1/18/2016.
 */
public class Settings_Model {

    private String ParamName;
    private String Unit;
    private String Range;
    private String Resolution;
    private String Mode;
    private double Alarm1;
    private double Alarm2;
    private double Gain;
    private double Offset;


    //Constructor

    public Settings_Model() {
        //Default Constructor
    }

    public Settings_Model(String ParamName, String Range, double Alarm1, double Alarm2, double Gain, double Offset, String Resolution,String Unit, String Mode) {
        this.ParamName = ParamName;
        this.Range = Range;
        this.Unit = Unit;
        this.Resolution = Resolution;
        this.Alarm1 = Alarm1;
        this.Alarm2 = Alarm2;
        this.Gain = Gain;
        this.Offset = Offset;
        this.Mode = Mode;
    }
    //Getters and Setters....


    public String getMode() {
        return Mode;
    }

    public void setMode(String mode) {
        Mode = mode;
    }

    public String getParamName() {
        return ParamName;
    }

    public void setParamName(String paramName) {
        ParamName = paramName;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getRange() {
        return Range;
    }

    public void setRange(String range) {
        Range = range;
    }

    public String getResolution() {
        return Resolution;
    }

    public void setResolution(String resolution) {
        Resolution = resolution;
    }

    public double getAlarm1() {
        return Alarm1;
    }

    public void setAlarm1(double alarm1) {
        Alarm1 = alarm1;
    }

    public double getAlarm2() {
        return Alarm2;
    }

    public void setAlarm2(double alarm2) {
        Alarm2 = alarm2;
    }

    public double getGain() {
        return Gain;
    }

    public void setGain(double gain) {
        Gain = gain;
    }

    public double getOffset() {
        return Offset;
    }

    public void setOffset(double offset) {
        Offset = offset;
    }

}
