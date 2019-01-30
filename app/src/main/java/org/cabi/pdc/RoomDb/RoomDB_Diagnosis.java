package org.cabi.pdc.RoomDb;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "DCADiagnosis")
public class RoomDB_Diagnosis {

    @PrimaryKey
    @ColumnInfo(name = "Diagnosis_Id")
    private int diagnosisId;

    public int getDiagnosisId() {
        return diagnosisId;
    }

    public void setDiagnosisId(int diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    @ColumnInfo(name = "Diagnosis")
    private String diagnosis;

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
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