package org.cabi.pdc.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.cabi.pdc.R;
import org.cabi.pdc.activities.FormActivity;
import org.cabi.pdc.common.ApiData;
import org.cabi.pdc.common.DCAApplication;
import org.cabi.pdc.common.Utility;
import org.cabi.pdc.models.Section;

import static android.widget.LinearLayout.VERTICAL;

public class PlannedActivitiesFragment extends Fragment {

    private Context mContext;
    DCAApplication dcaApplication;

    public PlannedActivitiesFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.mContext = getActivity();
        dcaApplication = (DCAApplication) getActivity().getApplication();

        View rootView = inflater.inflate(R.layout.fragment_planned_activities, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            final int sectionPos = bundle.getInt("SectionPosition", -1);
            final int fragmentId = this.getId();
            final boolean fromSummary = bundle.getBoolean("FromSummary");
            Section section = (Section) bundle.getSerializable("Section");
            if (section.getFields() != null && section.getFields().size() > 0) {

                LinearLayout baseLayout = rootView.findViewById(R.id.baseLayoutPlannedActivities);

                for (int i = 0; i < section.getFields().size(); i++) {
                    switch (section.getFields().get(i).getFieldType()) {
                        case 2: {

                            LinearLayout.LayoutParams tvLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            tvLayoutParams.bottomMargin = Utility.getDpFromPixels(mContext, 10);
                            tvLayoutParams.leftMargin = Utility.getDpFromPixels(mContext, 20);
                            if (i != 0) {
                                tvLayoutParams.topMargin = Utility.getDpFromPixels(mContext, 15);
                            }
                            TextView tvBaseTitle = new TextView(mContext);
                            tvBaseTitle.setLayoutParams(tvLayoutParams);
                            tvBaseTitle.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());
                            tvBaseTitle.setTextColor(Color.BLACK);
                            tvBaseTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                            baseLayout.addView(tvBaseTitle);

                            if (section.getFields().get(i).getOptions() != null && section.getFields().get(i).getOptions().size() > 0) {

                                LinearLayout.LayoutParams rgroupLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                rgroupLayoutParams.leftMargin = Utility.getDpFromPixels(mContext, 20);
                                if (i == section.getFields().size() - 2) {
                                    rgroupLayoutParams.bottomMargin = Utility.getDpFromPixels(mContext, 30);
                                }
                                RadioGroup rbGrpPlannedActivities = new RadioGroup(mContext);
                                rbGrpPlannedActivities.setLayoutParams(rgroupLayoutParams);
                                rbGrpPlannedActivities.setOrientation(VERTICAL);

                                for (int j = 0; j < section.getFields().get(i).getOptions().size(); j++) {

                                    RadioButton rbPlannedActivitiesOption = (RadioButton) inflater.inflate(R.layout.generic_radio_button, null);
                                    RadioGroup.LayoutParams layoutParamsRB = new RadioGroup.LayoutParams(Utility.getDpFromPixels(mContext, 340), ViewGroup.LayoutParams.WRAP_CONTENT);
                                    if (j != 0) {
                                        layoutParamsRB.topMargin = Utility.getDpFromPixels(mContext, 10);
                                    }
                                    rbPlannedActivitiesOption.setLayoutParams(layoutParamsRB);
                                    rbPlannedActivitiesOption.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getOptions().get(j).getTranslationId()).getValue());
                                    rbPlannedActivitiesOption.setTag(section.getFields().get(i).getTranslationId() + "##" + section.getFields().get(i).getOptions().get(j).getValue());
                                    rbPlannedActivitiesOption.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            if (ApiData.getInstance().getCurrentForm() != null) {
                                                String[] selectedRB = view.getTag().toString().split("##");
                                                if (selectedRB[0].toUpperCase().equals("FACTSHEETGIVEN")) {
                                                    ApiData.getInstance().getCurrentForm().setFactsheetGiven(selectedRB[1]);
                                                } else if (selectedRB[0].toUpperCase().equals("SAMPLESENTTOLAB")) {
                                                    ApiData.getInstance().getCurrentForm().setSampleSentToLab(selectedRB[1]);
                                                } else if (selectedRB[0].toUpperCase().equals("FIELDVISITARRANGED")) {
                                                    ApiData.getInstance().getCurrentForm().setFieldVisitArranged(selectedRB[1]);
                                                }
                                            }
//                                            Toast.makeText(mContext, "Selected:" + view.getTag(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    rbGrpPlannedActivities.addView(rbPlannedActivitiesOption);

                                    if (ApiData.getInstance().getCurrentForm() != null) {

                                        switch (section.getFields().get(i).getTranslationId().toUpperCase()) {
                                            case "FACTSHEETGIVEN": {
                                                String factsheetGiven = ApiData.getInstance().getCurrentForm().getFactsheetGiven();
                                                if (!TextUtils.isEmpty(factsheetGiven)) {
                                                    for (int k = 0; k < rbGrpPlannedActivities.getChildCount(); k++) {
                                                        RadioButton rbutton = (RadioButton) rbGrpPlannedActivities.getChildAt(k);
                                                        if (rbutton.getText().toString().toUpperCase().equals(factsheetGiven.toUpperCase())) {
                                                            rbutton.setChecked(true);
                                                        }
                                                    }
                                                }
                                            }
                                            break;
                                            case "SAMPLESENTTOLAB": {
                                                String sampleSentToLab = ApiData.getInstance().getCurrentForm().getSampleSentToLab();
                                                if (!TextUtils.isEmpty(sampleSentToLab)) {
                                                    for (int k = 0; k < rbGrpPlannedActivities.getChildCount(); k++) {
                                                        RadioButton rbutton = (RadioButton) rbGrpPlannedActivities.getChildAt(k);
                                                        if (rbutton.getText().toString().toUpperCase().equals(sampleSentToLab.toUpperCase())) {
                                                            rbutton.setChecked(true);
                                                        }
                                                    }
                                                }
                                            }
                                            break;
                                            case "FIELDVISITARRANGED": {
                                                String fieldVisitArranged = ApiData.getInstance().getCurrentForm().getFieldVisitArranged();
                                                if (!TextUtils.isEmpty(fieldVisitArranged)) {
                                                    for (int k = 0; k < rbGrpPlannedActivities.getChildCount(); k++) {
                                                        RadioButton rbutton = (RadioButton) rbGrpPlannedActivities.getChildAt(k);
                                                        if (rbutton.getText().toString().toUpperCase().equals(fieldVisitArranged.toUpperCase())) {
                                                            rbutton.setChecked(true);
                                                        }
                                                    }
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                                baseLayout.addView(rbGrpPlannedActivities);
                            }
                        }
                        break;
                        case 27: {
                            LinearLayout btnNextUpdate = (LinearLayout) inflater.inflate(R.layout.generic_section_next_button, null);
                            TextView tvNextButton = btnNextUpdate.findViewById(R.id.tvButtonNextUpdate);
                            String strNextBtn = "Update";
                            if (ApiData.getInstance().getMetadataTranslationsList() != null) {
                                if (fromSummary) {
                                    if (ApiData.getInstance().getMetadataTranslationsList().containsKey("SelectUpdate")) {
                                        strNextBtn = ApiData.getInstance().getMetadataTranslationsList().get("SelectUpdate").getValue();
                                    }
                                } else {
                                    strNextBtn = ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue();
                                }
                            }
                            tvNextButton.setText(strNextBtn);
                            RelativeLayout btnNextSection = btnNextUpdate.findViewById(R.id.relLayoutButtonNext);
                            btnNextSection.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (sectionPos > -1) {
                                        if (fromSummary) {
                                            ((FormActivity) getActivity()).loadNextSection(dcaApplication.getSectionList().size() + 1, fragmentId, true);

                                        } else {
                                            ((FormActivity) getActivity()).loadNextSection(sectionPos + 1, fragmentId, false);
                                        }
                                    }
                                }
                            });
                            baseLayout.addView(btnNextUpdate);
                        }
                        break;
                    }
                }
            }
        }

        return rootView;
    }
}