package org.cabi.pdc.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Project implements Serializable{

    @SerializedName("ProjectId")
    private String projectId;
    @SerializedName("Name")
    private String name;
    @SerializedName("Code")
    private String code;

    // Getter Methods
    public String getProjectId() {
        return projectId;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    // Setter Methods
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }
}