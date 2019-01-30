package org.cabi.pdc.RoomDb;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "DCALocation2")
public class RoomDB_Location2 {

    @PrimaryKey
    @ColumnInfo(name = "Location2_Id")
    private int location2Id;

    public int getLocation2Id() {
        return location2Id;
    }

    public void setLocation2Id(int location2Id) {
        this.location2Id = location2Id;
    }

    @ColumnInfo(name = "Location2_Name")
    private String location2Name;


    public String getLocation2Name() {
        return location2Name;
    }

    public void setLocation2Name(String location2Name) {
        this.location2Name = location2Name;
    }

    @ColumnInfo(name = "Location1_Name")
    private String location1Name;

    public String getLocation1Name() {
        return location1Name;
    }

    public void setLocation1Name(String location1Name) {
        this.location1Name = location1Name;
    }
}