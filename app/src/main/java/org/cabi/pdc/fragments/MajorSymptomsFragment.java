package org.cabi.pdc.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.cabi.pdc.R;
import org.cabi.pdc.activities.FormActivity;
import org.cabi.pdc.common.ApiData;
import org.cabi.pdc.common.DCAApplication;
import org.cabi.pdc.common.Utility;
import org.cabi.pdc.models.Section;

import java.util.ArrayList;

public class MajorSymptomsFragment extends Fragment {

    private Context mContext;
    ListView mMajorSymptoms;
    ArrayList<String> mMajorSymptomsList;
    DCAApplication dcaApplication;
    TableLayout tblLaySymptoms;

    public MajorSymptomsFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.mContext = getActivity();
        dcaApplication = (DCAApplication) getActivity().getApplication();

        View rootView = inflater.inflate(R.layout.fragment_major_symptoms, container, false);

        mMajorSymptoms = rootView.findViewById(R.id.lvMajorSymptoms);
        mMajorSymptomsList = new ArrayList<>();
        tblLaySymptoms = rootView.findViewById(R.id.tblSymptoms);

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
                            if (section.getFields().get(i).isMultiValued()) {
                                TextView tvBaseTitleSymptoms = rootView.findViewById(R.id.baseTitleSymptoms);
                                tvBaseTitleSymptoms.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());

                                if (tblLaySymptoms.getChildCount() > 0) {
                                    tblLaySymptoms.removeAllViews();
                                }

                                int dp10 = Utility.getDpFromPixels(mContext, 10);
                                if (section.getFields().get(i).getOptions() != null && section.getFields().get(i).getOptions().size() > 0) {
                                    for (int j = 0; j < section.getFields().get(i).getOptions().size(); j++) {
                                        String val = ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getOptions().get(j).getTranslationId()).getValue();
                                        TableRow tr = new TableRow(getActivity());
                                        TableRow.LayoutParams trlayPars = new TableRow.LayoutParams(mContext, null);
                                        trlayPars.bottomMargin = dp10;
                                        tr.setLayoutParams(trlayPars);
                                        tr.setPadding(dp10, dp10, 0, dp10);
                                        tr.setBackgroundColor(Color.WHITE);
                                        RelativeLayout relLay = (RelativeLayout) inflater.inflate(R.layout.generic_checkboxlist_item, null, false);
                                        CheckBox cb = (CheckBox) relLay.getChildAt(0);
                                        cb.setText(val);
                                        cb.setTag(val);
                                        cb.setId(9400 + i);
                                        tr.addView(relLay);
                                        tblLaySymptoms.addView(tr);
                                        mMajorSymptomsList.add(val);
                                    }
                                }

                                if (ApiData.getInstance().getCurrentForm() != null) {
                                    if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getMajorSymptoms())) {
                                        String[] strSelectedSymptoms = ApiData.getInstance().getCurrentForm().getMajorSymptoms().split("##");
                                        if (strSelectedSymptoms != null && strSelectedSymptoms.length > 0) {
                                            for (int k = 0; k < tblLaySymptoms.getChildCount(); k++) {
                                                RelativeLayout relLay = (RelativeLayout) ((TableRow) tblLaySymptoms.getChildAt(k)).getChildAt(0);
                                                CheckBox chkBox = (CheckBox) relLay.findViewWithTag(mMajorSymptomsList.get(k));
                                                for (int l = 0; l < strSelectedSymptoms.length; l++) {
                                                    if (chkBox.getText().toString().toUpperCase().equals(strSelectedSymptoms[l].toUpperCase())) {
                                                        chkBox.setChecked(true);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                        case 10: {
                            LinearLayout baseLayout = rootView.findViewById(R.id.baseLayoutSymptoms);
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
                                            String selectedItems = "";
                                            for (int k = 0; k < tblLaySymptoms.getChildCount(); k++) {
                                                CheckBox chkBox = ((CheckBox) ((RelativeLayout) ((TableRow) tblLaySymptoms.getChildAt(k)).getChildAt(0)).getChildAt(0));
                                                if (chkBox.isChecked()) {
                                                    selectedItems = selectedItems + chkBox.getText() + "##";
                                                }
                                            }
                                            ApiData.getInstance().getCurrentForm().setMajorSymptoms(selectedItems);
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