package org.cabi.pdc.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Section implements Serializable{

    @SerializedName("Active")
    private boolean active;
    @SerializedName("FormDefinitionId")
    private String formDefinitionId;
    @SerializedName("fields")
    private ArrayList<Field> Fields;
    @SerializedName("ParentSectionId")
    private String parentSectionId;
    @SerializedName("TranslationId")
    private String translationId;
    @SerializedName("Order")
    private int order;
    @SerializedName("IsCore")
    private boolean isCore;
    @SerializedName("SectionId")
    private String sectionId;
    @SerializedName("Title")
    private String title;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getFormDefinitionId() {
        return formDefinitionId;
    }

    public void setFormDefinitionId(String formDefinitionId) {
        this.formDefinitionId = formDefinitionId;
    }

    public ArrayList<Field> getFields() {
        return Fields;
    }

    public void setFields(ArrayList<Field> fields) {
        Fields = fields;
    }

    public String getParentSectionId() {
        return parentSectionId;
    }

    public void setParentSectionId(String parentSectionId) {
        this.parentSectionId = parentSectionId;
    }

    public String getTranslationId() {
        return translationId;
    }

    public void setTranslationId(String translationId) {
        this.translationId = translationId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isCore() {
        return isCore;
    }

    public void setCore(boolean core) {
        isCore = core;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
