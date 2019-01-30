package org.cabi.pdc.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.cabi.pdc.R;
import org.cabi.pdc.activities.FormActivity;
import org.cabi.pdc.common.ApiData;
import org.cabi.pdc.common.DCAApplication;
import org.cabi.pdc.common.Utility;
import org.cabi.pdc.models.Section;

import java.util.ArrayList;

public class AreaAffectedFragment extends Fragment {

    private Spinner ddlYearFirstSeen;
    private Context mContext;
    private String yearSelected;
    DCAApplication dcaApplication;
    EditText etAreaPlanted;

    public AreaAffectedFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        dcaApplication = (DCAApplication) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_area_affected, container, false);

        etAreaPlanted = rootView.findViewById(R.id.etAreaPlanted);
        ddlYearFirstSeen = rootView.findViewById(R.id.ddlYearFirstSeen);

        Bundle bundle = getArguments();
        if (bundle != null) {
            final int sectionPos = bundle.getInt("SectionPosition", -1);
            final int fragmentId = this.getId();
            final boolean fromSummary = bundle.getBoolean("FromSummary");
            Section section = (Section) bundle.getSerializable("Section");

            if (section != null && section.getFields() != null && section.getFields().size() > 0) {
                for (int i = 0; i < section.getFields().size(); i++) {
                    switch (section.getFields().get(i).getFieldType()) {
                        case 8: {
                            TextView tvYearFirstSeen = rootView.findViewById(R.id.baseTitleYearFirstSeen);
                            tvYearFirstSeen.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());

                            if (section.getFields().get(i).getOptions() != null && section.getFields().get(i).getOptions().size() > 0) {

                                ArrayList<String> listYearFirstSeen = new ArrayList<String>();
                                for (int j = 0; j < section.getFields().get(i).getOptions().size(); j++) {
                                    if (section.getFields().get(i).getOptions().get(j).getTranslationId() != null) {
                                        listYearFirstSeen.add(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getOptions().get(j).getTranslationId()).getValue());
                                    } else {
                                        listYearFirstSeen.add(section.getFields().get(i).getOptions().get(j).getName());
                                    }
                                }

                                if (listYearFirstSeen != null && listYearFirstSeen.size() > 0) {
                                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.generic_spinner_item, listYearFirstSeen);
                                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    ddlYearFirstSeen.setAdapter(dataAdapter);
                                    ddlYearFirstSeen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                                            yearSelected = ((Spinner) adapterView).getSelectedItem().toString();
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });

                                    if (ApiData.getInstance().getCurrentForm() != null && !TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getYearFirstSeen())) {
                                        if (listYearFirstSeen.contains(ApiData.getInstance().getCurrentForm().getYearFirstSeen())) {
                                            ddlYearFirstSeen.setSelection(listYearFirstSeen.indexOf(ApiData.getInstance().getCurrentForm().getYearFirstSeen()));
                                        } else {
                                            ddlYearFirstSeen.setSelection(0);
                                        }
                                    }
                                }
                            }
                        }
                        break;
                        case 1: {
                            TextView tvAreaPlanted = rootView.findViewById(R.id.baseTitleAreaPlanted);
                            tvAreaPlanted.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());
                            if (ApiData.getInstance().getCurrentForm() != null && !TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getAreaPlanted())) {
                                etAreaPlanted.setText(ApiData.getInstance().getCurrentForm().getAreaPlanted());
                            }

                        }
                        break;
                        case 9: {
                            if (section.getFields().get(i).getTemplate().toUpperCase().equals("AREA")) {
                                TextView tvAreaPlantedUnit = rootView.findViewById(R.id.baseTitleAreaPlantedUnit);
                                tvAreaPlantedUnit.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());

                                RadioGroup rbGrpAreaPlantedUnit = rootView.findViewById(R.id.rbgrpAreaPlantedUnit);
                                if (section.getFields().get(i).getOptions() != null && section.getFields().get(i).getOptions().size() > 0) {
                                    for (int j = 0; j < section.getFields().get(i).getOptions().size(); j++) {

                                        RadioButton rbAreaPlantedUnit = (RadioButton) inflater.inflate(R.layout.generic_radio_button, null);
                                        RadioGroup.LayoutParams layoutParamsRB = new RadioGroup.LayoutParams(Utility.getDpFromPixels(mContext, 340), ViewGroup.LayoutParams.WRAP_CONTENT);
                                        if (j != 0) {
                                            layoutParamsRB.topMargin = Utility.getDpFromPixels(mContext, 10);
                                        }
                                        rbAreaPlantedUnit.setLayoutParams(layoutParamsRB);
                                        rbAreaPlantedUnit.setId(200 + j);
                                        rbAreaPlantedUnit.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getOptions().get(j).getTranslationId()).getValue());
//                                        rbAreaPlantedUnit.setTag(section.getFields().get(i).getOptions().get(j).getValue());
                                        rbAreaPlantedUnit.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                            }
                                        });
                                        rbGrpAreaPlantedUnit.addView(rbAreaPlantedUnit);
                                    }

                                    if (ApiData.getInstance().getCurrentForm() != null) {
                                        String areaPlantedUnit = ApiData.getInstance().getCurrentForm().getAreaPlantedUnit();
                                        if (!TextUtils.isEmpty(areaPlantedUnit)) {
                                            for (int k = 0; k < rbGrpAreaPlantedUnit.getChildCount(); k++) {
                                                RadioButton rbutton = (RadioButton) rbGrpAreaPlantedUnit.getChildAt(k);
                                                if (rbutton.getText().toString().toUpperCase().equals(areaPlantedUnit.toUpperCase())) {
                                                    rbutton.setChecked(true);
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if (section.getFields().get(i).getTemplate().toUpperCase().equals("PERCENTAGES")) {
                                TextView tvPercentCropAffected = rootView.findViewById(R.id.baseTitlePercentCropAffected);
                                tvPercentCropAffected.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());

                                RadioGroup rbGrpPercentCropAffected = rootView.findViewById(R.id.rbgrpPercentCropAffected);
                                if (section.getFields().get(i).getOptions() != null && section.getFields().get(i).getOptions().size() > 0) {
                                    for (int j = 0; j < section.getFields().get(i).getOptions().size(); j++) {

                                        RadioButton rbPercentCropAffected = (RadioButton) inflater.inflate(R.layout.generic_radio_button, null);
                                        RadioGroup.LayoutParams layoutParamsRB = new RadioGroup.LayoutParams(Utility.getDpFromPixels(mContext, 340), ViewGroup.LayoutParams.WRAP_CONTENT);
                                        if (j != 0) {
                                            layoutParamsRB.topMargin = Utility.getDpFromPixels(mContext, 10);
                                        }
                                        rbPercentCropAffected.setLayoutParams(layoutParamsRB);
                                        rbPercentCropAffected.setId(300 + j);
                                        rbPercentCropAffected.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getOptions().get(j).getTranslationId()).getValue());
//                                        rbPercentCropAffected.setTag(section.getFields().get(i).getOptions().get(j).getValue());
                                        rbPercentCropAffected.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                            }
                                        });
                                        rbGrpPercentCropAffected.addView(rbPercentCropAffected);
                                    }

                                    if (ApiData.getInstance().getCurrentForm() != null) {
                                        String perCropAffect = ApiData.getInstance().getCurrentForm().getPercentOfCropAffected();
                                        if (!TextUtils.isEmpty(perCropAffect)) {
                                            for (int k = 0; k < rbGrpPercentCropAffected.getChildCount(); k++) {
                                                RadioButton rbutton = (RadioButton) rbGrpPercentCropAffected.getChildAt(k);
                                                if (rbutton.getText().toString().toUpperCase().equals(perCropAffect.toUpperCase())) {
                                                    rbutton.setChecked(true);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                        case 10: {
                            LinearLayout baseLayout = rootView.findViewById(R.id.baseLayoutAreaAffected);
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
                                        if (ApiData.getInstance().getCurrentForm() != null) {

                                            String areaPlanted = etAreaPlanted.getText().toString().trim();
                                            ApiData.getInstance().getCurrentForm().setAreaPlanted(areaPlanted);

                                            RadioGroup areaPlantedUnit = (RadioGroup) getView().findViewById(R.id.rbgrpAreaPlantedUnit);
                                            int selectedId = areaPlantedUnit.getCheckedRadioButtonId();
                                            RadioButton selectedAreaPlantedUnit = (RadioButton) getView().findViewById(selectedId);
                                            if (selectedAreaPlantedUnit != null) {
                                                String areaPU = selectedAreaPlantedUnit.getText().toString();
                                                ApiData.getInstance().getCurrentForm().setAreaPlantedUnit(areaPU);
                                            }

                                            RadioGroup percentCropAffect = (RadioGroup) getView().findViewById(R.id.rbgrpPercentCropAffected);
                                            int checkedVal = percentCropAffect.getCheckedRadioButtonId();
                                            RadioButton checkedPCA = (RadioButton) getView().findViewById(checkedVal);
                                            if (checkedPCA != null) {
                                                String pCA = checkedPCA.getText().toString();
                                                ApiData.getInstance().getCurrentForm().setPercentOfCropAffected(pCA);
                                            }

                                            if (ApiData.getInstance().getCurrentForm() != null && !TextUtils.isEmpty(yearSelected)) {
                                                ApiData.getInstance().getCurrentForm().setYearFirstSeen(yearSelected);
                                            }
                                        }
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