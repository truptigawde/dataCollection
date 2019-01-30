package org.cabi.pdc.RoomDb;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "DCASessions")
public class RoomDB_Session {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "Session_Id")
    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @ColumnInfo(name = "session_Start_DateTime")
    private String sessionStartDateTime;

    public String getSessionStartDateTime() {
        return sessionStartDateTime;
    }

    public void setSessionStartDateTime(String sessionStartDateTime) {
        this.sessionStartDateTime = sessionStartDateTime;
    }

    @ColumnInfo(name = "session_End_DateTime")
    private String sessionEndDateTime;

    public String getSessionEndDateTime() {
        return sessionEndDateTime;
    }

    public void setSessionEndDateTime(String sessionEndDateTime) {
        this.sessionEndDateTime = sessionEndDateTime;
    }

    @ColumnInfo(name = "Clinic")
    private String clinic;

    public String getClinic() {
        return clinic;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }

    @ColumnInfo(name = "Draft")
    private int draft;

    public int getDraft() {
        return draft;
    }

    public void setDraft(int draft) {
        this.draft = draft;
    }

    @ColumnInfo(name = "Submitted")
    private int submitted;

    public int getSubmitted() {
        return submitted;
    }

    public void setSubmitted(int submitted) {
        this.submitted = submitted;
    }

    @ColumnInfo(name = "Men")
    private int men;

    public int getMen() {
        return men;
    }

    public void setMen(int men) {
        this.men = men;
    }

    @ColumnInfo(name = "Women")
    private int women;

    public int getWomen() {
        return women;
    }

    public void setWomen(int women) {
        this.women = women;
    }
}