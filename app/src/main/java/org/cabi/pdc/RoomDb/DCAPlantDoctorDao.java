package org.cabi.pdc.RoomDb;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DCAPlantDoctorDao {

    @Insert
    public void addPlantDoctor(RoomDB_PlantDoctor plantDoctor);

    @Query("select * from DCAPlantDoctors")
    public List<RoomDB_PlantDoctor> getAllPlantDoctors();

    @Delete
    public void deletePlantDoctor(RoomDB_PlantDoctor plantDoctor);

    @Update
    public void updatePlantDoctor(RoomDB_PlantDoctor plantDoctor);
}