package org.cabi.pdc.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.cabi.pdc.R;
import org.cabi.pdc.RoomDb.RoomDB_Diagnosis;
import org.cabi.pdc.activities.FormActivity;
import org.cabi.pdc.common.ApiData;
import org.cabi.pdc.common.DCAApplication;
import org.cabi.pdc.models.Metadata;
import org.cabi.pdc.models.Section;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class DiagnosisFragment extends Fragment {

    private Context mContext;
    ListView mAllDiagnosis;
    ArrayList<String> mAllDiagnosisList = new ArrayList<>();
    ListView mRecentDiagnosis;
    ArrayList<String> mRecentDiagnosisList = new ArrayList<>();
    DCAApplication dcaApplication;
    EditText etDiagnosis;

    public DiagnosisFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        dcaApplication = (DCAApplication) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_diagnosis, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            final int sectionPos = bundle.getInt("SectionPosition", -1);
            final int fragmentId = this.getId();
            final boolean fromSummary = bundle.getBoolean("FromSummary");
            Section section = (Section) bundle.getSerializable("Section");

            if (section.getFields() != null && section.getFields().size() > 0) {

                for (int i = 0; i < section.getFields().size(); i++) {
                    switch (section.getFields().get(i).getFieldType()) {
                        case 25: {
                            TextView tvBaseTitleDiagnosis = rootView.findViewById(R.id.baseTitleDiagnosis);
                            String strDiagnosis = ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue();
                            if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getCropName())) {
                                strDiagnosis = strDiagnosis + ": " + ApiData.getInstance().getCurrentForm().getCropName();
                                if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getCropVariety())) {
                                    strDiagnosis = strDiagnosis + ", " + ApiData.getInstance().getCurrentForm().getCropVariety();
                                }
                            }
                            tvBaseTitleDiagnosis.setText(strDiagnosis);

                            etDiagnosis = rootView.findViewById(R.id.etDiagnosis);
                            etDiagnosis.setHint(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getHintTranslationId()).getValue());
                            if (ApiData.getInstance().getCurrentForm() != null) {
                                if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getDiagnosis())) {
                                    etDiagnosis.setText(ApiData.getInstance().getCurrentForm().getDiagnosis());
                                }
                            }
                        }
                        break;
                        case 10: {
                            TextView tvNextButton = rootView.findViewById(R.id.btnTextNextDiagnosis);
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
                            rootView.findViewById(R.id.btnNextDiagnosis).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (sectionPos > -1) {
                                        if (ApiData.getInstance().getCurrentForm() != null) {
                                            String diagnosis = etDiagnosis.getText().toString();
                                            ApiData.getInstance().getCurrentForm().setDiagnosis(diagnosis);
                                        }
                                        if (fromSummary) {
                                            ((FormActivity) getActivity()).loadNextSection(dcaApplication.getSectionList().size() + 1, fragmentId, true);

                                        } else {
                                            ((FormActivity) getActivity()).loadNextSection(sectionPos + 1, fragmentId, false);
                                        }
                                    }
                                }
                            });
                        }
                        break;
                    }
                }
            }
        }

        SharedPreferences sp = getActivity().getSharedPreferences("FormData", MODE_PRIVATE);
        final String strRecentDiagnosis = sp.getString("Recent_Diagnosis", "");
        if (!TextUtils.isEmpty(strRecentDiagnosis)) {
            try {
                JSONArray jArrRecentDiagnosis = new JSONArray(strRecentDiagnosis);
                for (int i = 0; i < jArrRecentDiagnosis.length(); i++) {
                    if (!mRecentDiagnosisList.contains(jArrRecentDiagnosis.getString(i))) {
                        mRecentDiagnosisList.add(jArrRecentDiagnosis.getString(i));
                    }
                }
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }

        mRecentDiagnosis = rootView.findViewById(R.id.lvRecentDiagnosis);
        if (mRecentDiagnosisList.size() > 0) {
            final ArrayAdapter<String> recentDiagnosisAdapter = new ArrayAdapter<String>(getActivity(), R.layout.generic_listview_item, R.id.title, mRecentDiagnosisList);
            mRecentDiagnosis.setAdapter(recentDiagnosisAdapter);
            View headerViewRecentDiagnosis = inflater.inflate(R.layout.generic_listview_header, container, false);
            TextView headerTextRecent = headerViewRecentDiagnosis.findViewById(R.id.headerText);
            String recentDiagnosisHeader = "Recent diagnoses:";
            if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("DiagnosisRecent")) {
                recentDiagnosisHeader = ApiData.getInstance().getMetadataTranslationsList().get("DiagnosisRecent").getValue();
            }
            headerTextRecent.setText(recentDiagnosisHeader);
            mRecentDiagnosis.addHeaderView(headerViewRecentDiagnosis);
            mRecentDiagnosis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    OnListItemClick(adapterView, view, i, l);
                }
            });
        } else {
            mRecentDiagnosis.setVisibility(View.GONE);
        }

        if (dcaApplication.getCropVarietyMetadata() != null && dcaApplication.getCropVarietyMetadata().size() > 0)
            if (dcaApplication.getDiagnosisMetadata() != null && dcaApplication.getDiagnosisMetadata().size() > 0 && !TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getCropName())) {
                for (Metadata item : dcaApplication.getDiagnosisMetadata()) {
                    if (item.getParentName() != null && item.getParentName().toUpperCase().contains(ApiData.getInstance().getCurrentForm().getCropName().toUpperCase())) {
                        if (!mAllDiagnosisList.contains(item.getName())) {
                            mAllDiagnosisList.add(item.getName());
                        }
                    }
                }
            }

        List<RoomDB_Diagnosis> diagnosislist = ((FormActivity) getActivity()).dcaFormDatabase.dcaDiagnosisDao().getAllDiagnosis();
        if (diagnosislist != null && diagnosislist.size() > 0) {
            for (RoomDB_Diagnosis diagnosis : diagnosislist) {
                if (!mAllDiagnosisList.contains(diagnosis.getDiagnosis())) {
                    mAllDiagnosisList.add(diagnosis.getDiagnosis());
                }
            }
        }

        mAllDiagnosis = rootView.findViewById(R.id.lvAllDiagnosis);
        if (mAllDiagnosisList.size() > 0) {
            final ArrayAdapter<String> diagnosisAdapter = new ArrayAdapter<String>(getActivity(), R.layout.generic_listview_item, R.id.title, mAllDiagnosisList);
            mAllDiagnosis.setAdapter(diagnosisAdapter);

            View headerAllDiagnosis = inflater.inflate(R.layout.generic_listview_header, container, false);
            TextView headerTextAllDiagnosis = headerAllDiagnosis.findViewById(R.id.headerText);
            String allDiagnosisHeader = "All diagnoses:";
            if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("DiagnosisAll")) {
                allDiagnosisHeader = ApiData.getInstance().getMetadataTranslationsList().get("DiagnosisAll").getValue();
            }
            headerTextAllDiagnosis.setText(allDiagnosisHeader);
            mAllDiagnosis.addHeaderView(headerAllDiagnosis);

            mAllDiagnosis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    OnListItemClick(adapterView, view, i, l);
                }
            });

            etDiagnosis.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() > 0) {
                        mRecentDiagnosis.setVisibility(View.GONE);
                    } else {
                        mRecentDiagnosis.setVisibility(View.VISIBLE);
                    }
                    diagnosisAdapter.getFilter().filter(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        } else {
            mAllDiagnosis.setVisibility(View.INVISIBLE);
        }

        return rootView;
    }

    private void OnListItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < 1)
            return;
        if (TextUtils.isEmpty(((TextView) view.findViewById(R.id.title)).getText())) {
            return;
        }
        etDiagnosis.setText(((TextView) view.findViewById(R.id.title)).getText());
    }
}