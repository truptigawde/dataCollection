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
import org.cabi.pdc.RoomDb.RoomDB_PlantDoctor;
import org.cabi.pdc.activities.FormActivity;
import org.cabi.pdc.adapters.GenericListAdapter;
import org.cabi.pdc.common.ApiData;
import org.cabi.pdc.common.DCAApplication;
import org.cabi.pdc.models.Metadata;
import org.cabi.pdc.models.Section;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class PlantDoctorFragment extends Fragment {

    private Context mContext;
    private ListView mRecentPlantDoctorNames;
    private ListView mAllPlantDoctorNames;
    private ArrayList<String> mRecentPlantDoctorNameList = new ArrayList<>();
    private ArrayList<String> mAllPlantDoctorNameList = new ArrayList<>();
    private EditText etPDName;
    DCAApplication dcaApplication;

    public PlantDoctorFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;

        dcaApplication = (DCAApplication) getActivity().getApplication();
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        if(etPDName!=null && !TextUtils.isEmpty(etPDName.getText().toString())) {
//            outState.putString("pd_name", etPDName.getText().toString());
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_plant_doctor, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            final int sectionPos = bundle.getInt("SectionPosition", -1);
            final int fragmentId = this.getId();
            final boolean fromSummary = bundle.getBoolean("FromSummary");
            Section section = (Section) bundle.getSerializable("Section");

            if (section != null && section.getFields() != null && section.getFields().size() > 0) {

                for (int i = 0; i < section.getFields().size(); i++) {
                    switch (section.getFields().get(i).getFieldType()) {
                        case 25: {
                            TextView tvBaseTitlePDName = rootView.findViewById(R.id.baseTitlePDName);
                            tvBaseTitlePDName.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());

                            etPDName = rootView.findViewById(R.id.etPlantDoctorName);
                            etPDName.setHint(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getHintTranslationId()).getValue());
                            if (ApiData.getInstance().getCurrentForm() != null) {
                                if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getPlantDoctor())) {
                                    etPDName.setText(ApiData.getInstance().getCurrentForm().getPlantDoctor());
                                }
                            }
                        }
                        break;
                        case 10: {
                            TextView tvNextButton = rootView.findViewById(R.id.btnTextNextPDName);
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

                            rootView.findViewById(R.id.btnNextPDName).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (sectionPos > -1) {
                                        if (ApiData.getInstance().getCurrentForm() != null) {
                                            String pdName = etPDName.getText().toString().trim();
                                            ApiData.getInstance().getCurrentForm().setPlantDoctor(pdName);
                                        }
                                    }

                                    if (fromSummary) {
                                        ((FormActivity) getActivity()).loadNextSection(dcaApplication.getSectionList().size() + 1, fragmentId, true);

                                    } else {
                                        ((FormActivity) getActivity()).loadNextSection(sectionPos + 1, fragmentId, false);
                                    }
//                                        EditText etPDName = (EditText) getView().findViewById(R.id.etPlantDoctorName);
                                }
                            });
//                            tvNextButton.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    if (sectionPos > -1) {
//                                        if (ApiData.getInstance().getCurrentForm() == null) {
//                                            ApiData.getInstance().setCurrentForm(new RoomDb_Form());
//                                        }
//                                        EditText etPDName = (EditText) getView().findViewById(R.id.etPlantDoctorName);
//                                        String pdName = etPDName.getText().toString().trim();
//                                        ApiData.getInstance().getCurrentForm().setPlantDoctor(pdName);
//                                        ((FormActivity) getActivity()).loadNextSection(sectionPos + 1, fragmentId);
//                                    }
//                                }
//                            });
                        }
                        break;
                    }
                }
            }
        }

        SharedPreferences sp = getActivity().getSharedPreferences("FormData", MODE_PRIVATE);
        final String strRecentPD = sp.getString("Recent_Plant_Doctor", "");
        if (!TextUtils.isEmpty(strRecentPD)) {
            try {
                JSONArray jArrRecentPD = new JSONArray(strRecentPD);
                for (int i = 0; i < jArrRecentPD.length(); i++) {
                    if (!mRecentPlantDoctorNameList.contains(jArrRecentPD.getString(i))) {
                        mRecentPlantDoctorNameList.add(jArrRecentPD.getString(i));
                    }
                }
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }

        mRecentPlantDoctorNames = rootView.findViewById(R.id.lvRecentPlantDoctorNames);
        if (mRecentPlantDoctorNameList.size() > 0) {
            GenericListAdapter recentPDNamesAdapter = new GenericListAdapter(getActivity(), mRecentPlantDoctorNameList);
//            mRecentPlantDoctorNames.setAdapter(recentPDNamesAdapter);
            final ArrayAdapter<String> recentPDAdapter = new ArrayAdapter<String>(getActivity(), R.layout.generic_listview_item, R.id.title, mRecentPlantDoctorNameList);
            mRecentPlantDoctorNames.setAdapter(recentPDAdapter);
            View headerViewRecentPDNames = inflater.inflate(R.layout.generic_listview_header, container, false);
            TextView headerTextRecent = headerViewRecentPDNames.findViewById(R.id.headerText);
            String recentPDHeader = "Recent plant doctors:";
            if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("PlantDoctorRecent")) {
                recentPDHeader = ApiData.getInstance().getMetadataTranslationsList().get("PlantDoctorRecent").getValue();
            }
            headerTextRecent.setText(recentPDHeader);
            mRecentPlantDoctorNames.addHeaderView(headerViewRecentPDNames);
            mRecentPlantDoctorNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    OnListItemClick(adapterView, view, i, l);
                }
            });
        } else {
            mRecentPlantDoctorNames.setVisibility(View.GONE);
        }

        if (dcaApplication.getPlantDoctorMetadata() != null && dcaApplication.getPlantDoctorMetadata().size() > 0) {
            for (Metadata item : dcaApplication.getPlantDoctorMetadata()) {
                if (!mAllPlantDoctorNameList.contains(item.getName())) {
                    mAllPlantDoctorNameList.add(item.getName());
                }
            }
        }

        List<RoomDB_PlantDoctor> pdlist = ((FormActivity) getActivity()).dcaFormDatabase.dcaPlantDoctorDao().getAllPlantDoctors();
        if (pdlist != null && pdlist.size() > 0) {
            for (RoomDB_PlantDoctor pd : pdlist) {
                if (!mAllPlantDoctorNameList.contains(pd.getPlantDoctorName())) {
                    mAllPlantDoctorNameList.add(pd.getPlantDoctorName());
                }
            }
        }

        mAllPlantDoctorNames = rootView.findViewById(R.id.lvAllPlantDoctorNames);
        if (mAllPlantDoctorNameList.size() > 0) {
            GenericListAdapter allPDNamesAdapter = new GenericListAdapter(getActivity(), mAllPlantDoctorNameList);
//        mAllPlantDoctorNames.setAdapter(allPDNamesAdapter);
            final ArrayAdapter<String> plantDoctorAdapter = new ArrayAdapter<String>(getActivity(), R.layout.generic_listview_item, R.id.title, mAllPlantDoctorNameList);
            mAllPlantDoctorNames.setAdapter(plantDoctorAdapter);

            View headerViewAllPDNames = inflater.inflate(R.layout.generic_listview_header, container, false);
            TextView headerTextAll = headerViewAllPDNames.findViewById(R.id.headerText);
            String allPDHeader = "All plant doctors:";
            if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("PlantDoctorAll")) {
                allPDHeader = ApiData.getInstance().getMetadataTranslationsList().get("PlantDoctorAll").getValue();
            }
            headerTextAll.setText(allPDHeader);
            mAllPlantDoctorNames.addHeaderView(headerViewAllPDNames);

            mAllPlantDoctorNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    OnListItemClick(adapterView, view, i, l);
                }
            });

            etPDName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() > 0) {
                        mRecentPlantDoctorNames.setVisibility(View.GONE);
                    } else {
                        mRecentPlantDoctorNames.setVisibility(View.VISIBLE);
                    }
                    plantDoctorAdapter.getFilter().filter(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        } else {
            mAllPlantDoctorNames.setVisibility(View.INVISIBLE);
        }

        return rootView;
    }

    private void OnListItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < 1)
            return;
        if (TextUtils.isEmpty(((TextView) view.findViewById(R.id.title)).getText())) {
            return;
        }
        etPDName.setText(((TextView) view.findViewById(R.id.title)).getText());
    }
}