package org.cabi.pdc.RoomDb;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "DCAPlantDoctors")
public class RoomDB_PlantDoctor {

    @PrimaryKey
    @ColumnInfo(name = "Plant_Doctor_Id")
    private int plantDoctorId;

    public int getPlantDoctorId() {
        return plantDoctorId;
    }

    public void setPlantDoctorId(int plantDoctorId) {
        this.plantDoctorId = plantDoctorId;
    }

    @ColumnInfo(name = "Plant_Doctor_Name")
    private String plantDoctorName;

    public String getPlantDoctorName() {
        return plantDoctorName;
    }

    public void setPlantDoctorName(String plantDoctorName) {
        this.plantDoctorName = plantDoctorName;
    }
}