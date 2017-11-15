package com.bydesign.hmicontroller.model;

/**
 * Created by Geeta on 1/18/2016.
 */
public class CurrentDataModel {
    String Date;
    String Time;
    String Status;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getParamName() {
        return ParamName;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public void setParamName(String paramName) {
        ParamName = paramName;

    }

    public Double getO2() {
        return O2;
    }

    public void setO2(Double o2) {
        O2 = o2;
    }

    public Double getCo2() {
        return Co2;
    }

    public void setCo2(Double co2) {
        Co2 = co2;
    }

    public String getStackTemp() {
        return StackTemp;
    }

    public void setStackTemp(String stackTemp) {
        StackTemp = stackTemp;
    }

    public String getAmbientTemp() {
        return AmbientTemp;
    }

    public void setAmbientTemp(String ambientTemp) {
        AmbientTemp = ambientTemp;
    }

    Double O2,Co2;
    String StackTemp,AmbientTemp;
    public Double[] getVal() {
        return val;
    }

    public void setVal(Double[] val) {
        this.val = val;
    }

    Double val[];
    String ParamName;
    String QCODE;
    String DmMessage;
    Double value;

    public String getTime() {
        return Time;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    String unit;
   /* public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }
*/
  /*  public java.sql.Time getTime() {
        return Time;
    }

    public void setTime(java.sql.Time time) {
        Time = time;
    }
*/


    public String getQCODE() {
        return QCODE;
    }

    public void setQCODE(String QCODE) {
        this.QCODE = QCODE;
    }

    public String getDmMessage() {
        return DmMessage;
    }

    public void setDmMessage(String dmMessage) {
        DmMessage = dmMessage;
    }
}
