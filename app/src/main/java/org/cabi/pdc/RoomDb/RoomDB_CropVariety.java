package org.cabi.pdc.RoomDb;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "DCACropVarieties")
public class RoomDB_CropVariety {

    @PrimaryKey
    @ColumnInfo(name = "Crop_Variety_Id")
    private int cropVarietyId;

    public int getCropVarietyId() {
        return cropVarietyId;
    }

    public void setCropVarietyId(int cropVarietyId) {
        this.cropVarietyId = cropVarietyId;
    }

    @ColumnInfo(name = "Crop_Variety_Name")
    private String cropVarietyName;

    public String getCropVarietyName() {
        return cropVarietyName;
    }

    public void setCropVarietyName(String cropVarietyName) {
        this.cropVarietyName = cropVarietyName;
    }

    @ColumnInfo(name = "Crop_Name")
    private String cropName;

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

}