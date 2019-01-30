package org.cabi.pdc.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
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

public class DiagnosisProblemTypeFragment extends Fragment {

    private Context mContext;
    DCAApplication dcaApplication;

    public DiagnosisProblemTypeFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        dcaApplication = (DCAApplication) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_diagnosis_problem_type, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            final int sectionPos = bundle.getInt("SectionPosition", -1);
            final int fragmentId = this.getId();
            final boolean fromSummary = bundle.getBoolean("FromSummary");
            Section section = (Section) bundle.getSerializable("Section");

            if (section.getFields() != null && section.getFields().size() > 0) {

                for (int i = 0; i < section.getFields().size(); i++) {
                    switch (section.getFields().get(i).getFieldType()) {
                        case 9: {
                            TextView tvBaseTitle = rootView.findViewById(R.id.baseTitleDiagnosisProblemType);
                            tvBaseTitle.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());

                            final RadioGroup rbGrpDiagnosisProblemType = rootView.findViewById(R.id.rbgrpDiagnosisProblemType);
                            if (section.getFields().get(i).getOptions() != null && section.getFields().get(i).getOptions().size() > 0) {
                                for (int j = 0; j < section.getFields().get(i).getOptions().size(); j++) {

                                    RadioButton rbDiagnosisProblemType = (RadioButton) inflater.inflate(R.layout.generic_radio_button, null);
                                    RadioGroup.LayoutParams layoutParamsRB = new RadioGroup.LayoutParams(Utility.getDpFromPixels(mContext, 340), ViewGroup.LayoutParams.WRAP_CONTENT);
                                    if (j != 0) {
                                        layoutParamsRB.topMargin = Utility.getDpFromPixels(mContext, 10);
                                    }
                                    rbDiagnosisProblemType.setLayoutParams(layoutParamsRB);
                                    rbDiagnosisProblemType.setId(500 + j);
                                    rbDiagnosisProblemType.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getOptions().get(j).getTranslationId()).getValue());
                                    rbDiagnosisProblemType.setTag(section.getFields().get(i).getOptions().get(j).getValue());
                                    rbDiagnosisProblemType.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                        }
                                    });
                                    rbGrpDiagnosisProblemType.addView(rbDiagnosisProblemType);
                                }

                                if (ApiData.getInstance().getCurrentForm() != null) {
                                    String diagnosisProbType = ApiData.getInstance().getCurrentForm().getDiagnosisTypeOfProblem();
                                    if (!TextUtils.isEmpty(diagnosisProbType)) {
                                        for (int k = 0; k < rbGrpDiagnosisProblemType.getChildCount(); k++) {
                                            RadioButton rbutton = (RadioButton) rbGrpDiagnosisProblemType.getChildAt(k);
                                            if (rbutton.getTag().toString().toUpperCase().equals(diagnosisProbType.toUpperCase())) {
                                                rbutton.setChecked(true);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                        case 10: {
                            LinearLayout baseLayout = rootView.findViewById(R.id.baseLayoutDiagnosisProblemType);
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
                                            RadioGroup diagnosisProbType = (RadioGroup) getView().findViewById(R.id.rbgrpDiagnosisProblemType);
                                            int checkedVal = diagnosisProbType.getCheckedRadioButtonId();
                                            RadioButton checkedDPT = (RadioButton) getView().findViewById(checkedVal);
                                            if (checkedDPT != null) {
                                                String diagnosisPT = checkedDPT.getTag().toString();
                                                ApiData.getInstance().getCurrentForm().setDiagnosisTypeOfProblem(diagnosisPT);
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