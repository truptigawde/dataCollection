package org.cabi.pdc.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AllTimeClinicStats implements Serializable{

    @SerializedName("Min")
    private int min;
    @SerializedName("Max")
    private int max;
    @SerializedName("Average")
    private int average;

    // Getter Methods

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getAverage() {
        return average;
    }

    // Setter Methods

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setAverage(int average) {
        this.average = average;
    }
}