package org.cabi.pdc.RoomDb;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DCACropVarietyDao {

    @Insert
    public void addCropVariety(RoomDB_CropVariety cropVariety);

    @Query("select * from DCACropVarieties")
    public List<RoomDB_CropVariety> getAllCropVarities();

    @Delete
    public void deleteCropVariety(RoomDB_CropVariety cropVariety);

    @Update
    public void updateCropVariety(RoomDB_CropVariety cropVariety);
}