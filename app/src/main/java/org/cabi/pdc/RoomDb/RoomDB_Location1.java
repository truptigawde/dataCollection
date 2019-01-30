package org.cabi.pdc.RoomDb;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "DCALocation1")
public class RoomDB_Location1 {

    @PrimaryKey
    @ColumnInfo(name = "Location1_Id")
    private int location1Id;

    public int getLocation1Id() {
        return location1Id;
    }

    public void setLocation1Id(int location1Id) {
        this.location1Id = location1Id;
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