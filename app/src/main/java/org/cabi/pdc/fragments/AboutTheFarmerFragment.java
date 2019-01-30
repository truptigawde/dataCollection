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
import org.cabi.pdc.RoomDb.RoomDb_Form;
import org.cabi.pdc.activities.FormActivity;
import org.cabi.pdc.common.ApiData;
import org.cabi.pdc.common.DCAApplication;
import org.cabi.pdc.common.Utility;
import org.cabi.pdc.models.Section;

public class AboutTheFarmerFragment extends Fragment {

    private Context mContext;
    DCAApplication dcaApplication;

    public AboutTheFarmerFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        dcaApplication = (DCAApplication) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about_the_farmer, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            final int sectionPos = bundle.getInt("SectionPosition", -1);
            final int fragmentId = this.getId();
            final boolean fromSummary = bundle.getBoolean("FromSummary");
            final Section section = (Section) bundle.getSerializable("Section");

            if (section != null && section.getFields() != null && section.getFields().size() > 0) {
                for (int i = 0; i < section.getFields().size(); i++) {
                    switch (section.getFields().get(i).getFieldType()) {
                        case 11: {
                            TextView baseTitleFarmerName = rootView.findViewById(R.id.baseTitleFarmerName);
                            baseTitleFarmerName.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());
                            if (ApiData.getInstance().getCurrentForm() != null &&
                                    !TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getFarmerName())) {
                                EditText etFarmerName = (EditText) rootView.findViewById(R.id.etFarmerName);
                                etFarmerName.setText(ApiData.getInstance().getCurrentForm().getFarmerName().trim());
                            }
                        }
                        break;
                        case 15: {
                            TextView baseTitleFarmerPhoneNumber = rootView.findViewById(R.id.baseTitleFarmerPhoneNumber);
                            baseTitleFarmerPhoneNumber.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());
                            if (ApiData.getInstance().getCurrentForm() != null &&
                                    !TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getFarmerPhoneNumber())) {
                                EditText etFarmerPhoneNumber = (EditText) rootView.findViewById(R.id.etFarmerPhoneNumber);
                                etFarmerPhoneNumber.setText(ApiData.getInstance().getCurrentForm().getFarmerPhoneNumber().trim());
                            }
                        }
                        break;
                        case 13: {
                            TextView baseTitle = rootView.findViewById(R.id.baseTitleFarmerSex);
                            baseTitle.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());

                            RadioGroup radioGroupFarmerSex = rootView.findViewById(R.id.rbgrpFarmerSex);
                            if (section.getFields().get(i).getOptions() != null && section.getFields().get(i).getOptions().size() > 0) {
                                for (int j = 0; j < section.getFields().get(i).getOptions().size(); j++) {

                                    RadioButton radioButtonFarmerSex = (RadioButton) inflater.inflate(R.layout.generic_radio_button, null);
                                    RadioGroup.LayoutParams layoutParamsRB = new RadioGroup.LayoutParams(Utility.getDpFromPixels(mContext, 340), ViewGroup.LayoutParams.WRAP_CONTENT);
                                    if (j != 0) {
                                        layoutParamsRB.topMargin = Utility.getDpFromPixels(mContext, 10);
                                    }
                                    radioButtonFarmerSex.setLayoutParams(layoutParamsRB);
                                    radioButtonFarmerSex.setId(400 + j);
                                    radioButtonFarmerSex.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getOptions().get(j).getTranslationId()).getValue());
                                    radioButtonFarmerSex.setTag(section.getFields().get(i).getOptions().get(j).getValue());
                                    radioButtonFarmerSex.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                        }
                                    });
                                    radioGroupFarmerSex.addView(radioButtonFarmerSex);
                                }

                                if (ApiData.getInstance().getCurrentForm() != null) {
                                    String farmerGender = ApiData.getInstance().getCurrentForm().getFarmerGender();
                                    if (!TextUtils.isEmpty(farmerGender)) {
                                        for (int k = 0; k < radioGroupFarmerSex.getChildCount(); k++) {
                                            RadioButton rbutton = (RadioButton) radioGroupFarmerSex.getChildAt(k);
                                            if (rbutton.getTag().toString().toUpperCase().equals(farmerGender.toUpperCase())) {
                                                rbutton.setChecked(true);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                        case 24: {
                            TextView btnTextFindAddress = rootView.findViewById(R.id.btnTextFindAddress);
                            btnTextFindAddress.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());

                            RelativeLayout rlFindAddress = rootView.findViewById(R.id.rlFindAddress);
                            rlFindAddress.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    String selectFarmerFindCounty = "Find county";
                                    if (ApiData.getInstance().getMetadataTranslationsList() != null && !TextUtils.isEmpty(ApiData.getInstance().getMetadataTranslationsList().get("SelectFarmerFindCounty").getValue())) {
                                        selectFarmerFindCounty = ApiData.getInstance().getMetadataTranslationsList().get("SelectFarmerFindCounty").getValue().toString();
                                    }

                                    ((FormActivity) getActivity()).loadNextSection(selectFarmerFindCounty, fragmentId);
//                                    Fragment fragment = new CountyFragment();
//                                    FragmentManager fragmentManager = getFragmentManager();
//                                    fragmentManager.beginTransaction()
//                                            .replace(getId(), fragment)
//                                            .addToBackStack(fragment.getClass().toString())
//                                            .commit();
                                }
                            });
                        }
                        break;
                        case 22: {
                            TextView tvFarmerLocation1 = rootView.findViewById(R.id.baseTitleFarmerLocation1);
                            tvFarmerLocation1.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());
                            if (ApiData.getInstance().getCurrentForm() != null &&
                                    !TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getFarmerLocation1())) {
                                EditText etFarmerLoc1 = (EditText) rootView.findViewById(R.id.etFarmerLocation1);
                                etFarmerLoc1.setText(ApiData.getInstance().getCurrentForm().getFarmerLocation1().trim());
                            }
                        }
                        break;
                        case 20: {
                            TextView tvFarmerLocation2 = rootView.findViewById(R.id.baseTitleFarmerLocation2);
                            tvFarmerLocation2.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());
                            if (ApiData.getInstance().getCurrentForm() != null &&
                                    !TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getFarmerLocation2())) {
                                EditText etFarmerLoc2 = (EditText) rootView.findViewById(R.id.etFarmerLocation2);
                                etFarmerLoc2.setText(ApiData.getInstance().getCurrentForm().getFarmerLocation2().trim());
                            }
                        }
                        break;
                        case 21: {
                            TextView tvFarmerLocation3 = rootView.findViewById(R.id.baseTitleFarmerLocation3);
                            tvFarmerLocation3.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());
                            if (ApiData.getInstance().getCurrentForm() != null &&
                                    !TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getFarmerLocation3())) {
                                EditText etFarmerLoc3 = (EditText) rootView.findViewById(R.id.etFarmerLocation3);
                                etFarmerLoc3.setText(ApiData.getInstance().getCurrentForm().getFarmerLocation3().trim());
                            }
                        }
                        break;
                        case 10: {
                            LinearLayout baseLayout = rootView.findViewById(R.id.baseLayoutAboutTheFarmer);
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
                                        if (ApiData.getInstance().getCurrentForm() == null) {
                                            ApiData.getInstance().setCurrentForm(new RoomDb_Form());
                                        }
                                        EditText etFarmerName = (EditText) getView().findViewById(R.id.etFarmerName);
                                        String farmerName = etFarmerName.getText().toString().trim();
                                        ApiData.getInstance().getCurrentForm().setFarmerName(farmerName);

                                        EditText etFarmerPhoneNumber = (EditText) getView().findViewById(R.id.etFarmerPhoneNumber);
                                        String farmerPhoneNumber = etFarmerPhoneNumber.getText().toString().trim();
                                        ApiData.getInstance().getCurrentForm().setFarmerPhoneNumber(farmerPhoneNumber);

                                        RadioGroup farmerSex = (RadioGroup) getView().findViewById(R.id.rbgrpFarmerSex);
                                        int selectedId = farmerSex.getCheckedRadioButtonId();
                                        RadioButton selectedfarmerSex = (RadioButton) getView().findViewById(selectedId);
                                        if (selectedfarmerSex != null) {
                                            String sex = selectedfarmerSex.getTag().toString();
                                            ApiData.getInstance().getCurrentForm().setFarmerGender(sex);
                                        }

                                        EditText etFarmerLocation1 = (EditText) getView().findViewById(R.id.etFarmerLocation1);
                                        String farmerLoc1 = etFarmerLocation1.getText().toString().trim();
                                        ApiData.getInstance().getCurrentForm().setFarmerLocation1(farmerLoc1);

                                        EditText etFarmerLocation2 = (EditText) getView().findViewById(R.id.etFarmerLocation2);
                                        String farmerLoc2 = etFarmerLocation2.getText().toString().trim();
                                        ApiData.getInstance().getCurrentForm().setFarmerLocation2(farmerLoc2);

                                        EditText etFarmerLocation3 = (EditText) getView().findViewById(R.id.etFarmerLocation3);
                                        String farmerLoc3 = etFarmerLocation3.getText().toString().trim();
                                        ApiData.getInstance().getCurrentForm().setFarmerLocation3(farmerLoc3);

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