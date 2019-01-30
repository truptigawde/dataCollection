package org.cabi.pdc.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.cabi.pdc.R;
import org.cabi.pdc.activities.FormActivity;
import org.cabi.pdc.common.ApiData;
import org.cabi.pdc.common.DCAApplication;
import org.cabi.pdc.models.Section;

public class RecommendationFragment extends Fragment {

    private Context mContext;
    DCAApplication dcaApplication;
    LinearLayout sectionAlternateLanguage;
    ImageView imgAddRemoveAltLanguage;
    RelativeLayout btnAddAlternateLang;
    TextView btnTextAddAlternateLang;

    public RecommendationFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        dcaApplication = (DCAApplication) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recommendation, container, false);

        sectionAlternateLanguage = rootView.findViewById(R.id.sectionAlternateLanguage);
        imgAddRemoveAltLanguage = rootView.findViewById(R.id.imgAddRemoveAltLanguage);
        btnAddAlternateLang = rootView.findViewById(R.id.btnAddAlternateLang);
        btnTextAddAlternateLang = rootView.findViewById(R.id.btnTextAddAlternateLang);

        Bundle bundle = getArguments();
        if (bundle != null) {
            final int sectionPos = bundle.getInt("SectionPosition", -1);
            final int fragmentId = this.getId();
            final Section section = (Section) bundle.getSerializable("Section");
            if (section != null && section.getFields() != null && section.getFields().size() > 0) {
                for (int i = 0; i < section.getFields().size(); i++) {
                    switch (section.getFields().get(i).getFieldType()) {
                        case 26: {
                            TextView tvCopyPreviousRecommendation = rootView.findViewById(R.id.btnTextCopyPreviousRecommendation);
                            tvCopyPreviousRecommendation.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());

                            RelativeLayout relativeLayout = rootView.findViewById(R.id.relLayCopyPrevRecommendation);
                            relativeLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String recommendationFind = "Find recommendation";
                                    if (ApiData.getInstance().getMetadataTranslationsList() != null && !TextUtils.isEmpty(ApiData.getInstance().getMetadataTranslationsList().get("RecommendationFind").getValue())) {
                                        recommendationFind = ApiData.getInstance().getMetadataTranslationsList().get("RecommendationFind").getValue().toString();
                                    }

                                    ((FormActivity) getActivity()).loadNextSection(recommendationFind, fragmentId);
                                }
                            });
                        }
                        break;
                        case 3: {
                            if (section.getFields().get(i).getOrder() == 2) {
                                TextView tvManageCurrent = rootView.findViewById(R.id.baseTitleRecommendationToManageCurrent);
                                tvManageCurrent.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());
                                TextView tvManageCurrentAltLang = rootView.findViewById(R.id.baseTitleRecommendationToManageCurrentAltLang);
                                tvManageCurrentAltLang.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());
                                if (ApiData.getInstance().getCurrentForm() != null) {
                                    String strRecommendNow = "";
                                    if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getRecommendationToManageNow())) {
                                        strRecommendNow = ApiData.getInstance().getCurrentForm().getRecommendationToManageNow();
                                    } else if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getDiagnosis()) || !TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getCropName())) {
                                        if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getDiagnosis())) {
                                            strRecommendNow = ApiData.getInstance().getCurrentForm().getDiagnosis();
                                        }
                                        if (!TextUtils.isEmpty(strRecommendNow)) {
                                            strRecommendNow = strRecommendNow + " - " + (ApiData.getInstance().getCurrentForm().getCropName());
                                        } else if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getCropName())) {
                                            strRecommendNow = (ApiData.getInstance().getCurrentForm().getCropName());
                                        }
                                    }
                                    EditText recommendNow = rootView.findViewById(R.id.etRecommendationManageNow);
                                    recommendNow.setText(strRecommendNow);
                                    if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getRecommendationToManageNowAltLang())) {
                                        EditText recommendNowAlt = rootView.findViewById(R.id.etRecommendationManageNowAltLang);
                                        recommendNowAlt.setText(ApiData.getInstance().getCurrentForm().getRecommendationToManageNowAltLang());
                                    }
                                }
                            } else if (section.getFields().get(i).getOrder() == 3) {
                                TextView tvPreventFuture = rootView.findViewById(R.id.baseTitleRecommendationToPreventFuture);
                                tvPreventFuture.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());
                                TextView tvPreventFutureAltLang = rootView.findViewById(R.id.baseTitleRecommendationToPreventFutureAltLang);
                                tvPreventFutureAltLang.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());
                                if (ApiData.getInstance().getCurrentForm() != null) {
                                    String strRecommendFuture = "";
                                    if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getRecommendationForFuturePrevention())) {
                                        strRecommendFuture = ApiData.getInstance().getCurrentForm().getRecommendationForFuturePrevention();
                                    } else if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getDiagnosis()) || !TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getCropName())) {
                                        if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getDiagnosis())) {
                                            strRecommendFuture = ApiData.getInstance().getCurrentForm().getDiagnosis();
                                        }
                                        if (!TextUtils.isEmpty(strRecommendFuture)) {
                                            strRecommendFuture = strRecommendFuture + " - " + (ApiData.getInstance().getCurrentForm().getCropName());
                                        } else if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getCropName())) {
                                            strRecommendFuture = (ApiData.getInstance().getCurrentForm().getCropName());
                                        }
                                    }
                                    EditText recommendFuture = rootView.findViewById(R.id.etRecommendationFuturePrevent);
                                    recommendFuture.setText(strRecommendFuture);
                                    if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getRecommendationForFuturePreventionAltLang())) {
                                        EditText recommendFutureAlt = rootView.findViewById(R.id.etRecommendationFuturePreventAltLang);
                                        recommendFutureAlt.setText(ApiData.getInstance().getCurrentForm().getRecommendationForFuturePreventionAltLang());
                                    }
                                }
                            }

                        }
                        break;
                        case 28: {
                            final String textAddAltLanguage = ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue();
                            final String textRemoveAltLanguage = ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId().replace("Add", "Remove")).getValue();
//                            final RelativeLayout btnAddAlternateLang = rootView.findViewById(R.id.btnAddAlternateLang);
//                            final TextView btnTextAddAlternateLang = rootView.findViewById(R.id.btnTextAddAlternateLang);
                            btnTextAddAlternateLang.setText(textAddAltLanguage);
                            btnAddAlternateLang.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (sectionAlternateLanguage.getVisibility() == View.GONE) {
//                                        sectionAlternateLanguage.setVisibility(View.VISIBLE);
//                                        imgAddRemoveAltLanguage.setImageResource(R.drawable.ic_remove_black);
//                                        btnTextAddAlternateLang.setText(textRemoveAltLanguage);
//                                        btnAddAlternateLang.setBackgroundColor(Color.BLACK);
                                        showSectionAltLang(textRemoveAltLanguage);
                                    } else {
//                                        sectionAlternateLanguage.setVisibility(View.GONE);
//                                        imgAddRemoveAltLanguage.setImageResource(R.drawable.ic_add_black);
//                                        btnTextAddAlternateLang.setText(textAddAltLanguage);
//                                        btnAddAlternateLang.setBackgroundColor(getResources().getColor(R.color.ButtonGreen));
                                        hideSectionAltLang(textAddAltLanguage);
                                    }
                                }
                            });

                            if (ApiData.getInstance().getCurrentForm() != null && (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getRecommendationToManageNowAltLang()) || !TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getRecommendationForFuturePreventionAltLang()))) {
                                showSectionAltLang(textRemoveAltLanguage);
                            } else {
                                hideSectionAltLang(textAddAltLanguage);
                            }
                        }
                        break;
                        case 10: {
                            LinearLayout baseLayout = rootView.findViewById(R.id.baseLayoutRecommendations);
                            LinearLayout btnNextUpdate = (LinearLayout) inflater.inflate(R.layout.generic_section_next_button, null);
                            TextView tvNextButton = btnNextUpdate.findViewById(R.id.tvButtonNextUpdate);
                            final boolean fromSummary = bundle.getBoolean("FromSummary");

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
                                            EditText etRecMN = rootView.findViewById(R.id.etRecommendationManageNow);
                                            ApiData.getInstance().getCurrentForm().setRecommendationToManageNow(etRecMN.getText().toString().trim());
                                            EditText etRecFP = rootView.findViewById(R.id.etRecommendationFuturePrevent);
                                            ApiData.getInstance().getCurrentForm().setRecommendationForFuturePrevention(etRecFP.getText().toString().trim());
                                            EditText etRecMNAlt = rootView.findViewById(R.id.etRecommendationManageNowAltLang);
                                            ApiData.getInstance().getCurrentForm().setRecommendationToManageNowAltLang(etRecMNAlt.getText().toString().trim());
                                            EditText etRecFPAlt = rootView.findViewById(R.id.etRecommendationFuturePreventAltLang);
                                            ApiData.getInstance().getCurrentForm().setRecommendationForFuturePreventionAltLang(etRecFPAlt.getText().toString().trim());
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

    public void showSectionAltLang(String textRemoveAltLanguage) {
        sectionAlternateLanguage.setVisibility(View.VISIBLE);
        imgAddRemoveAltLanguage.setImageResource(R.drawable.ic_remove_black);
        btnTextAddAlternateLang.setText(textRemoveAltLanguage);
        btnAddAlternateLang.setBackgroundColor(Color.BLACK);
    }

    public void hideSectionAltLang(String textAddAltLanguage) {
        sectionAlternateLanguage.setVisibility(View.GONE);
        imgAddRemoveAltLanguage.setImageResource(R.drawable.ic_add_black);
        btnTextAddAlternateLang.setText(textAddAltLanguage);
        btnAddAlternateLang.setBackgroundColor(getResources().getColor(R.color.ButtonGreen));
    }
}