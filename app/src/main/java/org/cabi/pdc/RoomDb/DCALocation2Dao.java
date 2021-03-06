package org.cabi.pdc.RoomDb;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DCALocation2Dao {
    @Insert
    public void addLocation2(RoomDB_Location2 location2);

    @Query("select * from DCALocation2")
    public List<RoomDB_Location2> getAllLocation2();

    @Delete
    public void deleteLocation2(RoomDB_Location2 location2);

    @Update
    public void updateLocation2(RoomDB_Location2 location2);
}