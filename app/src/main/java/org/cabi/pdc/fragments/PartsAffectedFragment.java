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

public class PartsAffectedFragment extends Fragment {

    private Context mContext;
    ListView mPartsAffected;
    ArrayList<String> mPartsAffectedList;
    DCAApplication dcaApplication;
    TableLayout tbLayPartsAffected;

    public PartsAffectedFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        dcaApplication = (DCAApplication) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_parts_affected, container, false);

        mPartsAffected = rootView.findViewById(R.id.lvPlantPartAffected);
        mPartsAffectedList=new ArrayList<>();
        tbLayPartsAffected=rootView.findViewById(R.id.tblPartsAffected);

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
                                TextView tvBaseTitlePartsAffected = rootView.findViewById(R.id.baseTitlePartsAffected);
                                tvBaseTitlePartsAffected.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());

                                if (section.getFields().get(i).getOptions() != null && section.getFields().get(i).getOptions().size() > 0) {
                                    if (tbLayPartsAffected.getChildCount() > 0) {
                                        tbLayPartsAffected.removeAllViews();
                                    }

                                    int dp10 = Utility.getDpFromPixels(mContext, 10);
                                    for (int j = 0; j < section.getFields().get(i).getOptions().size(); j++) {
                                        String val = ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getOptions().get(j).getTranslationId()).getValue();
                                        TableRow tr = new TableRow(getActivity());
                                        TableRow.LayoutParams trlayPars = new TableRow.LayoutParams(mContext, null);
                                        trlayPars.bottomMargin = dp10;
                                        tr.setLayoutParams(trlayPars);
                                        tr.setPadding(10, dp10, 0, dp10);
                                        tr.setBackgroundColor(Color.WHITE);
                                        RelativeLayout relLay = (RelativeLayout) inflater.inflate(R.layout.generic_checkboxlist_item, null, false);
                                        CheckBox cb = (CheckBox) relLay.getChildAt(0);
                                        cb.setText(val);
                                        cb.setTag(val);
                                        cb.setId(9100 + i);
                                        tr.addView(relLay);
                                        tbLayPartsAffected.addView(tr);
                                        mPartsAffectedList.add(val);
                                    }
                                }
                            }
                            if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getPartsAffected())) {
                                String[] strSelectedParts = ApiData.getInstance().getCurrentForm().getPartsAffected().split("##");
                                if (strSelectedParts!= null && strSelectedParts.length > 0) {
                                    for (int k = 0; k < tbLayPartsAffected.getChildCount(); k++) {
                                        RelativeLayout relLay = (RelativeLayout) ((TableRow) tbLayPartsAffected.getChildAt(k)).getChildAt(0);
                                        CheckBox chkBox = (CheckBox) relLay.findViewWithTag(mPartsAffectedList.get(k));
                                        for (int l = 0; l < strSelectedParts.length; l++) {
                                            if (chkBox.getText().toString().toUpperCase().equals(strSelectedParts[l].toUpperCase())) {
                                                chkBox.setChecked(true);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                        case 10: {
                            LinearLayout baseLayout = rootView.findViewById(R.id.baseLayoutPartsAffected);
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
                                            for (int k = 0; k < tbLayPartsAffected.getChildCount(); k++) {
                                                CheckBox chkBox = ((CheckBox) ((RelativeLayout) ((TableRow) tbLayPartsAffected.getChildAt(k)).getChildAt(0)).getChildAt(0));
                                                if (chkBox.isChecked()) {
                                                    selectedItems = selectedItems + chkBox.getText() + "##";
                                                }
                                            }
                                            ApiData.getInstance().getCurrentForm().setPartsAffected(selectedItems);
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