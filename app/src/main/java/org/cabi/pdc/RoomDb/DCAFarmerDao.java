package org.cabi.pdc.RoomDb;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DCAFarmerDao {

    @Insert
    public void addFarmerDetails(RoomDB_FarmerDetails farmerDetails);

    @Query("select * from DCAFarmerDetails")
    public List<RoomDB_FarmerDetails> getAllFarmerDetails();

    @Delete
    public void deleteFarmerDetails(RoomDB_FarmerDetails farmerDetails);

    @Update
    public void updateFarmerDetails(RoomDB_FarmerDetails farmerDetails);
}