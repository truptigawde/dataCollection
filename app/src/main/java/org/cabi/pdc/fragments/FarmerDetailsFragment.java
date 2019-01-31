package org.cabi.pdc.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.cabi.pdc.R;
import org.cabi.pdc.RoomDb.RoomDB_FarmerDetails;
import org.cabi.pdc.RoomDb.RoomDb_Form;
import org.cabi.pdc.activities.FormActivity;
import org.cabi.pdc.adapters.FarmerDetailsListAdapter;
import org.cabi.pdc.common.ApiData;
import org.cabi.pdc.models.Section;
import org.cabi.pdc.viewmodels.FarmerDetails;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FarmerDetailsFragment extends Fragment {

    private Context mContext;
    private ListView mAllFarmerNames;
    private ArrayList<FarmerDetails> mAllFarmerNamesList = new ArrayList<>();
    private ListView mRecentFarmerNames;
    private ArrayList<FarmerDetails> mRecentFarmerNamesList = new ArrayList<>();
    EditText etFarmerName;

    public FarmerDetailsFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.mContext = getActivity();

        View rootView = inflater.inflate(R.layout.fragment_farmer_details, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            final int sectionPos = bundle.getInt("SectionPosition", -1);
            final int fragmentId = this.getId();
            Section section = (Section) bundle.getSerializable("Section");
            if (section != null && section.getFields() != null && section.getFields().size() > 0) {
                for (int i = 0; i < section.getFields().size(); i++) {
                    switch (section.getFields().get(i).getFieldType()) {
                        case 25: {
                            TextView farmer = rootView.findViewById(R.id.baseTitleFarmer);
                            farmer.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());

                            etFarmerName = rootView.findViewById(R.id.etFarmerName);
                            etFarmerName.setHint(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getHintTranslationId()).getValue());
                            if (ApiData.getInstance().getCurrentForm() != null) {
                                if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getFarmerName())) {
                                    etFarmerName.setText(ApiData.getInstance().getCurrentForm().getFarmerName());
                                }
                            }
                        }
                        break;
                        case 10: {
                            TextView btnTextNext = rootView.findViewById(R.id.btnTextNextFarmer);
                            btnTextNext.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());
                            rootView.findViewById(R.id.btnNextFarmer).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (sectionPos > -1) {
                                        if (ApiData.getInstance().getCurrentForm() == null) {
                                            ApiData.getInstance().setCurrentForm(new RoomDb_Form());
                                        }
                                        EditText etFarmerName = (EditText) getView().findViewById(R.id.etFarmerName);
                                        String farmerName = etFarmerName.getText().toString().trim();
                                        if (TextUtils.isEmpty(farmerName)) {
                                            ApiData.getInstance().getCurrentForm().setFarmerName("");
                                            ApiData.getInstance().getCurrentForm().setFarmerPhoneNumber("");
                                            ApiData.getInstance().getCurrentForm().setFarmerGender("");
                                            ApiData.getInstance().getCurrentForm().setFarmerLocation1("");
                                            ApiData.getInstance().getCurrentForm().setFarmerLocation2("");
                                            ApiData.getInstance().getCurrentForm().setFarmerLocation3("");
                                        } else {
                                            ApiData.getInstance().getCurrentForm().setFarmerName(farmerName);
                                        }
                                        ((FormActivity) getActivity()).loadNextSection(sectionPos + 1, fragmentId, false);
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
        final String strRecentFarmer = sp.getString("Recent_Farmer", "");
        if (!TextUtils.isEmpty(strRecentFarmer)) {
            try {
                JSONArray jArrRecentFarmer = new JSONArray(strRecentFarmer);

                ArrayList<String> recentFarmerNames = new ArrayList<>();
                for (FarmerDetails fItem : mRecentFarmerNamesList) {
                    recentFarmerNames.add(fItem.getFarmerName());
                }

                for (int i = 0; i < jArrRecentFarmer.length(); i++) {
                    if (!recentFarmerNames.contains(jArrRecentFarmer.getString(i))) {
                        recentFarmerNames.add(jArrRecentFarmer.getString(i));
                    }
                }

                mRecentFarmerNamesList = new ArrayList<>();
                List<RoomDB_FarmerDetails> allFarmers = ((FormActivity) getActivity()).dcaFormDatabase.dcaFarmerDao().getAllFarmerDetails();
                if (allFarmers != null && allFarmers.size() > 0 && recentFarmerNames.size() > 0) {
                    for (RoomDB_FarmerDetails item : allFarmers) {
                        if (recentFarmerNames.contains(item.getFarmerName())) {
                            FarmerDetails fDetail = new FarmerDetails();
                            fDetail.setFarmerName(item.getFarmerName());
                            fDetail.setFarmerGender(item.getFarmerGender());
                            fDetail.setFarmerPhoneNumber(item.getFarmerPhoneNumber());
                            fDetail.setFarmerLocation1(item.getFarmerLocation1());
                            fDetail.setFarmerLocation2(item.getFarmerLocation2());
                            fDetail.setFarmerLocation3(item.getFarmerLocation3());

                            mRecentFarmerNamesList.add(fDetail);
                        }
                    }
                }

            } catch (JSONException je) {
                je.printStackTrace();
            }
        }

        mRecentFarmerNames = rootView.findViewById(R.id.lvRecentFarmerNames);
        if (mRecentFarmerNamesList.size() > 0) {
            final FarmerDetailsListAdapter recentFarmerAdapter = new FarmerDetailsListAdapter(getActivity(), mRecentFarmerNamesList);
            mRecentFarmerNames.setAdapter(recentFarmerAdapter);
            View headerViewRecentFarmer = inflater.inflate(R.layout.generic_listview_header, container, false);
            TextView headerTextFarmer = headerViewRecentFarmer.findViewById(R.id.headerText);
            String recentFarmerHeader = "Recent farmer details:";
            if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("FarmerDetailRecent")) {
                recentFarmerHeader = ApiData.getInstance().getMetadataTranslationsList().get("FarmerDetailRecent").getValue();
            }
            headerTextFarmer.setText(recentFarmerHeader);
            mRecentFarmerNames.addHeaderView(headerViewRecentFarmer);
            mRecentFarmerNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    OnListItemClick(adapterView, view, i, l);
                }
            });
        } else {
            mRecentFarmerNames.setVisibility(View.GONE);
        }

//        for (int i = 1; i < 6; i++) {
//            FarmerDetails fd = new FarmerDetails();
//            fd.setFarmerName("Farmer " + i);
//            fd.setFarmerPhoneNumber(String.valueOf(900000 + i));
//            fd.setFarmerGender((i / 2 == 0) ? "Male" : "Female");
//            fd.setFarmerLocation1("County " + i);
//            fd.setFarmerLocation2("Sub-County " + i);
//            fd.setFarmerLocation3("Village " + i);
//
//            if (!mAllFarmerNamesList.contains(fd)) {
//                mAllFarmerNamesList.add(fd);
//            }
//        }

        List<RoomDB_FarmerDetails> farmerlist = ((FormActivity) getActivity()).dcaFormDatabase.dcaFarmerDao().getAllFarmerDetails();
        if (farmerlist != null && farmerlist.size() > 0) {

            for (RoomDB_FarmerDetails item : farmerlist) {
                FarmerDetails fd = new FarmerDetails();
                fd.setFarmerName(item.getFarmerName());
                fd.setFarmerPhoneNumber(item.getFarmerPhoneNumber());
                fd.setFarmerGender(item.getFarmerGender());
                fd.setFarmerLocation1(item.getFarmerLocation1());
                fd.setFarmerLocation2(item.getFarmerLocation2());
                fd.setFarmerLocation3(item.getFarmerLocation3());

                mAllFarmerNamesList.add(fd);
            }
        }

        mAllFarmerNames = rootView.findViewById(R.id.lvAllFarmerNames);
        if (mAllFarmerNamesList.size() > 0) {
            FarmerDetailsListAdapter allFarmerNamesAdapter = new FarmerDetailsListAdapter(getActivity(), mAllFarmerNamesList);
            mAllFarmerNames.setAdapter(allFarmerNamesAdapter);

            View headerAllFarmerNames = inflater.inflate(R.layout.generic_listview_header, container, false);
            TextView headerTextAllFarmerNames = headerAllFarmerNames.findViewById(R.id.headerText);
            String allFarmerHeader = "All farmer details:";
            if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("FarmerDetailAll")) {
                allFarmerHeader = ApiData.getInstance().getMetadataTranslationsList().get("FarmerDetailAll").getValue();
            }
            headerTextAllFarmerNames.setText(allFarmerHeader);
            mAllFarmerNames.addHeaderView(headerAllFarmerNames);

            mAllFarmerNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    OnListItemClick(adapterView, view, i, l);
                }
            });
        } else {
            mAllFarmerNames.setVisibility(View.INVISIBLE);
        }

        return rootView;
    }

    private void OnListItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < 1)
            return;
        etFarmerName.setText(mAllFarmerNamesList.get(position - 1).getFarmerName());

        if (ApiData.getInstance().getCurrentForm() != null) {
            ApiData.getInstance().getCurrentForm().setFarmerName(mAllFarmerNamesList.get(position - 1).getFarmerName());
            ApiData.getInstance().getCurrentForm().setFarmerPhoneNumber(mAllFarmerNamesList.get(position - 1).getFarmerPhoneNumber());
            ApiData.getInstance().getCurrentForm().setFarmerGender(mAllFarmerNamesList.get(position - 1).getFarmerGender());
            ApiData.getInstance().getCurrentForm().setFarmerLocation1(mAllFarmerNamesList.get(position - 1).getFarmerLocation1());
            ApiData.getInstance().getCurrentForm().setFarmerLocation2(mAllFarmerNamesList.get(position - 1).getFarmerLocation2());
            ApiData.getInstance().getCurrentForm().setFarmerLocation3(mAllFarmerNamesList.get(position - 1).getFarmerLocation3());
        }
    }
}