package org.cabi.pdc.common;

import org.cabi.pdc.RoomDb.RoomDB_Session;
import org.cabi.pdc.RoomDb.RoomDb_Form;
import org.cabi.pdc.models.FormDefinition;
import org.cabi.pdc.models.Metadata;
import org.cabi.pdc.models.MetadataTranslation;
import org.cabi.pdc.models.MyMonthlyReport;
import org.cabi.pdc.models.NationalReport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ApiData implements Serializable {
    private static volatile ApiData mInstance;

    public static ApiData getInstance() {
        if (mInstance == null) {
            synchronized (ApiData.class) {
                if (mInstance == null) {
                    mInstance = new ApiData();
                }
            }
        }
        return mInstance;
    }

    private ApiData() {
    }

    protected ApiData readResolve() {
        return getInstance();
    }


    private MyMonthlyReport myMonthlyReport;

    public MyMonthlyReport getMyMonthlyReport() {
        return myMonthlyReport;
    }

    public void setMyMonthlyReport(MyMonthlyReport myMonthlyReport) {
        this.myMonthlyReport = myMonthlyReport;
    }

    private NationalReport nationalReport;

    public NationalReport getNationalReport() {
        return nationalReport;
    }

    public void setNationalReport(NationalReport nationalReport) {
        this.nationalReport = nationalReport;
    }

    private String jwtToken;

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }


    private FormDefinition formDefinition;

    public FormDefinition getFormDefinition() {
        return formDefinition;
    }

    public void setFormDefinition(FormDefinition formDefinition) {
        this.formDefinition = formDefinition;
    }


    private ArrayList<Metadata> MetadataList;

    public ArrayList<Metadata> getMetadataList() {
        return MetadataList;
    }

    public void setMetadataList(ArrayList<Metadata> metadataList) {
        MetadataList = metadataList;
    }


    private String MetaDataLastUpdate;

    public String getMetaDataLastUpdate() {
        return MetaDataLastUpdate;
    }

    public void setMetaDataLastUpdate(String metaDataLastUpdate) {
        MetaDataLastUpdate = metaDataLastUpdate;
    }


    private HashMap<String, MetadataTranslation> metadataTranslationsList;

    public HashMap<String, MetadataTranslation> getMetadataTranslationsList() {
        return metadataTranslationsList;
    }

    public void setMetadataTranslationsList(HashMap<String, MetadataTranslation> metadataTranslationsList) {
        this.metadataTranslationsList = metadataTranslationsList;
    }

    private String userEmail;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    private RoomDb_Form currentForm;

    public RoomDb_Form getCurrentForm() {
        return currentForm;
    }

    public void setCurrentForm(RoomDb_Form currentForm) {
        this.currentForm = currentForm;
    }

    private RoomDB_Session currentSession;

    public RoomDB_Session getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(RoomDB_Session currentSession) {
        this.currentSession = currentSession;
    }

    private boolean isPasscodeChecked;
    public boolean isPasscodeChecked() {
        return isPasscodeChecked;
    }

    public void setPasscodeChecked(boolean passcodeChecked) {
        isPasscodeChecked = passcodeChecked;
    }
}