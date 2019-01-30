package org.cabi.pdc.RoomDb;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "DCARecommendations")
public class RoomDB_Recommendations {

    public int getRecommendationId() {
        return recommendationId;
    }

    public void setRecommendationId(int recommendationId) {
        this.recommendationId = recommendationId;
    }

    @PrimaryKey
    @ColumnInfo(name = "Recommendation_Id")
    private int recommendationId;

    @ColumnInfo(name = "Recommendation_To_Manage_Now")
    private String recommendationToManageNow;

    @ColumnInfo(name = "Recommendation_For_Future_Prevention")
    private String recommendationForFuturePrevention;

    @ColumnInfo(name = "Recommendation_To_Manage_Now_Alt_Lang")
    private String recommendationToManageNowAltLang;

    @ColumnInfo(name = "Recommendation_For_Future_Prevention_Alt_Lang")
    private String recommendationForFuturePreventionAltLang;

    @ColumnInfo(name = "Crop")
    private String crop;

    @ColumnInfo(name = "Diagnosis")
    private String diagnosis;

    @ColumnInfo(name = "Last_Used")
    private String lastUsed;

    @ColumnInfo(name = "Most_Common")
    private int mostCommon;

    public String getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(String lastUsed) {
        this.lastUsed = lastUsed;
    }

    public int getMostCommon() {
        return mostCommon;
    }

    public void setMostCommon(int mostCommon) {
        this.mostCommon = mostCommon;
    }

    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getRecommendationToManageNow() {
        return recommendationToManageNow;
    }

    public void setRecommendationToManageNow(String recommendationToManageNow) {
        this.recommendationToManageNow = recommendationToManageNow;
    }

    public String getRecommendationForFuturePrevention() {
        return recommendationForFuturePrevention;
    }

    public void setRecommendationForFuturePrevention(String recommendationForFuturePrevention) {
        this.recommendationForFuturePrevention = recommendationForFuturePrevention;
    }

    public String getRecommendationToManageNowAltLang() {
        return recommendationToManageNowAltLang;
    }

    public void setRecommendationToManageNowAltLang(String recommendationToManageNowAltLang) {
        this.recommendationToManageNowAltLang = recommendationToManageNowAltLang;
    }

    public String getRecommendationForFuturePreventionAltLang() {
        return recommendationForFuturePreventionAltLang;
    }

    public void setRecommendationForFuturePreventionAltLang(String recommendationForFuturePreventionAltLang) {
        this.recommendationForFuturePreventionAltLang = recommendationForFuturePreventionAltLang;
    }
}