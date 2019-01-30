package org.cabi.pdc.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FormDefinitions implements Serializable{

    public List<FormDefinition> getFormDefinitions() {
        return formDefinitions;
    }

    public void setFormDefinitions(List<FormDefinition> formDefinitions) {
        this.formDefinitions = formDefinitions;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("FormDefinitions")
    private List<FormDefinition> formDefinitions;
    @SerializedName("Message")
    private String message;
}