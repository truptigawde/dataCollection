package org.cabi.pdc.fragments;

import android.app.AlertDialog;
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
import org.cabi.pdc.RoomDb.RoomDB_ClinicCode;
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

public class ClinicDetailsFragment extends Fragment {

    private Context mContext;
    private ListView mAllPlantClinicCodes;
    private ArrayList<String> mAllPlantClinicCodesList = new ArrayList<>();
    private ListView mRecentPlantClinicCodes;
    private ArrayList<String> mRecentPlantClinicCodesList = new ArrayList<>();
    DCAApplication dcaApplication;
    private EditText etClinicCode;

    public ClinicDetailsFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        dcaApplication = (DCAApplication) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dcaApplication = (DCAApplication) getActivity().getApplication();

        View rootView = inflater.inflate(R.layout.fragment_clinic_details, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            Section section = (Section) bundle.getSerializable("Section");
            final int sectionPos = bundle.getInt("SectionPosition", -1);
            final int fragmentId = this.getId();
            final boolean fromSummary = bundle.getBoolean("FromSummary");

            if (section.getFields() != null && section.getFields().size() > 0) {

                for (int i = 0; i < section.getFields().size(); i++) {
                    switch (section.getFields().get(i).getFieldType()) {
                        case 25: {
                            TextView tvBaseTitleClinicCode = rootView.findViewById(R.id.baseTitleClinicCode);
                            tvBaseTitleClinicCode.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());

                            etClinicCode = rootView.findViewById(R.id.etPlantClinicCode);
                            etClinicCode.setHint(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getHintTranslationId()).getValue());
                            if (ApiData.getInstance().getCurrentForm() != null) {
                                if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getClinicCode())) {
                                    etClinicCode.setText(ApiData.getInstance().getCurrentForm().getClinicCode());
                                }
                            }
                        }
                        break;
                        case 10: {
                            TextView tvNextButton = rootView.findViewById(R.id.btnTextNextClinicCode);
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
                            rootView.findViewById(R.id.btnNextClinicCode).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (sectionPos > -1) {
                                        String clinicCode = etClinicCode.getText().toString().trim();
                                        if (TextUtils.isEmpty(clinicCode)) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                            builder.setMessage("Please enter a clinic code to proceed")
                                                    .setCancelable(true)
                                                    .setPositiveButton("Ok", null);
                                            builder.create().show();
                                        } else {
                                            if (ApiData.getInstance().getCurrentForm() != null) {
                                                ApiData.getInstance().getCurrentForm().setClinicCode(clinicCode.toUpperCase());
                                                if (fromSummary) {
                                                    ((FormActivity) getActivity()).loadNextSection(dcaApplication.getSectionList().size() + 1, fragmentId, true);

                                                } else {
                                                    ((FormActivity) getActivity()).loadNextSection(sectionPos + 1, fragmentId, false);
                                                }
                                            }
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
        final String strRecentCC = sp.getString("Recent_Clinic_Code", "");
        if (!TextUtils.isEmpty(strRecentCC)) {
            try {
                JSONArray jArrRecentCC = new JSONArray(strRecentCC);
                for (int i = 0; i < jArrRecentCC.length(); i++) {
                    if (!mRecentPlantClinicCodesList.contains(jArrRecentCC.getString(i))) {
                        mRecentPlantClinicCodesList.add(jArrRecentCC.getString(i));
                    }
                }
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }

        mRecentPlantClinicCodes = rootView.findViewById(R.id.lvRecentPlantClinicCodes);
        if (mRecentPlantClinicCodesList.size() > 0) {
            final ArrayAdapter<String> recentCCAdapter = new ArrayAdapter<String>(getActivity(), R.layout.generic_listview_item, R.id.title, mRecentPlantClinicCodesList);
            mRecentPlantClinicCodes.setAdapter(recentCCAdapter);
            View headerViewRecentClinicCode = inflater.inflate(R.layout.generic_listview_header, container, false);
            TextView headerTextRecent = headerViewRecentClinicCode.findViewById(R.id.headerText);
            String recentCCHeader = "Recent clinic codes";
            if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("ClinicCodeRecent")) {
                recentCCHeader = ApiData.getInstance().getMetadataTranslationsList().get("ClinicCodeRecent").getValue();
            }
            headerTextRecent.setText(recentCCHeader);
            mRecentPlantClinicCodes.addHeaderView(headerViewRecentClinicCode);
            mRecentPlantClinicCodes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    OnListItemClick(adapterView, view, i, l);
                }
            });
        } else {
            mRecentPlantClinicCodes.setVisibility(View.GONE);
        }

        if (dcaApplication.getClinicCodeMetadata() != null && dcaApplication.getClinicCodeMetadata().size() > 0) {
            for (Metadata item : dcaApplication.getClinicCodeMetadata()) {
                if (!mAllPlantClinicCodesList.contains(item.getName())) {
                    mAllPlantClinicCodesList.add(item.getName());
                }
            }
        }

        List<RoomDB_ClinicCode> cliniclist = ((FormActivity) getActivity()).dcaFormDatabase.dcaClinicCodeDao().getAllClinicCodes();
        if (cliniclist != null && cliniclist.size() > 0) {
            for (RoomDB_ClinicCode clinic : cliniclist) {
                if (!mAllPlantClinicCodesList.contains(clinic.getClinicCode())) {
                    mAllPlantClinicCodesList.add(clinic.getClinicCode());
                }
            }
        }

        mAllPlantClinicCodes = rootView.findViewById(R.id.lvAllPlantClinicCodes);
        if (mAllPlantClinicCodesList.size() > 0) {
            final ArrayAdapter<String> clinicCodeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.generic_listview_item, R.id.title, mAllPlantClinicCodesList);
            mAllPlantClinicCodes.setAdapter(clinicCodeAdapter);

            View headerAllClinicCodes = inflater.inflate(R.layout.generic_listview_header, container, false);
            TextView headerTextAllPCCodes = headerAllClinicCodes.findViewById(R.id.headerText);
            String allCCHeader = "All clinic codes:";
            if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("ClinicCodeAll")) {
                allCCHeader = ApiData.getInstance().getMetadataTranslationsList().get("ClinicCodeAll").getValue();
            }
            headerTextAllPCCodes.setText(allCCHeader);
            mAllPlantClinicCodes.addHeaderView(headerAllClinicCodes);

            mAllPlantClinicCodes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    OnListItemClick(adapterView, view, i, l);
                }
            });

            etClinicCode.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() > 0) {
                        mRecentPlantClinicCodes.setVisibility(View.GONE);
                    } else {
                        mRecentPlantClinicCodes.setVisibility(View.VISIBLE);
                    }
                    clinicCodeAdapter.getFilter().filter(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        } else {
            mAllPlantClinicCodes.setVisibility(View.INVISIBLE);
        }

        return rootView;
    }

    private void OnListItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < 1)
            return;
        if (TextUtils.isEmpty(((TextView) view.findViewById(R.id.title)).getText())) {
            return;
        }
        etClinicCode.setText(((TextView) view.findViewById(R.id.title)).getText());
    }
}