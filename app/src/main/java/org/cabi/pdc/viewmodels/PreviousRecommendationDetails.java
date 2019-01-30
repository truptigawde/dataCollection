package org.cabi.pdc.viewmodels;

public class PreviousRecommendationDetails {

    private String recommendationToManageNow;
    private String recommendationForFuturePrevention;
    private String recommendationToManageNowAltLang;
    private String recommendationForFuturePreventionAltLang;
    private String lastUsed;

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

    public String getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(String lastUsed) {
        this.lastUsed = lastUsed;
    }
}