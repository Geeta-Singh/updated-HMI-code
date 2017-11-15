package com.bydesign.hmicontroller.model;

import com.bydesign.hmicontroller.Service.DatabaseHandler;

/**
 * Created by user on 2/23/2016.
 */
public class Actual_corrected_model {
    String paramName;
    Double A_mg;
    Double A_ppm;
    Double C_mg;
    Double C_ppm;

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public Double getA_mg() {
        return A_mg;
    }

    public void setA_mg(Double a_mg) {
        A_mg = a_mg;
    }

    public Double getA_ppm() {
        return A_ppm;
    }

    public void setA_ppm(Double a_ppm) {
        A_ppm = a_ppm;
    }

    public Double getC_mg() {
        return C_mg;
    }

    public void setC_mg(Double c_mg) {
        C_mg = c_mg;
    }

    public Double getC_ppm() {
        return C_ppm;
    }

    public void setC_ppm(Double c_ppm) {
        C_ppm = c_ppm;
    }
}
