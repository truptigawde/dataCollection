package org.cabi.pdc.RoomDb;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DCALocation1Dao {

    @Insert
    public void addLocation1(RoomDB_Location1 location1);

    @Query("select * from DCALocation1")
    public List<RoomDB_Location1> getAllLocation1();

    @Delete
    public void deleteLocation1(RoomDB_Location1 location1);

    @Update
    public void updateLocation1(RoomDB_Location1 location1);
}