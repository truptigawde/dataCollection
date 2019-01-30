package org.cabi.pdc.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Option implements Serializable{

    @SerializedName("Name")
    private String name;
    @SerializedName("Value")
    private String value;
    @SerializedName("TranslationId")
    private String translationId;
    @SerializedName("Id")
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTranslationId() {
        return translationId;
    }

    public void setTranslationId(String translationId) {
        this.translationId = translationId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
