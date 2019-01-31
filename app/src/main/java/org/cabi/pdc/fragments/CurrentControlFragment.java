package org.cabi.pdc.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

public class CurrentControlFragment extends Fragment {

    Context mContext;
    DCAApplication dcaApplication;
    EditText etCurrentControl;
    LinearLayout layPracticesUsed;

    public CurrentControlFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.mContext = getActivity();
        dcaApplication = (DCAApplication) getActivity().getApplication();

        View rootView = inflater.inflate(R.layout.fragment_current_control, container, false);

        layPracticesUsed = rootView.findViewById(R.id.layPracticesUsed);
        etCurrentControl = rootView.findViewById(R.id.etCurrentControl);

        Bundle bundle = getArguments();
        if (bundle != null) {
            final int sectionPos = bundle.getInt("SectionPosition", -1);
            final int fragmentId = this.getId();
            final boolean fromSummary = bundle.getBoolean("FromSummary");
            Section section = (Section) bundle.getSerializable("Section");
            if (section.getFields() != null && section.getFields().size() > 0) {
                for (int i = 0; i < section.getFields().size(); i++) {
                    switch (section.getFields().get(i).getFieldType()) {
                        case 2: {
                            TextView tvBaseTitle = rootView.findViewById(R.id.baseTitleCurrentControl);
                            tvBaseTitle.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());

                            if (section.getFields().get(i).getOptions() != null && section.getFields().get(i).getOptions().size() > 0) {

                                RadioGroup rbGrpCurrentControl = rootView.findViewById(R.id.rbgrpCurrentControl);

                                for (int j = 0; j < section.getFields().get(i).getOptions().size(); j++) {
                                    RadioButton rbCurrentControlOption = (RadioButton) inflater.inflate(R.layout.generic_radio_button, null);
                                    RadioGroup.LayoutParams layoutParamsRB = new RadioGroup.LayoutParams(Utility.getDpFromPixels(mContext, 340), ViewGroup.LayoutParams.WRAP_CONTENT);
                                    if (j != 0) {
                                        layoutParamsRB.topMargin = Utility.getDpFromPixels(mContext, 10);
                                    }
                                    rbCurrentControlOption.setLayoutParams(layoutParamsRB);
                                    rbCurrentControlOption.setId(200 + j);
                                    rbCurrentControlOption.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getOptions().get(j).getTranslationId()).getValue());
                                    rbCurrentControlOption.setTag(section.getFields().get(i).getOptions().get(j).getValue());
                                    rbCurrentControlOption.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (view.getTag().toString().toUpperCase().equals("YES")) {
                                                layPracticesUsed.setVisibility(View.VISIBLE);
                                            } else {
                                                etCurrentControl.setText("");
                                                layPracticesUsed.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                                    rbGrpCurrentControl.addView(rbCurrentControlOption);
                                }

                                if (ApiData.getInstance().getCurrentForm() != null) {
                                    String currentControl = ApiData.getInstance().getCurrentForm().getCurrentControl();
                                    if (!TextUtils.isEmpty(currentControl)) {
                                        for (int k = 0; k < rbGrpCurrentControl.getChildCount(); k++) {
                                            RadioButton rbutton = (RadioButton) rbGrpCurrentControl.getChildAt(k);
                                            if (rbutton.getTag().toString().toUpperCase().equals(currentControl.toUpperCase())) {
                                                rbutton.setChecked(true);
                                            }
                                            if (currentControl.toUpperCase().equals("YES")) {
                                                layPracticesUsed.setVisibility(View.VISIBLE);
                                            } else {
                                                layPracticesUsed.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                        case 3: {
                            if (!TextUtils.isEmpty(section.getFields().get(i).getLinkedToField())) {
                                TextView tvTitlePracticesUsed = rootView.findViewById(R.id.tvTitlePracticesUsed);
                                tvTitlePracticesUsed.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());
                                if (ApiData.getInstance().getCurrentForm() != null) {
                                    if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getCurrentControlPracticesUsed())) {
                                        etCurrentControl.setText(ApiData.getInstance().getCurrentForm().getCurrentControlPracticesUsed());
                                    }
                                }
                            }
                        }
                        break;
                        case 10: {
                            LinearLayout baseLayout = rootView.findViewById(R.id.baseLayoutCurrentControl);
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
                                            RadioGroup rgCurrentControl = (RadioGroup) getView().findViewById(R.id.rbgrpCurrentControl);
                                            int selectedId = rgCurrentControl.getCheckedRadioButtonId();
                                            RadioButton checkedCurrentControl = (RadioButton) getView().findViewById(selectedId);
                                            if (checkedCurrentControl != null) {
                                                String currControl = checkedCurrentControl.getTag().toString();
                                                ApiData.getInstance().getCurrentForm().setCurrentControl(currControl);
                                            }

//                                            EditText etCurrentControlPU = (EditText) getView().findViewById(R.id.etCurrentControl);
                                            String currentControlPU = etCurrentControl.getText().toString();
                                            ApiData.getInstance().getCurrentForm().setCurrentControlPracticesUsed(currentControlPU.trim());
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