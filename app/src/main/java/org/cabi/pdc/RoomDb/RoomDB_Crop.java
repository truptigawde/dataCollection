package org.cabi.pdc.RoomDb;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "DCACrops")
public class RoomDB_Crop {

    @PrimaryKey
    @ColumnInfo(name = "Crop_Id")
    private int cropId;

    public int getCropId() {
        return cropId;
    }

    public void setCropId(int cropId) {
        this.cropId = cropId;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    @ColumnInfo(name = "Crop_Name")
    private String cropName;
}
