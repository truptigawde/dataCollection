package org.cabi.pdc.RoomDb;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DCACropDao {

    @Insert
    public void addCrop(RoomDB_Crop crop);

    @Query("select * from DCACrops")
    public List<RoomDB_Crop> getAllCrops();

    @Delete
    public void deleteCrop(RoomDB_Crop crop);

    @Update
    public void updateCrop(RoomDB_Crop crop);
}