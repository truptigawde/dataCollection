package org.cabi.pdc.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MetadataTranslation implements Serializable {

    @SerializedName("Id")
    private String id;
    @SerializedName("Value")
    private String value;

    // Getter methods

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    // Setter methods
    public void setId(String id) {
        this.id = id;
    }

    public void setValue(String value) {
        this.value = value;
    }
}