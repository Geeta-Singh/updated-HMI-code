package com.bydesign.hmicontroller.model;

/**
 * Created by user on 2/16/2016.
 */
public class ParameterForGaugeView {
    Double alarm1;
    Double alarm2;
    String rang;
    String param;
    Double value;
    String unit;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getAlarm1() {
        return alarm1;
    }

    public void setAlarm1(Double alarm1) {
        this.alarm1 = alarm1;
    }

    public Double getAlarm2() {
        return alarm2;
    }

    public void setAlarm2(Double alarm2) {
        this.alarm2 = alarm2;
    }

    public String getRang() {
        return rang;
    }

    public void setRang(String rang) {
        this.rang = rang;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
