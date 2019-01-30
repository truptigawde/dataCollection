package org.cabi.pdc.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class FormDefinition implements Serializable{
    @SerializedName("FormDefinitionId")
    private String formDefinitionId;
    @SerializedName("Country")
    private String country;
    @SerializedName("ProjectCode")
    private String projectCode;
    @SerializedName("FormName")
    private String formName;
    @SerializedName("ReplacesFormId")
    private String replacesFormId;
    @SerializedName("Sections")
    private ArrayList<Section> sections;
    @SerializedName("Group")
    private String group;
    @SerializedName("Active")
    private boolean active;
    @SerializedName("Username")
    private String username;
    @SerializedName("Language")
    private String language;
    @SerializedName("CountryCode")
    private String countryCode;
    @SerializedName("ActiveSince")
    private String ActiveSince;

    public String getFormDefinitionId() {
        return formDefinitionId;
    }

    public void setFormDefinitionId(String formDefinitionId) {
        this.formDefinitionId = formDefinitionId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getReplacesFormId() {
        return replacesFormId;
    }

    public void setReplacesFormId(String replacesFormId) {
        this.replacesFormId = replacesFormId;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    public void setSections(ArrayList<Section> sections) {
        this.sections = sections;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getActiveSince() {
        return ActiveSince;
    }

    public void setActiveSince(String activeSince) {
        ActiveSince = activeSince;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @SerializedName("Version")
    private int version;
}
