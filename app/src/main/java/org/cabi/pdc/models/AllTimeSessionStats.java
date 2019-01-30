package org.cabi.pdc.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AllTimeSessionStats implements Serializable {

    @SerializedName("MonthYear")
    private String monthYear;
    @SerializedName("Sessions")
    private int sessions;
    @SerializedName("Queries")
    private int queries;
    @SerializedName("Men")
    private int men;
    @SerializedName("Women")
    private int women;

    // Getter Methods

    public String getMonthYear() {
        return monthYear;
    }

    public int getSessions() {
        return sessions;
    }

    public int getQueries() {
        return queries;
    }

    public int getMen() {
        return men;
    }

    public int getWomen() {
        return women;
    }

    // Setter Methods

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public void setSessions(int sessions) {
        this.sessions = sessions;
    }

    public void setQueries(int queries) {
        this.queries = queries;
    }

    public void setMen(int men) {
        this.men = men;
    }

    public void setWomen(int women) {
        this.women = women;
    }
}