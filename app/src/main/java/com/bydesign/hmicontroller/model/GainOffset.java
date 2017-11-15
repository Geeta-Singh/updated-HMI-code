package com.bydesign.hmicontroller.model;

/**
 * Created by Geeta on 1/18/2016.
 */
public class GainOffset {
    public Double getGain() {
        return gain;
    }

    public void setGain(Double gain) {
        this.gain = gain;
    }

    public Double getOffset() {
        return offset;
    }

    public void setOffset(Double offset) {
        this.offset = offset;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    String param;
    Double gain;
    Double offset;
}
