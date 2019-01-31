package org.cabi.pdc.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.cabi.pdc.R;
import org.cabi.pdc.RoomDb.RoomDB_ClinicCode;
import org.cabi.pdc.RoomDb.RoomDB_Crop;
import org.cabi.pdc.RoomDb.RoomDB_CropVariety;
import org.cabi.pdc.RoomDb.RoomDB_Diagnosis;
import org.cabi.pdc.RoomDb.RoomDB_FarmerDetails;
import org.cabi.pdc.RoomDb.RoomDB_Location1;
import org.cabi.pdc.RoomDb.RoomDB_PlantDoctor;
import org.cabi.pdc.RoomDb.RoomDB_Location2;
import org.cabi.pdc.RoomDb.RoomDB_Recommendations;
import org.cabi.pdc.RoomDb.RoomDB_Session;
import org.cabi.pdc.activities.FormActivity;
import org.cabi.pdc.activities.FormFinishActivity;
import org.cabi.pdc.common.ApiData;
import org.cabi.pdc.common.DCAApplication;
import org.cabi.pdc.common.UrlFactory;
import org.cabi.pdc.common.Utility;
import org.cabi.pdc.helpers.CustomJSONObjectRequest;
import org.cabi.pdc.helpers.VolleyController;
import org.cabi.pdc.interfaces.VolleyCallback;
import org.cabi.pdc.models.Metadata;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class FormSummaryFragment extends Fragment implements View.OnClickListener {

    private Context mContext;

    private TextView txtSessionDate, valSessionDate, txtPDName, valPDName, txtClinicCode, valClinicCode, txtFarmerName, valFarmerName, txtFarmerPhone, valFarmerPhone, txtFarmerGender, valFarmerGender, txtFarmerLoc1, valFarmerLoc1, txtFarmerLoc2, valFarmerLoc2, txtFarmerLoc3, valFarmerLoc3, txtCropName, valCropName, txtCropVariety, valCropVariety, txtDevelopmentStage, valDevelopmentStage, txtPartsAffected, valPartsAffected, txtAreaAffected, valYearFirstSeen, valAreaPlanted, valUnit, valPercentCropAffected, txtSymptoms, valSymptoms, txtSymptomsDistribution, valSymptomsDistribution, txtSymptomsDescribeProblem, valSymptomsDescribeProblem, txtDiagnosisProblemType, valDiagnosisProblemType, txtDiagnosis, valDiagnosis, txtCurrentControl, valCurrentControl, txtCurrentControlPracticesUsed, valCurrentControlPracticesUsed, txtRecommendationType, valRecommendationType, txtPlannedActivitiesSampleSent, valPlannedActivitiesSampleSent, txtPlannedActivitiesFactsheetGiven, valPlannedActivitiesFactsheetGiven, txtPlannedActivitiesFieldVisitArranged, valPlannedActivitiesFieldVisitArranged;
    Button editPDoctor, editClinicCode, editFarmerDetails, editCropName, editCropVariety, editSampleBrought, editDevelopmentStage, editPartsAffected, editAreaAffected, editSymptoms, editSymptomsDistribution, editSymptomsDescribeProblem, editProblemType, editDiagnosis, editCurrentControl, editRecommendationType, editRecommendation, editPlannedActivities, btnSendRecommendationSMS;
    private EditText etSummaryPhoneNumber;
    private LinearLayout btnSaveDraft, btnDeleteDraft, btnSaveSubmit;
    RadioGroup rgRecommendationLanguage;

    DCAApplication dcaApplication;
    HashMap<String, String> headers;

    public FormSummaryFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.mContext = getActivity();
        dcaApplication = (DCAApplication) getActivity().getApplication();

        View rootView = inflater.inflate(R.layout.fragment_form_summary, container, false);

        btnSaveDraft = rootView.findViewById(R.id.FormSummary_btn_SaveDraft);
        btnSaveDraft.setOnClickListener(this);
        btnDeleteDraft = rootView.findViewById(R.id.FormSummary_btn_DeleteDraft);
        btnDeleteDraft.setOnClickListener(this);
        btnSaveSubmit = rootView.findViewById(R.id.FormSummary_btn_SaveSubmit);
        btnSaveSubmit.setOnClickListener(this);

        if (ApiData.getInstance().getCurrentForm() != null) {
            txtSessionDate = rootView.findViewById(R.id.txt_Summary_SessionDate);
            String labelSessionDate = "Session date";
            if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("Session date")) {
                labelSessionDate = ApiData.getInstance().getMetadataTranslationsList().get("Session date").getValue();
            }
            txtSessionDate.setText(labelSessionDate);
            valSessionDate = rootView.findViewById(R.id.val_Summary_SessionDate);
            String date = ApiData.getInstance().getCurrentForm().getFormStartDateTime();
            try {
                date = new SimpleDateFormat("dd MMMM yyyy").format((new SimpleDateFormat("dd MMMM yyyy")).parse(date));
            } catch (ParseException pe) {
                pe.printStackTrace();
            }

            valSessionDate.setText(date);

            valPDName = rootView.findViewById(R.id.val_Summary_PDName);
            valPDName.setText(ApiData.getInstance().getCurrentForm().getPlantDoctor());
            editPDoctor = rootView.findViewById(R.id.btn_Summary_EditPDName);
            editPDoctor.setOnClickListener(this);

            valClinicCode = rootView.findViewById(R.id.val_Summary_ClinicCode);
            valClinicCode.setText(ApiData.getInstance().getCurrentForm().getClinicCode());
            editClinicCode = rootView.findViewById(R.id.btn_Summary_EditClinicCode);
            editClinicCode.setOnClickListener(this);

            valFarmerName = rootView.findViewById(R.id.val_Summary_FarmerName);
            valFarmerName.setText(ApiData.getInstance().getCurrentForm().getFarmerName());
            valFarmerPhone = rootView.findViewById(R.id.val_Summary_FarmerPhone);
            valFarmerPhone.setText(ApiData.getInstance().getCurrentForm().getFarmerPhoneNumber());
            valFarmerGender = rootView.findViewById(R.id.val_Summary_FarmerSex);
            valFarmerGender.setText(ApiData.getInstance().getCurrentForm().getFarmerGender());
            valFarmerLoc1 = rootView.findViewById(R.id.val_Summary_FarmerLoc1);
            valFarmerLoc1.setText(ApiData.getInstance().getCurrentForm().getFarmerLocation1());
            valFarmerLoc2 = rootView.findViewById(R.id.val_Summary_FarmerLoc2);
            valFarmerLoc2.setText(ApiData.getInstance().getCurrentForm().getFarmerLocation2());
            valFarmerLoc3 = rootView.findViewById(R.id.val_Summary_FarmerLoc3);
            valFarmerLoc3.setText(ApiData.getInstance().getCurrentForm().getFarmerLocation3());
            editFarmerDetails = rootView.findViewById(R.id.btn_Summary_EditFarmerDetails);
            editFarmerDetails.setOnClickListener(this);

            valCropName = rootView.findViewById(R.id.val_Summary_CropName);
            valCropName.setText(ApiData.getInstance().getCurrentForm().getCropName());
            editCropName = rootView.findViewById(R.id.btn_Summary_EditCropName);
            editCropName.setOnClickListener(this);

            valCropVariety = rootView.findViewById(R.id.val_Summary_CropVariety);
            valCropVariety.setText(ApiData.getInstance().getCurrentForm().getCropVariety());
            editCropVariety = rootView.findViewById(R.id.btn_Summary_EditVarietyName);
            editCropVariety.setOnClickListener(this);

            ImageView imgSample = rootView.findViewById(R.id.imgCropSample);
            TextView txtSamplePhoto = rootView.findViewById(R.id.val_Summary_CropPhoto);
            if (ApiData.getInstance().getCurrentForm().getCropSampleBrought() != null &&
                    ApiData.getInstance().getCurrentForm().getCropSampleBrought().toUpperCase().equals("YES") &&
                    ApiData.getInstance().getCurrentForm().getCropPhotoSample() != null) {
                imgSample.setImageBitmap(Utility.getBitmapFromString(ApiData.getInstance().getCurrentForm().getCropPhotoSample()));
                imgSample.setVisibility(View.VISIBLE);
                txtSamplePhoto.setVisibility(View.GONE);
            } else {
                imgSample.setVisibility(View.GONE);
                txtSamplePhoto.setVisibility(View.VISIBLE);
            }
            editSampleBrought = rootView.findViewById(R.id.btn_Summary_EditCropSampleBrought);
            editSampleBrought.setOnClickListener(this);

            valDevelopmentStage = rootView.findViewById(R.id.val_Summary_DevelopmentStage);
            String devStageValues = ApiData.getInstance().getCurrentForm().getDevelopmentStage();
            if (!TextUtils.isEmpty(devStageValues) && devStageValues.contains("##")) {
                devStageValues = devStageValues.replace("##", "\n");
            }
            valDevelopmentStage.setText(devStageValues);
            editDevelopmentStage = rootView.findViewById(R.id.btn_Summary_EditDevelopmentStage);
            editDevelopmentStage.setOnClickListener(this);

            valPartsAffected = rootView.findViewById(R.id.val_Summary_PartsAffected);
            String partsAffectValues = ApiData.getInstance().getCurrentForm().getPartsAffected();
            if (!TextUtils.isEmpty(partsAffectValues) && partsAffectValues.contains("##")) {
                partsAffectValues = partsAffectValues.replace("##", "\n");
            }
            valPartsAffected.setText(partsAffectValues);
            editPartsAffected = rootView.findViewById(R.id.btn_Summary_EditPartsAffected);
            editPartsAffected.setOnClickListener(this);

            valYearFirstSeen = rootView.findViewById(R.id.val_Summary_YearFirstSeen);
            valYearFirstSeen.setText(ApiData.getInstance().getCurrentForm().getYearFirstSeen());
            valAreaPlanted = rootView.findViewById(R.id.val_Summary_AreaPlanted);
            valAreaPlanted.setText(ApiData.getInstance().getCurrentForm().getAreaPlanted());
            valUnit = rootView.findViewById(R.id.val_Summary_Unit);
            valUnit.setText(ApiData.getInstance().getCurrentForm().getAreaPlantedUnit());
            valPercentCropAffected = rootView.findViewById(R.id.val_Summary_PercentCropAffected);
            valPercentCropAffected.setText(ApiData.getInstance().getCurrentForm().getPercentOfCropAffected());
            editAreaAffected = rootView.findViewById(R.id.btn_Summary_EditAreaAffected);
            editAreaAffected.setOnClickListener(this);

            valSymptoms = rootView.findViewById(R.id.val_Summary_MajorSymptoms);
            String symptomsValues = ApiData.getInstance().getCurrentForm().getMajorSymptoms();
            if (!TextUtils.isEmpty(symptomsValues) && symptomsValues.contains("##")) {
                symptomsValues = symptomsValues.replace("##", "\n");
            }
            valSymptoms.setText(symptomsValues);
            editSymptoms = rootView.findViewById(R.id.btn_Summary_EditMajorSymptoms);
            editSymptoms.setOnClickListener(this);

            valSymptomsDistribution = rootView.findViewById(R.id.val_Summary_SymptomsDistribution);
            String symptomDistributionValues = ApiData.getInstance().getCurrentForm().getSymptomsDistribution();
            if (!TextUtils.isEmpty(symptomDistributionValues) && symptomDistributionValues.contains("##")) {
                symptomDistributionValues = symptomDistributionValues.replace("##", "\n");
            }
            valSymptomsDistribution.setText(symptomDistributionValues);
            editSymptomsDistribution = rootView.findViewById(R.id.btn_Summary_EditSymptomsDistribution);
            editSymptomsDistribution.setOnClickListener(this);

            valSymptomsDescribeProblem = rootView.findViewById(R.id.val_Summary_SymptomsDescribeProblem);
            valSymptomsDescribeProblem.setText(ApiData.getInstance().getCurrentForm().getSymptomsDescribeProblem());
            editSymptomsDescribeProblem = rootView.findViewById(R.id.btn_Summary_EditSymptomsDescribeProblem);
            editSymptomsDescribeProblem.setOnClickListener(this);

            valDiagnosisProblemType = rootView.findViewById(R.id.val_Summary_DiagnosisProblemType);
            valDiagnosisProblemType.setText(ApiData.getInstance().getCurrentForm().getDiagnosisTypeOfProblem());
            editProblemType = rootView.findViewById(R.id.btn_Summary_EditDiagnosisProblemType);
            editProblemType.setOnClickListener(this);

            valDiagnosis = rootView.findViewById(R.id.val_Summary_Diagnosis);
            valDiagnosis.setText(ApiData.getInstance().getCurrentForm().getDiagnosis());
            editDiagnosis = rootView.findViewById(R.id.btn_Summary_EditDiagnosis);
            editDiagnosis.setOnClickListener(this);

            valCurrentControlPracticesUsed = rootView.findViewById(R.id.val_Summary_CurrentControlPracticesUsed);
            valCurrentControlPracticesUsed.setText(ApiData.getInstance().getCurrentForm().getCurrentControlPracticesUsed());
            editCurrentControl = rootView.findViewById(R.id.btn_Summary_EditCurrentControl);
            editCurrentControl.setOnClickListener(this);

            valRecommendationType = rootView.findViewById(R.id.val_Summary_RecommendationType);
            String recommendationTypeValues = ApiData.getInstance().getCurrentForm().getRecommendationType();
            if (!TextUtils.isEmpty(recommendationTypeValues) && recommendationTypeValues.contains("##")) {
                recommendationTypeValues = recommendationTypeValues.replace("##", "\n");
            }
            valRecommendationType.setText(recommendationTypeValues);
            editRecommendationType = rootView.findViewById(R.id.btn_Summary_EditRecommendationType);
            editRecommendationType.setOnClickListener(this);

            editRecommendation = rootView.findViewById(R.id.btn_Summary_EditRecommendation);
            editRecommendation.setOnClickListener(this);

            valPlannedActivitiesSampleSent = rootView.findViewById(R.id.val_Summary_PASampleSentToLab);
            valPlannedActivitiesSampleSent.setText(ApiData.getInstance().getCurrentForm().getSampleSentToLab());
            valPlannedActivitiesFactsheetGiven = rootView.findViewById(R.id.val_Summary_PAFactsheetGiven);
            valPlannedActivitiesFactsheetGiven.setText(ApiData.getInstance().getCurrentForm().getFactsheetGiven());
            valPlannedActivitiesFieldVisitArranged = rootView.findViewById(R.id.val_Summary_PAFieldVisitArranged);
            valPlannedActivitiesFieldVisitArranged.setText(ApiData.getInstance().getCurrentForm().getFieldVisitArranged());
            editPlannedActivities = rootView.findViewById(R.id.btn_Summary_EditPlannedActivities);
            editPlannedActivities.setOnClickListener(this);

            if (ApiData.getInstance().getCurrentForm().getFormStatus().toUpperCase().equals("SENT") ||
                    ApiData.getInstance().getCurrentForm().getFormStatus().toUpperCase().equals("NOTSENT")) {
                editPDoctor.setVisibility(View.INVISIBLE);
                editClinicCode.setVisibility(View.INVISIBLE);
                editFarmerDetails.setVisibility(View.INVISIBLE);
                editCropName.setVisibility(View.INVISIBLE);
                editCropVariety.setVisibility(View.INVISIBLE);
                editSampleBrought.setVisibility(View.INVISIBLE);
                editDevelopmentStage.setVisibility(View.INVISIBLE);
                editPartsAffected.setVisibility(View.INVISIBLE);
                editAreaAffected.setVisibility(View.INVISIBLE);
                editSymptoms.setVisibility(View.INVISIBLE);
                editSymptomsDistribution.setVisibility(View.INVISIBLE);
                editSymptomsDescribeProblem.setVisibility(View.INVISIBLE);
                editProblemType.setVisibility(View.INVISIBLE);
                editDiagnosis.setVisibility(View.INVISIBLE);
                editCurrentControl.setVisibility(View.INVISIBLE);
                editRecommendationType.setVisibility(View.INVISIBLE);
                editRecommendation.setVisibility(View.INVISIBLE);
                editPlannedActivities.setVisibility(View.INVISIBLE);
                btnSaveDraft.setVisibility(View.INVISIBLE);
                btnSaveSubmit.setVisibility(View.INVISIBLE);
                btnDeleteDraft.setVisibility(View.INVISIBLE);
            } else {
                editPDoctor.setVisibility(View.VISIBLE);
                editClinicCode.setVisibility(View.VISIBLE);
                editFarmerDetails.setVisibility(View.VISIBLE);
                editCropName.setVisibility(View.VISIBLE);
                editCropVariety.setVisibility(View.VISIBLE);
                editSampleBrought.setVisibility(View.VISIBLE);
                editDevelopmentStage.setVisibility(View.VISIBLE);
                editPartsAffected.setVisibility(View.VISIBLE);
                editAreaAffected.setVisibility(View.VISIBLE);
                editSymptoms.setVisibility(View.VISIBLE);
                editSymptomsDistribution.setVisibility(View.VISIBLE);
                editSymptomsDescribeProblem.setVisibility(View.VISIBLE);
                editProblemType.setVisibility(View.VISIBLE);
                editDiagnosis.setVisibility(View.VISIBLE);
                editCurrentControl.setVisibility(View.VISIBLE);
                editRecommendationType.setVisibility(View.VISIBLE);
                editRecommendation.setVisibility(View.VISIBLE);
                editPlannedActivities.setVisibility(View.VISIBLE);
                btnSaveDraft.setVisibility(View.VISIBLE);
                btnSaveSubmit.setVisibility(View.VISIBLE);
                btnDeleteDraft.setVisibility(View.VISIBLE);
            }
        }

        TextView txt_Summary_SendSMSIn = rootView.findViewById(R.id.txt_Summary_SendSMSIn);
        String strSendSMSIn = "Send SMS in";
        if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("SummarySendSmsIn")) {
            strSendSMSIn = ApiData.getInstance().getMetadataTranslationsList().get("SummarySendSmsIn").getValue();
        }
        txt_Summary_SendSMSIn.setText(strSendSMSIn);

        final TextView txt_Summary_RecommendationCurrentControl = rootView.findViewById(R.id.txt_Summary_RecommendationCurrentControl);
        final TextView txt_Summary_RecommendationFuturePrevention = rootView.findViewById(R.id.txt_Summary_RecommendationFuturePrevention);
        if (ApiData.getInstance().getCurrentForm() != null) {
            if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getRecommendationToManageNow())) {
                String strSummaryRecommendationCurrentControl = "For current control";
                if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("SummaryForCurrentControl")) {
                    strSummaryRecommendationCurrentControl = ApiData.getInstance().getMetadataTranslationsList().get("SummaryForCurrentControl").getValue();
                }
                txt_Summary_RecommendationCurrentControl.setText(strSummaryRecommendationCurrentControl + ": " + ApiData.getInstance().getCurrentForm().getRecommendationToManageNow());
                txt_Summary_RecommendationCurrentControl.setVisibility(View.VISIBLE);
            } else {
                txt_Summary_RecommendationCurrentControl.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getRecommendationForFuturePrevention())) {
                String strSummaryRecommendationFuturePrevention = "For future prevention";
                if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("SummaryForFutureControl")) {
                    strSummaryRecommendationFuturePrevention = ApiData.getInstance().getMetadataTranslationsList().get("SummaryForFutureControl").getValue();
                }
                txt_Summary_RecommendationFuturePrevention.setText(strSummaryRecommendationFuturePrevention + ": " + ApiData.getInstance().getCurrentForm().getRecommendationForFuturePrevention());
                txt_Summary_RecommendationFuturePrevention.setVisibility(View.VISIBLE);
            } else {
                txt_Summary_RecommendationFuturePrevention.setVisibility(View.GONE);
            }
        }


        rgRecommendationLanguage = rootView.findViewById(R.id.rgRecommendationLanguage);
        rgRecommendationLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton selectedRB = (RadioButton) view;
                if (selectedRB.getText().toString().toUpperCase() == "ENGLISH") {
                    if (ApiData.getInstance().getCurrentForm() != null) {
                        if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getRecommendationToManageNow())) {
                            String strSummaryRecommendationCurrentControl = "For current control";
                            if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("SummaryForCurrentControl")) {
                                strSummaryRecommendationCurrentControl = ApiData.getInstance().getMetadataTranslationsList().get("SummaryForCurrentControl").getValue();
                            }
                            txt_Summary_RecommendationCurrentControl.setText(strSummaryRecommendationCurrentControl + ": " + ApiData.getInstance().getCurrentForm().getRecommendationToManageNow());
                        }

                        if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getRecommendationForFuturePrevention())) {
                            String strSummaryRecommendationFuturePrevention = "For future prevention";
                            if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("SummaryForFutureControl")) {
                                strSummaryRecommendationFuturePrevention = ApiData.getInstance().getMetadataTranslationsList().get("SummaryForFutureControl").getValue();
                            }
                            txt_Summary_RecommendationFuturePrevention.setText(strSummaryRecommendationFuturePrevention + ": " + ApiData.getInstance().getCurrentForm().getRecommendationForFuturePrevention());
                        }
                    }
                } else {
                    if (ApiData.getInstance().getCurrentForm() != null) {
                        if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getRecommendationToManageNowAltLang())) {
                            String strSummaryRecommendationCurrentControl = "For current control";
                            if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("SummaryForCurrentControl")) {
                                strSummaryRecommendationCurrentControl = ApiData.getInstance().getMetadataTranslationsList().get("SummaryForCurrentControl").getValue();
                            }
                            txt_Summary_RecommendationCurrentControl.setText(strSummaryRecommendationCurrentControl + ": " + ApiData.getInstance().getCurrentForm().getRecommendationToManageNowAltLang());
                        }
                        if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getRecommendationForFuturePreventionAltLang())) {
                            String strSummaryRecommendationFuturePrevention = "For future prevention";
                            if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("SummaryForFutureControl")) {
                                strSummaryRecommendationFuturePrevention = ApiData.getInstance().getMetadataTranslationsList().get("SummaryForFutureControl").getValue();
                            }
                            txt_Summary_RecommendationFuturePrevention.setText(strSummaryRecommendationFuturePrevention + ": " + ApiData.getInstance().getCurrentForm().getRecommendationForFuturePreventionAltLang());
                        }
                    }
                }
            }
        });

        TextView txt_Summary_SendRecommendationTo = rootView.findViewById(R.id.txt_Summary_SendRecommendationSMS);
        String strSendRecommendationTo = "Send this recommendation to";
        if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("SummarySendThisRecommendationTo")) {
            strSendRecommendationTo = ApiData.getInstance().getMetadataTranslationsList().get("SummarySendThisRecommendationTo").getValue();
        }
        txt_Summary_SendRecommendationTo.setText(strSendRecommendationTo);

        etSummaryPhoneNumber = rootView.findViewById(R.id.et_Summary_PhoneNumber);
        btnSendRecommendationSMS = rootView.findViewById(R.id.btn_Summary_SendRecommendationSMS);
        btnSendRecommendationSMS.setOnClickListener(this);
        if (ApiData.getInstance().getCurrentForm().getFormStatus().toUpperCase().equals("SENT")) {
            if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getRecommendationToManageNow())
                    || !TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getRecommendationToManageNowAltLang())
                    || !TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getRecommendationForFuturePrevention())
                    || !TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getRecommendationForFuturePreventionAltLang())) {
                btnSendRecommendationSMS.setVisibility(View.VISIBLE);
            } else {
                btnSendRecommendationSMS.setVisibility(View.INVISIBLE);
            }
        }

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.FormSummary_btn_SaveDraft: {
                if (ApiData.getInstance().getCurrentForm() != null) {

                    if (TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getClinicCode())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Clinic code required")
                                .setMessage("Can't proceed without clinic code")
                                .setCancelable(true)
                                .setPositiveButton("Ok", null);
                        builder.show();
                        return;
                    }

                    saveFormLocally("DRAFT");

                    if (((FormActivity) getActivity()).editDraft || ((FormActivity) getActivity()).viewSubmitted) {
                        ((FormActivity) getActivity()).goBack();
                    } else {
                        Intent intFormFinish = new Intent(getActivity(), FormFinishActivity.class);
                        intFormFinish.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intFormFinish);
                    }

//                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dcaAlertDialog);
//                    builder.setTitle("Do you want to send a SMS to the farmer?")
//                            .setMessage("You will be redirected to your messaging app where the recommendation and farmer phone number will be pre-filled.")
//                            .setCancelable(true)
//                            .setPositiveButton("YES, SEND SMS", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
//                                    smsIntent.setData(Uri.parse("smsto: " + etSummaryPhoneNumber.getText().toString()));
//                                    if (ApiData.getInstance().getCurrentForm() != null && (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getRecommendationToManageNow()) || !TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getRecommendationForFuturePrevention()))) {
//                                        smsIntent.putExtra("sms_body", "For current control: " + ApiData.getInstance().getCurrentForm().getRecommendationToManageNow() + "\n\n" + "For future prevention: " + ApiData.getInstance().getCurrentForm().getRecommendationForFuturePrevention());
//
//                                        if (smsIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//                                            startActivity(smsIntent);
//                                        }
//                                    }
//                                }
//                            })
//                            .setNegativeButton("NO, DON'T SEND SMS", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    Intent intFormFinish = new Intent(getActivity(), FormFinishActivity.class);
//                                    intFormFinish.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    startActivity(intFormFinish);
//                                }
//                            });
//                    AlertDialog sendSMSAlert = builder.create();
//                    sendSMSAlert.show();
//                    sendSMSAlert.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ButtonGray)));
//                    sendSMSAlert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.CYAN);
//                    sendSMSAlert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
                } else {
                    Toast.makeText(getActivity(), "Can't save blank form as draft", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            break;
            case R.id.FormSummary_btn_DeleteDraft: {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dcaAlertDialog);
                builder.setTitle("Delete draft form")
                        .setMessage("This is a permanent action. Once you have deleted a draft it cannot be retrieved.")
                        .setCancelable(true)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int index) {

//                                for (int i = 0; i <= getFragmentManager().getBackStackEntryCount(); i++) {
//                                    getFragmentManager().popBackStackImmediate();
//                                }
//                                if (getFragmentManager() != null)
//                                    getFragmentManager().popBackStackImmediate();
//                                if (getFragmentManager() != null)
//                                    getFragmentManager().popBackStackImmediate();

                                if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getFormId())) {
                                    List<RoomDB_Session> sessions = FormActivity.dcaFormDatabase.dcaSessionDao().getAllSessions();
                                    if (sessions != null && sessions.size() > 0) {
                                        for (RoomDB_Session sessionItem : sessions) {
                                            if (sessionItem.getSessionId().equals(ApiData.getInstance().getCurrentForm().getSessionId())) {
                                                int drafts = sessionItem.getDraft();
                                                if (drafts > 1) {
                                                    sessionItem.setDraft(drafts - 1);
                                                    FormActivity.dcaFormDatabase.dcaSessionDao().updateSession(sessionItem);
                                                } else if (drafts == 1) {
                                                    FormActivity.dcaFormDatabase.dcaSessionDao().deleteSession(sessionItem);
                                                }
                                                break;
                                            }
                                        }
                                    }

                                    FormActivity.dcaFormDatabase.dcaFormDao().deleteForm(ApiData.getInstance().getCurrentForm());
                                }

                                if (((FormActivity) getActivity()).editDraft || ((FormActivity) getActivity()).viewSubmitted) {
                                    ((FormActivity) getActivity()).goBack();
                                } else {
                                    Intent intFormFinish = new Intent(getActivity(), FormFinishActivity.class);
                                    intFormFinish.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intFormFinish);
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null);
                AlertDialog deleteDraftAlert = builder.create();
                deleteDraftAlert.setInverseBackgroundForced(true);
                deleteDraftAlert.show();
                deleteDraftAlert.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ButtonGray)));
                deleteDraftAlert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.CYAN);
                deleteDraftAlert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
            }
            break;
            case R.id.FormSummary_btn_SaveSubmit: {
                if (ApiData.getInstance().getCurrentForm() != null) {
                    if (TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getClinicCode())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Clinic code required")
                                .setMessage("Can't proceed without clinic code")
                                .setCancelable(true)
                                .setPositiveButton("Ok", null);
                        builder.show();
                        return;
                    }
                    saveFormLocally("NOTSENT");
                    submitForm();

                    if (((FormActivity) getActivity()).editDraft || ((FormActivity) getActivity()).viewSubmitted) {
                        ((FormActivity) getActivity()).goBack();
                    } else {
                        Intent intFormFinish = new Intent(getActivity(), FormFinishActivity.class);
                        intFormFinish.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intFormFinish);
                    }
                } else {
                    Toast.makeText(getActivity(), "Can't submit blank form", Toast.LENGTH_LONG).show();
                }
            }
            break;
            case R.id.btn_Summary_EditPDName: {
                int pos = 0;
                for (int i = 0; i < dcaApplication.getSectionList().size(); i++) {
                    if (dcaApplication.getSectionList().get(i).getTitle().toUpperCase().equals("PLANT DOCTOR")) {
                        pos = i + 1;
                        break;
                    }
                }
                ((FormActivity) getActivity()).loadNextSection(pos, getId(), true);
            }
            break;
            case R.id.btn_Summary_EditClinicCode: {
                int pos = 0;
                for (int i = 0; i < dcaApplication.getSectionList().size(); i++) {
                    if (dcaApplication.getSectionList().get(i).getTitle().toUpperCase().equals("CLINIC DETAILS")) {
                        pos = i + 1;
                        break;
                    }
                }
                ((FormActivity) getActivity()).loadNextSection(pos, getId(), true);
            }
            break;
            case R.id.btn_Summary_EditFarmerDetails: {
                int pos = 0;
                for (int i = 0; i < dcaApplication.getSectionList().size(); i++) {
                    if (dcaApplication.getSectionList().get(i).getTitle().toUpperCase().equals("ABOUT THE FARMER")) {
                        pos = i + 1;
                        break;
                    }
                }
                ((FormActivity) getActivity()).loadNextSection(pos, getId(), true);
            }
            break;
            case R.id.btn_Summary_EditCropName: {
                int pos = 0;
                for (int i = 0; i < dcaApplication.getSectionList().size(); i++) {
                    if (dcaApplication.getSectionList().get(i).getTitle().toUpperCase().equals("SAMPLE INFORMATION - CROP")) {
                        pos = i + 1;
                        break;
                    }
                }
                ((FormActivity) getActivity()).loadNextSection(pos, getId(), true);
            }
            break;
            case R.id.btn_Summary_EditVarietyName: {
                int pos = 0;
                for (int i = 0; i < dcaApplication.getSectionList().size(); i++) {
                    if (dcaApplication.getSectionList().get(i).getTitle().toUpperCase().equals("SAMPLE INFORMATION - VARIETY")) {
                        pos = i + 1;
                        break;
                    }
                }
                ((FormActivity) getActivity()).loadNextSection(pos, getId(), true);
            }
            break;
            case R.id.btn_Summary_EditCropSampleBrought: {
                int pos = 0;
                for (int i = 0; i < dcaApplication.getSectionList().size(); i++) {
                    if (dcaApplication.getSectionList().get(i).getTitle().toUpperCase().equals("SAMPLE INFORMATION - SAMPLE")) {
                        pos = i + 1;
                        break;
                    }
                }
                ((FormActivity) getActivity()).loadNextSection(pos, getId(), true);
            }
            break;
            case R.id.btn_Summary_EditDevelopmentStage: {
                int pos = 0;
                for (int i = 0; i < dcaApplication.getSectionList().size(); i++) {
                    if (dcaApplication.getSectionList().get(i).getTitle().toUpperCase().equals("DEVELOPMENT STAGE")) {
                        pos = i + 1;
                        break;
                    }
                }
                ((FormActivity) getActivity()).loadNextSection(pos, getId(), true);
            }
            break;
            case R.id.btn_Summary_EditPartsAffected: {
                int pos = 0;
                for (int i = 0; i < dcaApplication.getSectionList().size(); i++) {
                    if (dcaApplication.getSectionList().get(i).getTitle().toUpperCase().equals("PARTS AFFECTED")) {
                        pos = i + 1;
                        break;
                    }
                }
                ((FormActivity) getActivity()).loadNextSection(pos, getId(), true);
            }
            break;
            case R.id.btn_Summary_EditAreaAffected: {
                int pos = 0;
                for (int i = 0; i < dcaApplication.getSectionList().size(); i++) {
                    if (dcaApplication.getSectionList().get(i).getTitle().toUpperCase().equals("AREA AFFECTED")) {
                        pos = i + 1;
                        break;
                    }
                }
                ((FormActivity) getActivity()).loadNextSection(pos, getId(), true);
            }
            break;
            case R.id.btn_Summary_EditMajorSymptoms: {
                int pos = 0;
                for (int i = 0; i < dcaApplication.getSectionList().size(); i++) {
                    if (dcaApplication.getSectionList().get(i).getTitle().toUpperCase().equals("SYMPTOMS")) {
                        pos = i + 1;
                        break;
                    }
                }
                ((FormActivity) getActivity()).loadNextSection(pos, getId(), true);
            }
            break;
            case R.id.btn_Summary_EditSymptomsDistribution: {
                int pos = 0;
                for (int i = 0; i < dcaApplication.getSectionList().size(); i++) {
                    if (dcaApplication.getSectionList().get(i).getTitle().toUpperCase().equals("SYMPTOMS - DISTRIBUTION")) {
                        pos = i + 1;
                        break;
                    }
                }
                ((FormActivity) getActivity()).loadNextSection(pos, getId(), true);
            }
            break;
            case R.id.btn_Summary_EditSymptomsDescribeProblem: {
                int pos = 0;
                for (int i = 0; i < dcaApplication.getSectionList().size(); i++) {
                    if (dcaApplication.getSectionList().get(i).getTitle().toUpperCase().equals("SYMPTOMS - DESCRIBE PROBLEM")) {
                        pos = i + 1;
                        break;
                    }
                }
                ((FormActivity) getActivity()).loadNextSection(pos, getId(), true);
            }
            break;
            case R.id.btn_Summary_EditDiagnosisProblemType: {
                int pos = 0;
                for (int i = 0; i < dcaApplication.getSectionList().size(); i++) {
                    if (dcaApplication.getSectionList().get(i).getTitle().toUpperCase().equals("DIAGNOSIS - TYPE OF PROBLEM")) {
                        pos = i + 1;
                        break;
                    }
                }
                ((FormActivity) getActivity()).loadNextSection(pos, getId(), true);
            }
            break;
            case R.id.btn_Summary_EditDiagnosis: {
                int pos = 0;
                for (int i = 0; i < dcaApplication.getSectionList().size(); i++) {
                    if (dcaApplication.getSectionList().get(i).getTitle().toUpperCase().equals("DIAGNOSIS")) {
                        pos = i + 1;
                        break;
                    }
                }
                ((FormActivity) getActivity()).loadNextSection(pos, getId(), true);
            }
            break;
            case R.id.btn_Summary_EditCurrentControl: {
                int pos = 0;
                for (int i = 0; i < dcaApplication.getSectionList().size(); i++) {
                    if (dcaApplication.getSectionList().get(i).getTitle().toUpperCase().equals("CURRENT CONTROL")) {
                        pos = i + 1;
                        break;
                    }
                }
                ((FormActivity) getActivity()).loadNextSection(pos, getId(), true);
            }
            break;
            case R.id.btn_Summary_EditRecommendationType: {
                int pos = 0;
                for (int i = 0; i < dcaApplication.getSectionList().size(); i++) {
                    if (dcaApplication.getSectionList().get(i).getTitle().toUpperCase().equals("RECOMMENDATION - TYPE")) {
                        pos = i + 1;
                        break;
                    }
                }
                ((FormActivity) getActivity()).loadNextSection(pos, getId(), true);
            }
            break;
            case R.id.btn_Summary_EditPlannedActivities: {
                int pos = 0;
                for (int i = 0; i < dcaApplication.getSectionList().size(); i++) {
                    if (dcaApplication.getSectionList().get(i).getTitle().toUpperCase().equals("PLANNED ACTIVITIES")) {
                        pos = i + 1;
                        break;
                    }
                }
                ((FormActivity) getActivity()).loadNextSection(pos, getId(), true);
            }
            break;
            case R.id.btn_Summary_EditRecommendation: {
                int pos = 0;
                for (int i = 0; i < dcaApplication.getSectionList().size(); i++) {
                    if (dcaApplication.getSectionList().get(i).getTitle().toUpperCase().equals("RECOMMENDATION")) {
                        pos = i + 1;
                        break;
                    }
                }
                ((FormActivity) getActivity()).loadNextSection(pos, getId(), true);
            }
            break;
            case R.id.btn_Summary_SendRecommendationSMS: {
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                if (TextUtils.isEmpty(etSummaryPhoneNumber.getText().toString())) {

                    String strPhoneReqd = "Please enter the phone number you want to send the message to";
                    if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("ErrorPhoneNumberRequired")) {
                        strPhoneReqd = ApiData.getInstance().getMetadataTranslationsList().get("ErrorPhoneNumberRequired").getValue().toString();
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dcaAlertDialog);
                    builder.setTitle("Error")
                            .setMessage(strPhoneReqd)
                            .setCancelable(true)
                            .setPositiveButton("OK", null);
                    AlertDialog sendSMSAlert = builder.create();
                    sendSMSAlert.show();
                    sendSMSAlert.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ButtonGray)));
                    sendSMSAlert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.CYAN);
                } else {
                    smsIntent.setData(Uri.parse("smsto: " + etSummaryPhoneNumber.getText().toString()));  // This ensures only SMS apps respond
                    if (ApiData.getInstance().getCurrentForm() != null && !TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getRecommendationToManageNow())) {
                        smsIntent.putExtra("sms_body", "For current control: " + ApiData.getInstance().getCurrentForm().getRecommendationToManageNow() + "\n\n" + "For future prevention: " + ApiData.getInstance().getCurrentForm().getRecommendationForFuturePrevention());

                        if (smsIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivity(smsIntent);
                        }
                    } else {

                        String strRecommReqd = "SMS cannot be sent as the message is empty, please add a recommendation and try again.";
                        if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("ErrorSMSRecommendationEmpty")) {
                            strRecommReqd = ApiData.getInstance().getMetadataTranslationsList().get("ErrorSMSRecommendationEmpty").getValue().toString();
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dcaAlertDialog);
                        builder.setTitle("Error")
                                .setMessage(strRecommReqd)
                                .setCancelable(true)
                                .setPositiveButton("OK", null);
                        AlertDialog sendSMSAlert = builder.create();
                        sendSMSAlert.show();
                        sendSMSAlert.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ButtonGray)));
                        sendSMSAlert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.CYAN);
                    }
                }
            }
            break;
        }
    }

    private void saveFormLocally(String formStatus) {

        String pdName = ApiData.getInstance().getCurrentForm().getPlantDoctor();
        if (!TextUtils.isEmpty(pdName)) {
            ArrayList<String> pdListMetadata = new ArrayList<>();
            for (Metadata md : dcaApplication.getPlantDoctorMetadata()) {
                pdListMetadata.add(md.getName().toUpperCase());
            }
            if (!pdListMetadata.contains(pdName.toUpperCase())) {
                List<RoomDB_PlantDoctor> pdList = ((FormActivity) getActivity()).dcaFormDatabase.dcaPlantDoctorDao().getAllPlantDoctors();
                RoomDB_PlantDoctor pd = new RoomDB_PlantDoctor();
                pd.setPlantDoctorName(pdName);
                if (pdList != null && pdList.size() > 0) {
                    boolean contains = false;
                    for (RoomDB_PlantDoctor item : pdList) {
                        if (item.getPlantDoctorName().toUpperCase().equals(pdName.toUpperCase())) {
                            contains = true;
                            break;
                        }
                    }
                    if (!contains) {
                        pd.setPlantDoctorId(pdList.size() + 1);
                        ((FormActivity) getActivity()).dcaFormDatabase.dcaPlantDoctorDao().addPlantDoctor(pd);
                    }
                } else {
                    pd.setPlantDoctorId(1);
                    ((FormActivity) getActivity()).dcaFormDatabase.dcaPlantDoctorDao().addPlantDoctor(pd);
                }
            }

            SharedPreferences sp = getActivity().getSharedPreferences("FormData", MODE_PRIVATE);
//            Set<String> recentPlantDoctors = sp.getStringSet("Recent_Plant_Doctors", new HashSet<String>());
//            if (recentPlantDoctors != null && recentPlantDoctors.size() > 0) {
//                if (!recentPlantDoctors.contains(pdName)) {
//                    HashSet recentPD = new HashSet(recentPlantDoctors);
//                    if (recentPD != null && recentPD.size() >= 3) {
//                        recentPD.remove(recentPD.iterator().next());
//                    }
//                    recentPlantDoctors.add(pdName);
//                }
//            } else {
//                recentPlantDoctors.add(pdName);
//            }
//            sp.edit().putStringSet("Recent_Plant_Doctor", recentPlantDoctors);
//            sp.edit().commit();

            String recentPD = "";
            if (sp.contains("Recent_Plant_Doctor")) {
                recentPD = sp.getString("Recent_Plant_Doctor", "");
            }
            if (!TextUtils.isEmpty(recentPD)) {
                if (!recentPD.contains(pdName)) {
                    try {
                        JSONArray jArrRecentPD = new JSONArray(recentPD);
                        if (jArrRecentPD.length() >= 3) {
                            jArrRecentPD.put(0, jArrRecentPD.getString(1));
                            jArrRecentPD.put(1, jArrRecentPD.getString(2));
                            jArrRecentPD.put(2, pdName);
                        } else {
                            int index = jArrRecentPD.length();
                            jArrRecentPD.put(index, pdName);
                        }
                        recentPD = jArrRecentPD.toString();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("Recent_Plant_Doctor", recentPD);
                        editor.commit();

                    } catch (JSONException je) {

                    }
                }
            } else {
                JSONArray jArrPDName = new JSONArray();
                jArrPDName.put(pdName);
                recentPD = jArrPDName.toString();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("Recent_Plant_Doctor", recentPD);
                editor.commit();
            }
        }

        String clinicCode = ApiData.getInstance().getCurrentForm().getClinicCode();
        if (!TextUtils.isEmpty(clinicCode)) {
            ArrayList<String> clinicListMetadata = new ArrayList<>();
            for (Metadata md : dcaApplication.getClinicCodeMetadata()) {
                clinicListMetadata.add(md.getName().toUpperCase());
            }

            if (!clinicListMetadata.contains(clinicCode.toUpperCase())) {
                List<RoomDB_ClinicCode> clinicCodeList = ((FormActivity) getActivity()).dcaFormDatabase.dcaClinicCodeDao().getAllClinicCodes();
                RoomDB_ClinicCode clinic = new RoomDB_ClinicCode();
                clinic.setClinicCode(clinicCode);
                if (clinicCodeList != null && clinicCodeList.size() > 0) {
                    boolean contains = false;
                    for (RoomDB_ClinicCode item : clinicCodeList) {
                        if (item.getClinicCode().toUpperCase().equals(clinicCode.toUpperCase())) {
                            contains = true;
                            break;
                        }
                    }
                    if (!contains) {
                        clinic.setClinicCodeId(clinicCodeList.size() + 1);
                        ((FormActivity) getActivity()).dcaFormDatabase.dcaClinicCodeDao().addClinicCode(clinic);
                    }
                } else {
                    clinic.setClinicCodeId(1);
                    ((FormActivity) getActivity()).dcaFormDatabase.dcaClinicCodeDao().addClinicCode(clinic);
                }
            }

            SharedPreferences sp = getActivity().getSharedPreferences("FormData", MODE_PRIVATE);
            String recentCC = "";
            if (sp.contains("Recent_Clinic_Code")) {
                recentCC = sp.getString("Recent_Clinic_Code", "");
            }
            if (!TextUtils.isEmpty(recentCC)) {
                if (!recentCC.contains(clinicCode)) {
                    try {
                        JSONArray jArrRecentCC = new JSONArray(recentCC);
                        if (jArrRecentCC.length() >= 3) {
                            jArrRecentCC.put(0, jArrRecentCC.getString(1));
                            jArrRecentCC.put(1, jArrRecentCC.getString(2));
                            jArrRecentCC.put(2, clinicCode);
                        } else {
                            int index = jArrRecentCC.length();
                            jArrRecentCC.put(index, clinicCode);
                        }
                        recentCC = jArrRecentCC.toString();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("Recent_Clinic_Code", recentCC);
                        editor.commit();

                    } catch (JSONException je) {

                    }
                }
            } else {
                JSONArray jArrCCode = new JSONArray();
                jArrCCode.put(clinicCode);
                recentCC = jArrCCode.toString();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("Recent_Clinic_Code", recentCC);
                editor.commit();
            }
        }

        String farmerName = ApiData.getInstance().getCurrentForm().getFarmerName();
        String farmerPhone = ApiData.getInstance().getCurrentForm().getFarmerPhoneNumber();
        String farmerGender = ApiData.getInstance().getCurrentForm().getFarmerGender();
        String farmerLoc1 = ApiData.getInstance().getCurrentForm().getFarmerLocation1();
        String farmerLoc2 = ApiData.getInstance().getCurrentForm().getFarmerLocation2();
        String farmerLoc3 = ApiData.getInstance().getCurrentForm().getFarmerLocation3();
        if (!TextUtils.isEmpty(farmerName) || !TextUtils.isEmpty(farmerPhone) || !TextUtils.isEmpty(farmerGender)
                || !TextUtils.isEmpty(farmerLoc1) || !TextUtils.isEmpty(farmerLoc2) || !TextUtils.isEmpty(farmerLoc3)) {

            List<RoomDB_FarmerDetails> farmerList = ((FormActivity) getActivity()).dcaFormDatabase.dcaFarmerDao().getAllFarmerDetails();
            RoomDB_FarmerDetails farmerDetails = new RoomDB_FarmerDetails();
            farmerDetails.setFarmerName(farmerName);
            farmerDetails.setFarmerPhoneNumber(farmerPhone);
            farmerDetails.setFarmerGender(farmerGender);
            farmerDetails.setFarmerLocation1(farmerLoc1);
            farmerDetails.setFarmerLocation2(farmerLoc2);
            farmerDetails.setFarmerLocation3(farmerLoc3);
            if (farmerList != null && farmerList.size() > 0) {
                boolean found = false;
                for (RoomDB_FarmerDetails farmerItem : farmerList) {
                    if (farmerItem.getFarmerName().toUpperCase().equals(farmerName.toUpperCase())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    farmerDetails.setFarmerId(farmerList.size() + 1);
                    ((FormActivity) getActivity()).dcaFormDatabase.dcaFarmerDao().addFarmerDetails(farmerDetails);
                }
            } else {
                farmerDetails.setFarmerId(1);
                ((FormActivity) getActivity()).dcaFormDatabase.dcaFarmerDao().addFarmerDetails(farmerDetails);
            }

            SharedPreferences sp = getActivity().getSharedPreferences("FormData", MODE_PRIVATE);
            String recentFarmer = "";
            if (sp.contains("Recent_Farmer")) {
                recentFarmer = sp.getString("Recent_Farmer", "");
            }
            if (!TextUtils.isEmpty(recentFarmer)) {
                if (!recentFarmer.contains(farmerName)) {
                    try {
                        JSONArray jArrRecentFarmer = new JSONArray(recentFarmer);
                        if (jArrRecentFarmer.length() >= 3) {
                            jArrRecentFarmer.put(0, jArrRecentFarmer.getString(1));
                            jArrRecentFarmer.put(1, jArrRecentFarmer.getString(2));
                            jArrRecentFarmer.put(2, farmerName);
                        } else {
                            int index = jArrRecentFarmer.length();
                            jArrRecentFarmer.put(index, pdName);
                        }
                        recentFarmer = jArrRecentFarmer.toString();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("Recent_Farmer", recentFarmer);
                        editor.commit();

                    } catch (JSONException je) {

                    }
                }
            } else {
                JSONArray jArrFarmer = new JSONArray();
                jArrFarmer.put(farmerName);
                recentFarmer = jArrFarmer.toString();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("Recent_Farmer", recentFarmer);
                editor.commit();
            }
        }

        if (!TextUtils.isEmpty(farmerLoc1)) {
            List<RoomDB_Location1> countyList = ((FormActivity) getActivity()).dcaFormDatabase.dcaLocation1Dao().getAllLocation1();
            RoomDB_Location1 county = new RoomDB_Location1();
            county.setLocation1Name(farmerLoc1);
            if (countyList != null && countyList.size() > 0) {
                boolean contains = false;
                for (RoomDB_Location1 item : countyList) {
                    if (item.getLocation1Name().toUpperCase().equals(farmerLoc1.toUpperCase())) {
                        contains = true;
                        break;
                    }
                }
                if (!contains) {
                    county.setLocation1Id(countyList.size() + 1);
                    ((FormActivity) getActivity()).dcaFormDatabase.dcaLocation1Dao().addLocation1(county);
                }
            } else {
                county.setLocation1Id(1);
                ((FormActivity) getActivity()).dcaFormDatabase.dcaLocation1Dao().addLocation1(county);
            }

            SharedPreferences sp = getActivity().getSharedPreferences("FormData", MODE_PRIVATE);
            String recentLoc1 = "";
            if (sp.contains("Recent_Location1")) {
                recentLoc1 = sp.getString("Recent_Location1", "");
            }
            if (!TextUtils.isEmpty(recentLoc1)) {
                if (!recentLoc1.contains(farmerLoc1)) {
                    try {
                        JSONArray jArrRecentLoc1 = new JSONArray(recentLoc1);
                        if (jArrRecentLoc1.length() >= 3) {
                            jArrRecentLoc1.put(0, jArrRecentLoc1.getString(1));
                            jArrRecentLoc1.put(1, jArrRecentLoc1.getString(2));
                            jArrRecentLoc1.put(2, farmerLoc1);
                        } else {
                            int index = jArrRecentLoc1.length();
                            jArrRecentLoc1.put(index, farmerLoc1);
                        }
                        recentLoc1 = jArrRecentLoc1.toString();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("Recent_Location1", recentLoc1);
                        editor.commit();

                    } catch (JSONException je) {

                    }
                }
            } else {
                JSONArray jArrRecentLoc1 = new JSONArray();
                jArrRecentLoc1.put(farmerLoc1);
                recentLoc1 = jArrRecentLoc1.toString();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("Recent_Location1", recentLoc1);
                editor.commit();
            }

            if (!TextUtils.isEmpty(farmerLoc2)) {
                List<RoomDB_Location2> subCountyList = ((FormActivity) getActivity()).dcaFormDatabase.dcaLocation2Dao().getAllLocation2();
                RoomDB_Location2 subCounty = new RoomDB_Location2();
                subCounty.setLocation2Name(farmerLoc2);
                subCounty.setLocation1Name(farmerLoc1);
                if (subCountyList != null && subCountyList.size() > 0) {
                    boolean contains = false;
                    for (RoomDB_Location2 item : subCountyList) {
                        if (item.getLocation2Name().toUpperCase().equals(farmerLoc2.toUpperCase())) {
                            contains = true;
                            break;
                        }
                    }
                    if (!contains) {
                        subCounty.setLocation2Id(subCountyList.size() + 1);
                        ((FormActivity) getActivity()).dcaFormDatabase.dcaLocation2Dao().addLocation2(subCounty);
                    }
                } else {
                    subCounty.setLocation2Id(1);
                    ((FormActivity) getActivity()).dcaFormDatabase.dcaLocation2Dao().addLocation2(subCounty);
                }

//                SharedPreferences sp = getActivity().getSharedPreferences("FormData", MODE_PRIVATE);
                String recentLoc2 = "";
                if (sp.contains("Recent_Location2")) {
                    recentLoc2 = sp.getString("Recent_Location2", "");
                }
                if (!TextUtils.isEmpty(recentLoc2)) {
                    if (!recentLoc2.contains(farmerLoc2)) {
                        try {
                            JSONArray jArrRecentLoc2 = new JSONArray(recentLoc2);
                            if (jArrRecentLoc2.length() >= 3) {
                                jArrRecentLoc2.put(0, jArrRecentLoc2.getString(1));
                                jArrRecentLoc2.put(1, jArrRecentLoc2.getString(2));
                                jArrRecentLoc2.put(2, farmerLoc2);
                            } else {
                                int index = jArrRecentLoc2.length();
                                jArrRecentLoc2.put(index, farmerLoc2);
                            }
                            recentLoc2 = jArrRecentLoc2.toString();
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("Recent_Location2", recentLoc2);
                            editor.commit();

                        } catch (JSONException je) {

                        }
                    }
                } else {
                    JSONArray jArrRecentLoc2 = new JSONArray();
                    jArrRecentLoc2.put(farmerLoc2);
                    recentLoc2 = jArrRecentLoc2.toString();
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("Recent_Location2", recentLoc2);
                    editor.commit();
                }
            }
        }

        String cropName = ApiData.getInstance().getCurrentForm().getCropName();
        String diagnosisName = ApiData.getInstance().getCurrentForm().getDiagnosis();
        if (!TextUtils.isEmpty(cropName)) {
            ArrayList<String> cropListMetadata = new ArrayList<>();
            for (Metadata md : dcaApplication.getCropMetadata()) {
                cropListMetadata.add(md.getName().toUpperCase());
            }

            if (!cropListMetadata.contains(cropName.toUpperCase())) {
                List<RoomDB_Crop> cropList = ((FormActivity) getActivity()).dcaFormDatabase.dcaCropDao().getAllCrops();
                RoomDB_Crop crop = new RoomDB_Crop();
                crop.setCropName(cropName);
                if (cropList != null && cropList.size() > 0) {
                    boolean contains = false;
                    for (RoomDB_Crop item : cropList) {
                        if (item.getCropName().toUpperCase().equals(cropName.toUpperCase())) {
                            contains = true;
                            break;
                        }
                    }
                    if (!contains) {
                        crop.setCropId(cropList.size() + 1);
                        ((FormActivity) getActivity()).dcaFormDatabase.dcaCropDao().addCrop(crop);
                    }
                } else {
                    crop.setCropId(1);
                    ((FormActivity) getActivity()).dcaFormDatabase.dcaCropDao().addCrop(crop);
                }
            }

            SharedPreferences sp = getActivity().getSharedPreferences("FormData", MODE_PRIVATE);
            String recentCrop = "";
            if (sp.contains("Recent_Crop")) {
                recentCrop = sp.getString("Recent_Crop", "");
            }
            if (!TextUtils.isEmpty(recentCrop)) {
                if (!recentCrop.contains(cropName)) {
                    try {
                        JSONArray jArrRecentCrop = new JSONArray(recentCrop);
                        if (jArrRecentCrop.length() >= 3) {
                            jArrRecentCrop.put(0, jArrRecentCrop.getString(1));
                            jArrRecentCrop.put(1, jArrRecentCrop.getString(2));
                            jArrRecentCrop.put(2, cropName);
                        } else {
                            int index = jArrRecentCrop.length();
                            jArrRecentCrop.put(index, cropName);
                        }
                        recentCrop = jArrRecentCrop.toString();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("Recent_Crop", recentCrop);
                        editor.commit();

                    } catch (JSONException je) {

                    }
                }
            } else {
                JSONArray jArrRecentCrop = new JSONArray();
                jArrRecentCrop.put(cropName);
                recentCrop = jArrRecentCrop.toString();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("Recent_Crop", recentCrop);
                editor.commit();
            }

            String cropVarietyName = ApiData.getInstance().getCurrentForm().getCropVariety();
            if (!TextUtils.isEmpty(cropName) && !TextUtils.isEmpty(cropVarietyName)) {
                ArrayList<String> cropVarietyMetadata = new ArrayList<>();
                for (Metadata md : dcaApplication.getCropVarietyMetadata()) {
                    cropVarietyMetadata.add(md.getName().toUpperCase());
                }

                if (!cropVarietyMetadata.contains(cropVarietyName.toUpperCase())) {
                    List<RoomDB_CropVariety> cropVarietyList = ((FormActivity) getActivity()).dcaFormDatabase.dcaCropVarietyDao().getAllCropVarities();
                    RoomDB_CropVariety cropVariety = new RoomDB_CropVariety();
                    cropVariety.setCropName(cropName);
                    cropVariety.setCropVarietyName(cropVarietyName);
                    if (cropVarietyList != null && cropVarietyList.size() > 0) {
                        boolean contains = false;
                        for (RoomDB_CropVariety item : cropVarietyList) {
                            if (item.getCropVarietyName().toUpperCase().equals(cropVarietyName.toUpperCase())) {
                                contains = true;
                                break;
                            }
                        }
                        if (!contains) {
                            cropVariety.setCropVarietyId(cropVarietyList.size() + 1);
                            ((FormActivity) getActivity()).dcaFormDatabase.dcaCropVarietyDao().addCropVariety(cropVariety);
                        }
                    } else {
                        cropVariety.setCropVarietyId(1);
                        ((FormActivity) getActivity()).dcaFormDatabase.dcaCropVarietyDao().addCropVariety(cropVariety);
                    }
                }

//                SharedPreferences sp = getActivity().getSharedPreferences("FormData", MODE_PRIVATE);
                String recentVariety = "";
                if (sp.contains("Recent_CropVariety")) {
                    recentVariety = sp.getString("Recent_CropVariety", "");
                }
                if (!TextUtils.isEmpty(recentVariety)) {
                    if (!recentVariety.contains(cropVarietyName)) {
                        try {
                            JSONArray jArrRecentVariety = new JSONArray(recentVariety);
                            if (jArrRecentVariety.length() >= 3) {
                                jArrRecentVariety.put(0, jArrRecentVariety.getString(1));
                                jArrRecentVariety.put(1, jArrRecentVariety.getString(2));
                                jArrRecentVariety.put(2, cropVarietyName);
                            } else {
                                int index = jArrRecentVariety.length();
                                jArrRecentVariety.put(index, cropVarietyName);
                            }
                            recentVariety = jArrRecentVariety.toString();
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("Recent_CropVariety", recentVariety);
                            editor.commit();

                        } catch (JSONException je) {

                        }
                    }
                } else {
                    JSONArray jArrRecentVariety = new JSONArray();
                    jArrRecentVariety.put(cropVarietyName);
                    recentVariety = jArrRecentVariety.toString();
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("Recent_CropVariety", recentVariety);
                    editor.commit();
                }
            }

            if (!TextUtils.isEmpty(diagnosisName)) {
                ArrayList<String> diagnosisListMetadata = new ArrayList<>();
                for (Metadata md : dcaApplication.getDiagnosisMetadata()) {
                    diagnosisListMetadata.add(md.getName());
                }

                if (!diagnosisListMetadata.contains(diagnosisName)) {
                    List<RoomDB_Diagnosis> diagnosisList = ((FormActivity) getActivity()).dcaFormDatabase.dcaDiagnosisDao().getAllDiagnosis();
                    RoomDB_Diagnosis diagnosis = new RoomDB_Diagnosis();
                    diagnosis.setDiagnosis(diagnosisName);
                    diagnosis.setCropName(cropName);
                    if (diagnosisList != null && diagnosisList.size() > 0) {
                        boolean contains = false;
                        for (RoomDB_Diagnosis item : diagnosisList) {
                            if (item.getDiagnosis().toUpperCase().equals(diagnosisName.toUpperCase())) {
                                contains = true;
                                break;
                            }
                        }
                        if (!contains) {
                            diagnosis.setDiagnosisId(diagnosisList.size() + 1);
                            ((FormActivity) getActivity()).dcaFormDatabase.dcaDiagnosisDao().addDiagnosis(diagnosis);
                        }
                    } else {
                        diagnosis.setDiagnosisId(1);
                        ((FormActivity) getActivity()).dcaFormDatabase.dcaDiagnosisDao().addDiagnosis(diagnosis);
                    }
                }

//                SharedPreferences sp = getActivity().getSharedPreferences("FormData", MODE_PRIVATE);
                String recentDiagnosis = "";
                if (sp.contains("Recent_Diagnosis")) {
                    recentDiagnosis = sp.getString("Recent_Diagnosis", "");
                }
                if (!TextUtils.isEmpty(recentDiagnosis)) {
                    if (!recentDiagnosis.contains(diagnosisName)) {
                        try {
                            JSONArray jArrRecentDiagnosis = new JSONArray(recentDiagnosis);
                            if (jArrRecentDiagnosis.length() >= 3) {
                                jArrRecentDiagnosis.put(0, jArrRecentDiagnosis.getString(1));
                                jArrRecentDiagnosis.put(1, jArrRecentDiagnosis.getString(2));
                                jArrRecentDiagnosis.put(2, diagnosisName);
                            } else {
                                int index = jArrRecentDiagnosis.length();
                                jArrRecentDiagnosis.put(index, diagnosisName);
                            }
                            recentDiagnosis = jArrRecentDiagnosis.toString();
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("Recent_Diagnosis", recentDiagnosis);
                            editor.commit();

                        } catch (JSONException je) {

                        }
                    }
                } else {
                    JSONArray jArrRecentDiagnosis = new JSONArray();
                    jArrRecentDiagnosis.put(diagnosisName);
                    recentDiagnosis = jArrRecentDiagnosis.toString();
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("Recent_Diagnosis", recentDiagnosis);
                    editor.commit();
                }
            }
        }

        String recomNow = ApiData.getInstance().getCurrentForm().getRecommendationToManageNow();
        String recomNowAltLang = ApiData.getInstance().getCurrentForm().getRecommendationToManageNowAltLang();
        String recomFuture = ApiData.getInstance().getCurrentForm().getRecommendationForFuturePrevention();
        String recomFutureAltLang = ApiData.getInstance().getCurrentForm().getRecommendationForFuturePreventionAltLang();
        if (!TextUtils.isEmpty(recomNow) || !TextUtils.isEmpty(recomNowAltLang) || !TextUtils.isEmpty(recomFuture) || !TextUtils.isEmpty(recomFutureAltLang)) {

            List<RoomDB_Recommendations> recommendationsList = ((FormActivity) getActivity()).dcaFormDatabase.dcaRecommendationDao().getAllRecommendations();
            RoomDB_Recommendations recomendation = new RoomDB_Recommendations();
            recomendation.setRecommendationToManageNow(recomNow);
            recomendation.setRecommendationToManageNowAltLang(recomNowAltLang);
            recomendation.setRecommendationForFuturePrevention(recomFuture);
            recomendation.setRecommendationForFuturePreventionAltLang(recomFutureAltLang);
            recomendation.setCrop(cropName);
            recomendation.setDiagnosis(diagnosisName);
            recomendation.setLastUsed(Utility.getUTCDateTime(new Date()));
            if (recommendationsList != null && recommendationsList.size() > 0) {
                boolean contains = false;
                for (RoomDB_Recommendations item : recommendationsList) {
                    if (item.getRecommendationToManageNow().toUpperCase().equals(recomNow.toUpperCase()) ||
                            item.getRecommendationToManageNowAltLang().toUpperCase().equals(recomNowAltLang.toUpperCase())) {
                        contains = true;
                        break;
                    }
                }
                if (!contains) {
                    recomendation.setRecommendationId(recommendationsList.size() + 1);
                    ((FormActivity) getActivity()).dcaFormDatabase.dcaRecommendationDao().addRecommedations(recomendation);
                }
            } else {
                recomendation.setRecommendationId(1);
                ((FormActivity) getActivity()).dcaFormDatabase.dcaRecommendationDao().addRecommedations(recomendation);
            }


            SharedPreferences sp = getActivity().getSharedPreferences("FormData", MODE_PRIVATE);
            String recentRecommendations = "";
            if (sp.contains("Recent_Recommendations")) {
                recentRecommendations = sp.getString("Recent_Recommendations", "");
            }
            if (!TextUtils.isEmpty(recentRecommendations)) {
                if (!recentRecommendations.contains(recomNow)) {
                    try {
                        JSONArray jArrRecentRecommendations = new JSONArray(recentRecommendations);
                        if (jArrRecentRecommendations.length() >= 3) {
                            jArrRecentRecommendations.put(0, jArrRecentRecommendations.getString(1));
                            jArrRecentRecommendations.put(1, jArrRecentRecommendations.getString(2));
                            jArrRecentRecommendations.put(2, recomNow);
                        } else {
                            int index = jArrRecentRecommendations.length();
                            jArrRecentRecommendations.put(index, recomNow);
                        }
                        recentRecommendations = jArrRecentRecommendations.toString();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("Recent_Recommendations", recentRecommendations);
                        editor.commit();

                    } catch (JSONException je) {

                    }
                }
            } else {
                JSONArray jArrRecentRecommendation = new JSONArray();
                jArrRecentRecommendation.put(recomNow);
                recentRecommendations = jArrRecentRecommendation.toString();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("Recent_Recommendations", recentRecommendations);
                editor.commit();
            }
        }

        if (TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getFormId())) {
            ApiData.getInstance().getCurrentForm().setFormId(Utility.getGuid());

            if (TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getFormStatus()) ||
                    ApiData.getInstance().getCurrentForm().getFormStatus().toUpperCase().equals("NEW") ||
                    ApiData.getInstance().getCurrentForm().getFormStatus().toUpperCase().equals("CONTINUE")) {
                ApiData.getInstance().getCurrentForm().setFormStatus(formStatus);
            }

            if (ApiData.getInstance().getCurrentSession() != null) {
                ApiData.getInstance().getCurrentForm().setSessionId(ApiData.getInstance().getCurrentSession().getSessionId());
                ApiData.getInstance().getCurrentSession().setClinic(ApiData.getInstance().getCurrentForm().getClinicCode());
                if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getFarmerGender())) {
                    switch (ApiData.getInstance().getCurrentForm().getFarmerGender().toUpperCase()) {
                        case "FEMALE": {
                            ApiData.getInstance().getCurrentSession().setWomen(ApiData.getInstance().getCurrentSession().getWomen() + 1);
                            break;
                        }
                        case "MALE": {
                            ApiData.getInstance().getCurrentSession().setMen(ApiData.getInstance().getCurrentSession().getMen() + 1);
                            break;
                        }
                    }
                }
//                ApiData.getInstance().getCurrentSession().setMen(ApiData.getInstance().getCurrentSession().getMen() + 1);
                if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getFormStatus())) {
                    switch (ApiData.getInstance().getCurrentForm().getFormStatus().toUpperCase()) {
                        case "NOTSENT": {
//                            ApiData.getInstance().getCurrentSession().setDraft(ApiData.getInstance().getCurrentSession().getSubmitted() + 1);
                            break;
                        }
                        case "DRAFT": {
//                            ApiData.getInstance().getCurrentSession().setDraft(ApiData.getInstance().getCurrentSession().getDraft() + 1);
                            break;
                        }
                    }
                }
                ApiData.getInstance().getCurrentForm().setFormEndDateTime(Utility.getUTCDateTime(new Date()));
                ((FormActivity) getActivity()).dcaFormDatabase.dcaFormDao().addForm(ApiData.getInstance().getCurrentForm());
            }
        } else {
            ApiData.getInstance().getCurrentForm().setFormStatus(formStatus);
            ApiData.getInstance().getCurrentForm().setFormEndDateTime(Utility.getUTCDateTime(new Date()));
            ((FormActivity) getActivity()).dcaFormDatabase.dcaFormDao().updateform(ApiData.getInstance().getCurrentForm());
        }

        Toast.makeText(getActivity(), "Form saved successfully in drafts", Toast.LENGTH_SHORT).show();
    }

    private void submitForm() {

        String postFormUrl = UrlFactory.postForms;
        if (headers == null) {
            headers = new HashMap<>();
            headers.put("jwt", ApiData.getInstance().getJwtToken());
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("FormId", ApiData.getInstance().getCurrentForm().getFormId());
        params.put("FormDefinitionId", ApiData.getInstance().getFormDefinition().getFormDefinitionId());
        params.put("ConsultantId", "");
        params.put("SessionId", ApiData.getInstance().getCurrentSession().getSessionId());
        params.put("ClinicCode", ApiData.getInstance().getCurrentForm().getClinicCode());

//        {
//            "FormId": "9910c220-c9a8-5542-9eb8-6246d52b1198",
//                "FormDefinitionId": "370f4ad2-4b76-4580-85d6-d462d252cf9b",
//                "ConsultantId": "",
//                "SessionId": "538b13ed-75a3-b5d5-e50d-3f280299000d",
//                "ClinicCode": "Training",
//                "Crop": "Soybeans / சோயா மொச்சை",
//                "Diagnosis": "Diagnosis on soybeans",
//                "CompletedDateTime": "11 January 2019 - 19:12",
//                "Language": "en-GB",
//                "Contact": {
//            "FirstName": "Farm2",
//                    "FamilyName": "",
//                    "Telephone": "0123456789",
//                    "Gender": "M",
//                    "Age": "",
//                    "OtherContact": "",
//                    "Address": {
//                "Address1": "",
//                        "Address2": "",
//                        "Address3": "",
//                        "Area": "",
//                        "Town": "",
//                        "Province": "",
//                        "Country": "IN-30004",
//                        "PostalCode": ""
//            }
//        },
//            "Fields": [{
//            "FieldDefinitionId": "949d3533-a876-4e6f-9213-d5facdd5707f",
//                    "Values": [{
//                "Value": "Devinder Singh"
//            }]
//        }, {
//            "FieldDefinitionId": "d9037ef2-b1b9-482b-a0fd-c633761fe164",
//                    "Values": [{
//                "Value": "Training"
//            }]
//        }, {
//            "FieldDefinitionId": "34184475-5d5c-4748-abf4-b03ba2626f67",
//                    "Values": [{
//                "Value": "Soybeans / சோயா மொச்சை"
//            }]
//        }, {
//            "FieldDefinitionId": "9fa7a97d-2d22-4aa5-a860-8f3b1466c492",
//                    "Values": [{
//                "Value": "9305"
//            }]
//        }, {
//            "FieldDefinitionId": "7f9a9110-2e20-471b-8a53-f6efa60998d9",
//                    "Values": [{
//                "Value": "Yes"
//            }]
//        }, {
//            "FieldDefinitionId": "79c3b029-81df-43b9-aef0-41e6117a371b",
//                    "Values": [{
//                "Value": "Flowering"
//            }, {
//                "Value": "Fruiting"
//            }]
//        }, {
//            "FieldDefinitionId": "e81bed0e-2dc6-4ea6-9706-02bf527e6768",
//                    "Values": [{
//                "Value": "Flower"
//            }, {
//                "Value": "Leaves"
//            }]
//        }, {
//            "FieldDefinitionId": "aa9002b2-ec20-4a92-a362-45a8d21ac3a9",
//                    "Values": [{
//                "Value": "2017"
//            }]
//        }, {
//            "FieldDefinitionId": "6cecfccb-9b33-4511-a0b3-6349575a6f23",
//                    "Values": [{
//                "Value": "6"
//            }]
//        }, {
//            "FieldDefinitionId": "b505ddad-76b7-492b-be78-6cf8668eef16",
//                    "Values": [{
//                "Value": "Hectares"
//            }]
//        }, {
//            "FieldDefinitionId": "c8983661-f5bf-4096-bf24-c4c2609c1bd7",
//                    "Values": [{
//                "Value": "25%"
//            }]
//        }, {
//            "FieldDefinitionId": "01ac66f8-7b01-4b6b-9114-4159715ff2d5",
//                    "Values": [{
//                "Value": "Bore holes"
//            }, {
//                "Value": "Distorted"
//            }]
//        }, {
//            "FieldDefinitionId": "8a01a96b-e415-4781-ae83-2f45ab876449",
//                    "Values": [{
//                "Value": "Linear"
//            }]
//        }, {
//            "FieldDefinitionId": "acbdc41a-cdd2-4fe6-905b-04f46066eb17",
//                    "Values": [{
//                "Value": "Description problem"
//            }]
//        }, {
//            "FieldDefinitionId": "668ddada-cc5e-4bcb-b77b-c93ae556a42a",
//                    "Values": [{
//                "Value": "Fungus"
//            }]
//        }, {
//            "FieldDefinitionId": "8442ce16-c797-4ab7-abcc-4361ec9628e2",
//                    "Values": [{
//                "Value": "Diagnosis on soybeans"
//            }]
//        }, {
//            "FieldDefinitionId": "4da63be0-4f52-4881-9435-a84fcc81de95",
//                    "Values": [{
//                "Value": "Yes"
//            }]
//        }, {
//            "FieldDefinitionId": "31f21bc0-8d3b-4c03-b0e2-9b302f2ec198",
//                    "Values": [{
//                "Value": "Current control of soybeans"
//            }]
//        }, {
//            "FieldDefinitionId": "3301318a-555f-493f-aa14-93bd31781fd0",
//                    "Values": [{
//                "Value": "Herbicide"
//            }]
//        }, {
//            "FieldDefinitionId": "feaa3108-a6c1-42c2-9071-44f9247c417d",
//                    "Values": [{
//                "Value": "Diagnosis on soybeans - Soybeans / சோயா மொச்சை "
//            }]
//        }, {
//            "FieldDefinitionId": "8c9527b7-0495-414f-8ed5-f899f3f472dd",
//                    "Values": [{
//                "Value": "Diagnosis on soybeans - Soybeans / சோயா மொச்சை "
//            }]
//        }, {
//            "FieldDefinitionId": "992cece7-3b10-4955-93ae-d9cce71dd75f",
//                    "Values": [{
//                "Value": "Yes"
//            }]
//        }, {
//            "FieldDefinitionId": "7a254d63-4baf-4d07-bac1-9ec3a6f07730",
//                    "Values": [{
//                "Value": "Yes"
//            }]
//        }, {
//            "FieldDefinitionId": "900174e2-d43f-49c5-bf0a-93355ff0b784",
//                    "Values": [{
//                "Value": "No"
//            }]
//        }],
//            "StartDateTime": "11 January 2019 - 19:11",
//                "PublishedDateTime": "11 January 2019 - 19:13",
//                "IsPublished": 1,
//                "FormImages": [{
//            "Section": "SectionSampleInformationSample",
//                    "Image": "data:image/jpeg;base64,iVCC"
//        }],
//            "Latitude": 28.6265432,
//                "Longitude": 77.1610995,
//                "Accuracy": 25.770000457763672
//        }


        makePOSTRequest(postFormUrl, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {

            }

            @Override
            public void onError(String result) throws Exception {

            }
        }, headers, null, "POST FORM");
    }

    private void makePOSTRequest(final String url, final VolleyCallback callback, final HashMap<String, String> headers, final HashMap<String, String> params, String tag) {
        CustomJSONObjectRequest req = new CustomJSONObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response != null) {
                                callback.onSuccess(response);
                                Log.v("Response", response.toString());
                            } else if (response == null && url.contains("auth/savepasscode")) {
                                callback.onSuccess(response);
                            } else {
                                callback.onError("Null response from server");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("Response", error.toString());
                        String err = null;
                        if (error instanceof NoConnectionError) {
                            err = "No Internet access";
                        }
                        try {
                            if (err != null) {
                                callback.onError(err);
                            } else {
                                callback.onError(error.toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        VolleyController.getInstance(getActivity()).addToRequestQueue(req, tag);
    }
}