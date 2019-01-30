package org.cabi.pdc.RoomDb;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "DCAClinicCodes")
public class RoomDB_ClinicCode {

    @PrimaryKey
    @ColumnInfo(name = "Clinic_Code_Id")
    private int clinicCodeId;

    public int getClinicCodeId() {
        return clinicCodeId;
    }

    public void setClinicCodeId(int clinicCodeId) {
        this.clinicCodeId = clinicCodeId;
    }

    @ColumnInfo(name = "Clinic_Code")
    private String clinicCode;

    public String getClinicCode() {
        return clinicCode;
    }

    public void setClinicCode(String clinicCode) {
        this.clinicCode = clinicCode;
    }
}