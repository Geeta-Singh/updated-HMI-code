package com.bydesign.hmicontroller.model;

import java.sql.Timestamp;

/**
 * Created by Geeta on 1/18/2016.
 */
public class SensorData {
    Double Values;
    Timestamp Ts;
    String  Status;
    String DiagnosticMsg;
    String Qcode;
    String Param;
    Double val[];
    public Double[] getVal() {
        return val;
    }

    public String getParam() {
        return Param;
    }

    public void setParam(String param) {
        Param = param;
    }

    public void setVal(Double[] val) {
        this.val = val;
    }



    public String getQcode() {
        return Qcode;
    }

    public void setQcode(String qcode) {
        Qcode = qcode;
    }

    public Double getValues() {
        return Values;
    }

    public void setValues(Double values) {
        Values = values;
    }

    public Timestamp getTs() {
        return Ts;
    }

    public void setTs(Timestamp ts) {
        Ts = ts;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDiagnosticMsg() {
        return DiagnosticMsg;
    }

    public void setDiagnosticMsg(String diagnosticMsg) {
        DiagnosticMsg = diagnosticMsg;
    }


}
