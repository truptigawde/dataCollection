package org.cabi.pdc.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class MyMonthlyReport implements Serializable {

    @SerializedName("Reports")
    private ArrayList<Object> reports = new ArrayList<Object>();
    @SerializedName("AllTimeSessionStatsObject")
    private AllTimeSessionStats allTimeSessionStatsObject;
    @SerializedName("AllTimeClinicStatsObject")
    private AllTimeClinicStats allTimeClinicStatsObject;

    // Getter Methods

    public AllTimeSessionStats getAllTimeSessionStats() {
        return allTimeSessionStatsObject;
    }

    public AllTimeClinicStats getAllTimeClinicStats() {
        return allTimeClinicStatsObject;
    }

    public ArrayList<Object> getReports() {
        return reports;
    }

    // Setter Methods

    public void setAllTimeSessionStats(AllTimeSessionStats allTimeSessionStatsObject) {
        this.allTimeSessionStatsObject = allTimeSessionStatsObject;
    }

    public void setAllTimeClinicStats(AllTimeClinicStats allTimeClinicStatsObject) {
        this.allTimeClinicStatsObject = allTimeClinicStatsObject;
    }

    public void setReports(ArrayList<Object> reports) {
        this.reports = reports;
    }
}