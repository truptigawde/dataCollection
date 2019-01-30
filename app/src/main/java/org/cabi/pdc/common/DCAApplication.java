package org.cabi.pdc.common;

import android.app.Application;
import android.content.res.Configuration;

import org.cabi.pdc.models.Metadata;
import org.cabi.pdc.models.Section;

import java.util.List;

public class DCAApplication extends Application{

    private boolean isAppUpdateDismissed;
    public boolean isAppUpdateDismissed() {
        return isAppUpdateDismissed;
    }

    public void setAppUpdateDismissed(boolean appUpdateDismissed) {
        isAppUpdateDismissed = appUpdateDismissed;
    }

    private List<Metadata> plantDoctorMetadata;
    public List<Metadata> getPlantDoctorMetadata() {
        return plantDoctorMetadata;
    }

    public void setPlantDoctorMetadata(List<Metadata> plantDoctorMetadata) {
        this.plantDoctorMetadata = plantDoctorMetadata;
    }

    private List<Metadata> clinicCodeMetadata;
    public List<Metadata> getClinicCodeMetadata() {
        return clinicCodeMetadata;
    }

    public void setClinicCodeMetadata(List<Metadata> clinicCodeMetadata) {
        this.clinicCodeMetadata = clinicCodeMetadata;
    }

    private List<Metadata> cropMetadata;
    public List<Metadata> getCropMetadata() {
        return cropMetadata;
    }

    public void setCropMetadata(List<Metadata> cropMetadata) {
        this.cropMetadata = cropMetadata;
    }

    private List<Metadata> cropVarietyMetadata;
    public List<Metadata> getCropVarietyMetadata() {
        return cropVarietyMetadata;
    }

    public void setCropVarietyMetadata(List<Metadata> cropVarietyMetadata) {
        this.cropVarietyMetadata = cropVarietyMetadata;
    }

    private List<Metadata> diagnosisMetadata;
    public List<Metadata> getDiagnosisMetadata() {
        return diagnosisMetadata;
    }

    public void setDiagnosisMetadata(List<Metadata> diagnosisMetadata) {
        this.diagnosisMetadata = diagnosisMetadata;
    }

    private List<Section> sectionList;
    public List<Section> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        super.onCreate();
        // Required initialization logic here!
    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
