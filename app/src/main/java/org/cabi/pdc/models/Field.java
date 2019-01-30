package org.cabi.pdc.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Field implements Serializable {

    @SerializedName("LineOrder")
    private int lineOrder;
    @SerializedName("Attributes")
    private ArrayList<Attribute> attributes;
    @SerializedName("Template")
    private String template;
    @SerializedName("FieldType")
    private int fieldType;
    @SerializedName("Order")
    private int order;
    @SerializedName("HintTranslationId")
    private String hintTranslationId;
    @SerializedName("Hint")
    private String hint;
    @SerializedName("Options")
    private ArrayList<Option> options;
    @SerializedName("ExportId")
    private String exportId;
    @SerializedName("Active")
    private boolean active;
    @SerializedName("Required")
    private boolean required;
    @SerializedName("OtherAllowed")
    private boolean otherAllowed;
    @SerializedName("BaseTitle")
    private String baseTitle;
    @SerializedName("GrowSelection")
    private boolean growSelection;
    @SerializedName("DontKnowAllowed")
    private boolean dontKnowAllowed;
    @SerializedName("DifferForSms")
    private boolean differForSms;
    @SerializedName("TranslationId")
    private String translationId;
    @SerializedName("SectionId")
    private String sectionId;
    @SerializedName("IsMultiValued")
    private boolean isMultiValued;
    @SerializedName("ItemSetId")
    private String itemSetId;
    @SerializedName("LinkedToField")
    private String linkedToField;
    @SerializedName("FieldDefinitionId")
    private String fieldDefinitionId;

    public int getLineOrder() {
        return lineOrder;
    }

    public void setLineOrder(int lineOrder) {
        this.lineOrder = lineOrder;
    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<Attribute> attributes) {
        this.attributes = attributes;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public int getFieldType() {
        return fieldType;
    }

    public void setFieldType(int fieldType) {
        this.fieldType = fieldType;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getHintTranslationId() {
        return hintTranslationId;
    }

    public void setHintTranslationId(String hintTranslationId) {
        this.hintTranslationId = hintTranslationId;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public ArrayList<Option> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<Option> options) {
        this.options = options;
    }

    public String getExportId() {
        return exportId;
    }

    public void setExportId(String exportId) {
        this.exportId = exportId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isOtherAllowed() {
        return otherAllowed;
    }

    public void setOtherAllowed(boolean otherAllowed) {
        this.otherAllowed = otherAllowed;
    }

    public String getBaseTitle() {
        return baseTitle;
    }

    public void setBaseTitle(String baseTitle) {
        this.baseTitle = baseTitle;
    }

    public boolean isGrowSelection() {
        return growSelection;
    }

    public void setGrowSelection(boolean growSelection) {
        this.growSelection = growSelection;
    }

    public boolean isDontKnowAllowed() {
        return dontKnowAllowed;
    }

    public void setDontKnowAllowed(boolean dontKnowAllowed) {
        this.dontKnowAllowed = dontKnowAllowed;
    }

    public boolean isDifferForSms() {
        return differForSms;
    }

    public void setDifferForSms(boolean differForSms) {
        this.differForSms = differForSms;
    }

    public String getTranslationId() {
        return translationId;
    }

    public void setTranslationId(String translationId) {
        this.translationId = translationId;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public boolean isMultiValued() {
        return isMultiValued;
    }

    public void setMultiValued(boolean multiValued) {
        isMultiValued = multiValued;
    }

    public String getItemSetId() {
        return itemSetId;
    }

    public void setItemSetId(String itemSetId) {
        this.itemSetId = itemSetId;
    }

    public String getLinkedToField() {
        return linkedToField;
    }

    public void setLinkedToField(String linkedToField) {
        this.linkedToField = linkedToField;
    }

    public String getFieldDefinitionId() {
        return fieldDefinitionId;
    }

    public void setFieldDefinitionId(String fieldDefinitionId) {
        this.fieldDefinitionId = fieldDefinitionId;
    }
}
