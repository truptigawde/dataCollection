package org.cabi.pdc.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.cabi.pdc.R;
import org.cabi.pdc.activities.FormActivity;
import org.cabi.pdc.common.ApiData;
import org.cabi.pdc.common.DCAApplication;
import org.cabi.pdc.common.Utility;
import org.cabi.pdc.models.Section;

import java.util.ArrayList;

public class DevelopmentStageFragment extends Fragment {

    private Context mContext;
    private ListView mDevelopmentStage;
    private ArrayList<String> mDevelopmentStageList;
    DCAApplication dcaApplication;
    TableLayout tblDevStage;

    public DevelopmentStageFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mContext = getActivity();
        dcaApplication = (DCAApplication) getActivity().getApplication();

        View rootView = inflater.inflate(R.layout.fragment_development_stage, container, false);

        mDevelopmentStage = rootView.findViewById(R.id.lvDevelopmentStage);
        tblDevStage = rootView.findViewById(R.id.tblDevStage);
        mDevelopmentStageList = new ArrayList<String>();

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
                                TextView tvBaseTitleDevelopmentStage = rootView.findViewById(R.id.baseTitleDevelopmentStage);
                                tvBaseTitleDevelopmentStage.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());

//                                if (section.getFields().get(i).getOptions() != null && section.getFields().get(i).getOptions().size() > 0) {
//                                    for (int j = 0; j < section.getFields().get(i).getOptions().size(); j++) {
//                                        String val = ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getOptions().get(j).getTranslationId()).getValue();
//                                        if (!mDevelopmentStageList.contains(val)) {
//                                            mDevelopmentStageList.add(val);
//                                        }
//                                    }
//                                }
//                                if (ApiData.getInstance().getCurrentForm() != null) {
//                                    if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getDevelopmentStage())) {
//                                        String[] strSelectedStages = ApiData.getInstance().getCurrentForm().getDevelopmentStage().split("##");
//                                        for (int k = 0; k < mDevelopmentStage.getChildCount(); k++) {
//                                            RelativeLayout relLay = (RelativeLayout) mDevelopmentStage.getChildAt(k);
//                                            CheckBox chkBox = relLay.findViewById(R.id.checkBoxItem);
//                                            if (ArrayUtils.contains(strSelectedStages, chkBox.getText())) {
//                                                chkBox.setChecked(true);
//                                            }
//                                        }
//                                    }
//                                }

                                if (tblDevStage.getChildCount() > 0) {
                                    tblDevStage.removeAllViews();
                                }

                                int dp10 = Utility.getDpFromPixels(mContext, 10);
                                if (section.getFields().get(i).getOptions() != null && section.getFields().get(i).getOptions().size() > 0) {
                                    for (int j = 0; j < section.getFields().get(i).getOptions().size(); j++) {

                                        String val = ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getOptions().get(j).getTranslationId()).getValue();
                                        TableRow tr = new TableRow(getActivity());
                                        TableRow.LayoutParams trlayPars = new TableRow.LayoutParams(mContext, null);
                                        trlayPars.bottomMargin = dp10;
                                        tr.setLayoutParams(trlayPars);
                                        tr.setPadding(10, dp10, 0, dp10);
                                        tr.setBackgroundColor(Color.WHITE);
                                        RelativeLayout relLay = (RelativeLayout) inflater.inflate(R.layout.generic_checkboxlist_item, null, false);
                                        CheckBox cb = (CheckBox) relLay.getChildAt(0); //findViewById(R.id.checkBoxItem);
                                        cb.setText(val);
                                        cb.setTag(val);
                                        cb.setId(9000 + i);
//                                        if (ApiData.getInstance().getCurrentForm() != null && !TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getDevelopmentStage())) {
//                                            String[] selectedItems = ApiData.getInstance().getCurrentForm().getDevelopmentStage().split("##");
//                                            if (ArrayUtils.contains(selectedItems, val)) {
//                                                cb.setChecked(true);
//                                            } else {
//                                                cb.setChecked(false);
//                                            }
//                                        }
                                        tr.addView(relLay);
                                        tblDevStage.addView(tr);
                                        mDevelopmentStageList.add(val);
                                        cb = null;
                                    }
                                }
                            } else {
                                Toast.makeText(getActivity(), "Checkbox issue in " + section.getFields().get(i).getBaseTitle().toString(), Toast.LENGTH_SHORT).show();
                            }

                            if (ApiData.getInstance().getCurrentForm() != null) {
                                if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getDevelopmentStage())) {
                                    String[] strSelectedStages = ApiData.getInstance().getCurrentForm().getDevelopmentStage().split("##");
                                    if (strSelectedStages != null && strSelectedStages.length > 0) {
                                        for (int k = 0; k < tblDevStage.getChildCount(); k++) {
                                            RelativeLayout relLay = (RelativeLayout) ((TableRow) tblDevStage.getChildAt(k)).getChildAt(0);
                                            CheckBox chkBox = (CheckBox) relLay.findViewWithTag(mDevelopmentStageList.get(k));//findViewById(R.id.checkBoxItem);
                                            for (int l = 0; l < strSelectedStages.length; l++) {
                                                if (chkBox.getText().toString().toUpperCase().equals(strSelectedStages[l].toUpperCase())) {
                                                    chkBox.setChecked(true);
                                                }
//                                            else {
//                                                chkBox.setChecked(false);
//                                            }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                        case 10: {
                            LinearLayout baseLayout = rootView.findViewById(R.id.baseLayoutDevelopmentStage);
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
//                                            for (int k = 0; k < mDevelopmentStage.getChildCount(); k++) {
//                                                RelativeLayout relLay = (RelativeLayout) mDevelopmentStage.getChildAt(k);
//                                                CheckBox chkBox = relLay.findViewById(R.id.checkBoxItem);
//                                                if (chkBox.isChecked()) {
//                                                    selectedItems = selectedItems + chkBox.getText() + "##";
//                                                }
//                                            }
                                            for (int k = 0; k < tblDevStage.getChildCount(); k++) {
                                                CheckBox chkBox = ((CheckBox) ((RelativeLayout) ((TableRow) tblDevStage.getChildAt(k)).getChildAt(0)).getChildAt(0)); //findViewById(R.id.checkBoxItem)
                                                if (chkBox.isChecked()) {
                                                    selectedItems = selectedItems + chkBox.getText() + "##";
                                                }
                                            }
                                            ApiData.getInstance().getCurrentForm().setDevelopmentStage(selectedItems);
                                        }
                                    }

                                    if (fromSummary) {
                                        ((FormActivity) getActivity()).loadNextSection(dcaApplication.getSectionList().size() + 1, fragmentId, true);

                                    } else {
                                        ((FormActivity) getActivity()).loadNextSection(sectionPos + 1, fragmentId, false);
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
//        GenericCheckBoxListAdapter developmentStageAdapter = new GenericCheckBoxListAdapter(getActivity(), mDevelopmentStageList);
//        mDevelopmentStage.setAdapter(developmentStageAdapter);
//
//        mDevelopmentStage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                OnListItemClick(adapterView, view, i, l);
//            }
//        });

        return rootView;
    }

    private void OnListItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}