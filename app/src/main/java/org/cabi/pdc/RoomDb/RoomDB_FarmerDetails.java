package org.cabi.pdc.RoomDb;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "DCAFarmerDetails")
public class RoomDB_FarmerDetails {

    @PrimaryKey
    @ColumnInfo(name = "Farmer_Id")
    private int farmerId;

    @ColumnInfo(name = "Farmer_Name")
    private String farmerName;

    @ColumnInfo(name = "Farmer_Phone_Number")
    private String farmerPhoneNumber;

    @ColumnInfo(name = "Farmer_Gender")
    private String farmerGender;

    @ColumnInfo(name = "Farmer_Location1")
    private String farmerLocation1;

    @ColumnInfo(name = "Farmer_Location2")
    private String farmerLocation2;

    public int getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(int farmerId) {
        this.farmerId = farmerId;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getFarmerPhoneNumber() {
        return farmerPhoneNumber;
    }

    public void setFarmerPhoneNumber(String farmerPhoneNumber) {
        this.farmerPhoneNumber = farmerPhoneNumber;
    }

    public String getFarmerGender() {
        return farmerGender;
    }

    public void setFarmerGender(String farmerGender) {
        this.farmerGender = farmerGender;
    }

    public String getFarmerLocation1() {
        return farmerLocation1;
    }

    public void setFarmerLocation1(String farmerLocation1) {
        this.farmerLocation1 = farmerLocation1;
    }

    public String getFarmerLocation2() {
        return farmerLocation2;
    }

    public void setFarmerLocation2(String farmerLocation2) {
        this.farmerLocation2 = farmerLocation2;
    }

    public String getFarmerLocation3() {
        return farmerLocation3;
    }

    public void setFarmerLocation3(String farmerLocation3) {
        this.farmerLocation3 = farmerLocation3;
    }

    @ColumnInfo(name = "Farmer_Location3")
    private String farmerLocation3;
}