package org.cabi.pdc.RoomDb;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "DCAForms")
public class RoomDb_Form {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "Form_Id")
    private String formId;

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    //--------------------
    @ColumnInfo(name = "Form_Start_DateTime")
    private String formStartDateTime;

    public String getFormStartDateTime() {
        return formStartDateTime;
    }

    public void setFormStartDateTime(String formStartDateTime) {
        this.formStartDateTime = formStartDateTime;
    }

    //--------------------
    @ColumnInfo(name = "Form_End_DateTime")
    private String formEndDateTime;

    public String getFormEndDateTime() {
        return formEndDateTime;
    }

    public void setFormEndDateTime(String formEndDateTime) {
        this.formEndDateTime = formEndDateTime;
    }

    //--------------------
    @ColumnInfo(name = "Latitude")
    private String latitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    //--------------------
    @ColumnInfo(name = "Longitude")
    private String longitude;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    //--------------------
    @ColumnInfo(name = "Status")
    private String FormStatus;

    public String getFormStatus() {
        return FormStatus;
    }

    public void setFormStatus(String formStatus) {
        FormStatus = formStatus;
    }

    //--------------------
    @ColumnInfo(name = "Form_Definiton_Id")
    private String formDefinitonId;

    public String getFormDefinitonId() {
        return formDefinitonId;
    }

    public void setFormDefinitonId(String formDefinitonId) {
        this.formDefinitonId = formDefinitonId;
    }

    //--------------------
    @ColumnInfo(name = "SessionId")
    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    //--------------------
    @ColumnInfo(name = "Plant_Doctor")
    private String plantDoctor;

    public String getPlantDoctor() {
        return plantDoctor;
    }

    public void setPlantDoctor(String plantDoctor) {
        this.plantDoctor = plantDoctor;
    }

    //--------------------
    @ColumnInfo(name = "Clinic_Code")
    private String clinicCode;

    public String getClinicCode() {
        return clinicCode;
    }

    public void setClinicCode(String clinicCode) {
        this.clinicCode = clinicCode;
    }

    //--------------------
    @ColumnInfo(name = "Farmer_Name")
    private String farmerName;

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    //--------------------
    @ColumnInfo(name = "Farmer_Phone_Number")
    private String farmerPhoneNumber;

    public String getFarmerPhoneNumber() {
        return farmerPhoneNumber;
    }

    public void setFarmerPhoneNumber(String farmerPhoneNumber) {
        this.farmerPhoneNumber = farmerPhoneNumber;
    }

    //--------------------
    @ColumnInfo(name = "Farmer_Gender")
    private String farmerGender;

    public String getFarmerGender() {
        return farmerGender;
    }

    public void setFarmerGender(String farmerGender) {
        this.farmerGender = farmerGender;
    }

    //--------------------
    @ColumnInfo(name = "Farmer_Location1")
    private String farmerLocation1;

    public String getFarmerLocation1() {
        return farmerLocation1;
    }

    public void setFarmerLocation1(String farmerLocation1) {
        this.farmerLocation1 = farmerLocation1;
    }

    //--------------------
    @ColumnInfo(name = "Farmer_Location2")
    private String farmerLocation2;

    public String getFarmerLocation2() {
        return farmerLocation2;
    }

    public void setFarmerLocation2(String farmerLocation2) {
        this.farmerLocation2 = farmerLocation2;
    }

    //--------------------
    @ColumnInfo(name = "Farmer_Location3")
    private String farmerLocation3;

    public String getFarmerLocation3() {
        return farmerLocation3;
    }

    public void setFarmerLocation3(String farmerLocation3) {
        this.farmerLocation3 = farmerLocation3;
    }

    //--------------------
    @ColumnInfo(name = "Crop_Name")
    private String cropName;

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    //--------------------
    @ColumnInfo(name = "Crop_Variety")
    private String cropVariety;

    public String getCropVariety() {
        return cropVariety;
    }

    public void setCropVariety(String cropVariety) {
        this.cropVariety = cropVariety;
    }

    //--------------------
    @ColumnInfo(name = "Crop_Sample_Brought")
    private String cropSampleBrought;

    public String getCropSampleBrought() {
        return cropSampleBrought;
    }

    public void setCropSampleBrought(String cropSampleBrought) {
        this.cropSampleBrought = cropSampleBrought;
    }

    //--------------------
    @ColumnInfo(name = "Crop_Photo_Sample")
    private String cropPhotoSample;

    public String getCropPhotoSample() {
        return cropPhotoSample;
    }

    public void setCropPhotoSample(String cropPhotoSample) {
        this.cropPhotoSample = cropPhotoSample;
    }

    //--------------------
    @ColumnInfo(name = "Development_Stage")
    private String developmentStage;

    public String getDevelopmentStage() {
        return developmentStage;
    }

    public void setDevelopmentStage(String developmentStage) {
        this.developmentStage = developmentStage;
    }

    //--------------------
    @ColumnInfo(name = "Parts_Affected")
    private String partsAffected;

    public String getPartsAffected() {
        return partsAffected;
    }

    public void setPartsAffected(String partsAffected) {
        this.partsAffected = partsAffected;
    }

    //--------------------
    @ColumnInfo(name = "Year_First_Seen")
    private String yearFirstSeen;

    public String getYearFirstSeen() {
        return yearFirstSeen;
    }

    public void setYearFirstSeen(String yearFirstSeen) {
        this.yearFirstSeen = yearFirstSeen;
    }

    //--------------------
    @ColumnInfo(name = "Area_Planted")
    private String areaPlanted;

    public String getAreaPlanted() {
        return areaPlanted;
    }

    public void setAreaPlanted(String areaPlanted) {
        this.areaPlanted = areaPlanted;
    }

    //--------------------
    @ColumnInfo(name = "Area_Planted_Unit")
    private String areaPlantedUnit;

    public String getAreaPlantedUnit() {
        return areaPlantedUnit;
    }

    public void setAreaPlantedUnit(String areaPlantedUnit) {
        this.areaPlantedUnit = areaPlantedUnit;
    }

    //--------------------
    @ColumnInfo(name = "Percent_Of_Crop_Affected")
    private String percentOfCropAffected;

    public String getPercentOfCropAffected() {
        return percentOfCropAffected;
    }

    public void setPercentOfCropAffected(String percentOfCropAffected) {
        this.percentOfCropAffected = percentOfCropAffected;
    }

    //--------------------
    @ColumnInfo(name = "Major_Symptoms")
    private String majorSymptoms;

    public String getMajorSymptoms() {
        return majorSymptoms;
    }

    public void setMajorSymptoms(String majorSymptoms) {
        this.majorSymptoms = majorSymptoms;
    }

    //--------------------
    @ColumnInfo(name = "Symptoms_Distribution")
    private String symptomsDistribution;

    public String getSymptomsDistribution() {
        return symptomsDistribution;
    }

    public void setSymptomsDistribution(String symptomsDistribution) {
        this.symptomsDistribution = symptomsDistribution;
    }

    //--------------------
    @ColumnInfo(name = "Symptoms_Describe_Problem")
    private String symptomsDescribeProblem;

    public String getSymptomsDescribeProblem() {
        return symptomsDescribeProblem;
    }

    public void setSymptomsDescribeProblem(String symptomsDescribeProblem) {
        this.symptomsDescribeProblem = symptomsDescribeProblem;
    }

    //--------------------
    @ColumnInfo(name = "Diagnosis_Type_Of_Problem")
    private String diagnosisTypeOfProblem;

    public String getDiagnosisTypeOfProblem() {
        return diagnosisTypeOfProblem;
    }

    public void setDiagnosisTypeOfProblem(String diagnosisTypeOfProblem) {
        this.diagnosisTypeOfProblem = diagnosisTypeOfProblem;
    }

    //--------------------
    @ColumnInfo(name = "Diagnosis")
    private String diagnosis;

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    //--------------------
    @ColumnInfo(name = "Current_Control")
    private String currentControl;

    public String getCurrentControl() {
        return currentControl;
    }

    public void setCurrentControl(String currentControl) {
        this.currentControl = currentControl;
    }

    //--------------------
    @ColumnInfo(name = "Current_Control_Practices_Used")
    private String currentControlPracticesUsed;

    public String getCurrentControlPracticesUsed() {
        return currentControlPracticesUsed;
    }

    public void setCurrentControlPracticesUsed(String currentControlPracticesUsed) {
        this.currentControlPracticesUsed = currentControlPracticesUsed;
    }

    //--------------------
    @ColumnInfo(name = "Recommendation_Type")
    private String recommendationType;

    public String getRecommendationType() {
        return recommendationType;
    }

    public void setRecommendationType(String recommendationType) {
        this.recommendationType = recommendationType;
    }

    //--------------------
    @ColumnInfo(name = "Recommendation_To_Manage_Now")
    private String recommendationToManageNow;

    public String getRecommendationToManageNow() {
        return recommendationToManageNow;
    }

    public void setRecommendationToManageNow(String recommendationToManageNow) {
        this.recommendationToManageNow = recommendationToManageNow;
    }

    //--------------------
    @ColumnInfo(name = "Recommendation_For_Future_Prevention")
    private String recommendationForFuturePrevention;

    public String getRecommendationForFuturePrevention() {
        return recommendationForFuturePrevention;
    }

    public void setRecommendationForFuturePrevention(String recommendationForFuturePrevention) {
        this.recommendationForFuturePrevention = recommendationForFuturePrevention;
    }

    //--------------------
    @ColumnInfo(name = "Recommendation_To_Manage_Now_AltLang")
    private String recommendationToManageNowAltLang;

    public String getRecommendationToManageNowAltLang() {
        return recommendationToManageNowAltLang;
    }

    public void setRecommendationToManageNowAltLang(String recommendationToManageNowAltLang) {
        this.recommendationToManageNowAltLang = recommendationToManageNowAltLang;
    }

    //--------------------
    @ColumnInfo(name = "Recommendation_For_Future_Prevention_AltLang")
    private String recommendationForFuturePreventionAltLang;

    public String getRecommendationForFuturePreventionAltLang() {
        return recommendationForFuturePreventionAltLang;
    }

    public void setRecommendationForFuturePreventionAltLang(String recommendationForFuturePreventionAltLang) {
        this.recommendationForFuturePreventionAltLang = recommendationForFuturePreventionAltLang;
    }

    //--------------------
    @ColumnInfo(name = "Sample_Sent_To_Lab")
    private String sampleSentToLab;

    public String getSampleSentToLab() {
        return sampleSentToLab;
    }

    public void setSampleSentToLab(String sampleSentToLab) {
        this.sampleSentToLab = sampleSentToLab;
    }

    //--------------------
    @ColumnInfo(name = "Factsheet_Given")
    private String factsheetGiven;

    public String getFactsheetGiven() {
        return factsheetGiven;
    }

    public void setFactsheetGiven(String factsheetGiven) {
        this.factsheetGiven = factsheetGiven;
    }

    //--------------------
    @ColumnInfo(name = "Field_Visit_Arranged")
    private String fieldVisitArranged;

    public String getFieldVisitArranged() {
        return fieldVisitArranged;
    }

    public void setFieldVisitArranged(String fieldVisitArranged) {
        this.fieldVisitArranged = fieldVisitArranged;
    }
}