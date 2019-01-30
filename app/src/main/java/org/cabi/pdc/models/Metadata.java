package org.cabi.pdc.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Metadata implements Serializable {

    @SerializedName("MetadataValueId")
    private String metadataValueId;
    @SerializedName("ItemSetId")
    private String itemSetId;
    @SerializedName("Name")
    private String name;
    @SerializedName("Language")
    private String language;
    @SerializedName("Value")
    private String value;
    @SerializedName("Active")
    private Boolean active;
    @SerializedName("ParentId")
    private String parentId;
    @SerializedName("ParentName")
    private String parentName;
    @SerializedName("Template")
    private String template;

//    Getter Methods

    public String getMetadataValueId() {
        return metadataValueId;
    }

    public String getItemSetId() {
        return itemSetId;
    }

    public String getName() {
        return name;
    }

    public String getLanguage() {
        return language;
    }

    public String getValue() {
        return value;
    }

    public Boolean getActive() {
        return active;
    }

    public String getParentId() {
        return parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public String getTemplate() {
        return template;
    }

    // Setter Methods

    public void setMetadataValueId(String metadataValueId) {
        this.metadataValueId = metadataValueId;
    }

    public void setItemSetId(String itemSetId) {
        this.itemSetId = itemSetId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}