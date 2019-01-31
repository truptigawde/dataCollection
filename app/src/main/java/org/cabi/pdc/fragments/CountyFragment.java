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
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.cabi.pdc.R;
import org.cabi.pdc.RoomDb.RoomDB_Location1;
import org.cabi.pdc.RoomDb.RoomDb_Form;
import org.cabi.pdc.activities.FormActivity;
import org.cabi.pdc.common.ApiData;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CountyFragment extends Fragment {

    private Context mContext;
    ListView mAllLocation1;
    ArrayList<String> mAllLocation1List = new ArrayList<>();
    ListView mRecentLocation1;
    ArrayList<String> mRecentLocation1List = new ArrayList<>();
    EditText etLoc1Name;
    TextView baseTitleLocation1;

    public CountyFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.mContext = getActivity();

        final View rootView = inflater.inflate(R.layout.fragment_find_county, container, false);

        final int fragmentId = this.getId();

        baseTitleLocation1 = rootView.findViewById(R.id.baseTitleLocation1);
        String strBaseTitleCounty = "Select or enter county";
        if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("SelectCounty")) {
            strBaseTitleCounty = ApiData.getInstance().getMetadataTranslationsList().get("SelectCounty").getValue();
        }
        baseTitleLocation1.setText(strBaseTitleCounty);

        etLoc1Name = rootView.findViewById(R.id.etLocation1);
        String strLoc1Hint = "Start typing county";
        if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("SelectCountyStartTyping")) {
            strLoc1Hint = ApiData.getInstance().getMetadataTranslationsList().get("SelectCountyStartTyping").getValue();
        }
        etLoc1Name.setHint(strLoc1Hint);

        SharedPreferences sp = getActivity().getSharedPreferences("FormData", MODE_PRIVATE);
        final String strRecentLoc1 = sp.getString("Recent_Location1", "");
        if (!TextUtils.isEmpty(strRecentLoc1)) {
            try {
                JSONArray jArrRecentLoc1 = new JSONArray(strRecentLoc1);
                for (int i = 0; i < jArrRecentLoc1.length(); i++) {
                    if (!mRecentLocation1List.contains(jArrRecentLoc1.getString(i))) {
                        mRecentLocation1List.add(jArrRecentLoc1.getString(i));
                    }
                }
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }

        mRecentLocation1 = rootView.findViewById(R.id.lvRecentLocation1);
        if (mRecentLocation1List.size() > 0) {
            final ArrayAdapter<String> recentLocation1Adapter = new ArrayAdapter<String>(getActivity(), R.layout.generic_listview_item, R.id.title, mRecentLocation1List);
            mRecentLocation1.setAdapter(recentLocation1Adapter);
            View headerViewRecentLocation1 = inflater.inflate(R.layout.generic_listview_header, container, false);
            TextView headerTextRecent = headerViewRecentLocation1.findViewById(R.id.headerText);
            String recentLocation1Header = "Recent county";
            if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("CountyRecent")) {
                recentLocation1Header = ApiData.getInstance().getMetadataTranslationsList().get("CountyRecent").getValue();
            }
            headerTextRecent.setText(recentLocation1Header);
            mRecentLocation1.addHeaderView(headerViewRecentLocation1);
            mRecentLocation1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    OnListItemClick(adapterView, view, i, l);
                }
            });
        } else {
            mRecentLocation1.setVisibility(View.GONE);
        }

//        mAllLocation1List.add("Delhi");

        List<RoomDB_Location1> countyList = ((FormActivity) getActivity()).dcaFormDatabase.dcaLocation1Dao().getAllLocation1();
        if (countyList != null && countyList.size() > 0) {
            for (RoomDB_Location1 county : countyList) {
                if (!mAllLocation1List.contains(county.getLocation1Name())) {
                    mAllLocation1List.add(county.getLocation1Name());
                }
            }
        }

        mAllLocation1 = rootView.findViewById(R.id.lvAllLocation1);
        if (mAllLocation1List.size() > 0) {
            final ArrayAdapter<String> allLocation1Adapter = new ArrayAdapter<>(getActivity(), R.layout.generic_listview_item, R.id.title, mAllLocation1List);
            mAllLocation1.setAdapter(allLocation1Adapter);

            View headerAllLocation1Names = inflater.inflate(R.layout.generic_listview_header, container, false);
            TextView headerTextAllLocation1Names = headerAllLocation1Names.findViewById(R.id.headerText);
            String allLoc1Header = "All county:";
            if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("CountyAll")) {
                allLoc1Header = ApiData.getInstance().getMetadataTranslationsList().get("CountyAll").getValue();
            }
            headerTextAllLocation1Names.setText(allLoc1Header);
            mAllLocation1.addHeaderView(headerAllLocation1Names);

            mAllLocation1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    OnListItemClick(adapterView, view, i, l);
                }
            });

            etLoc1Name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() > 0) {
                        mRecentLocation1.setVisibility(View.GONE);
                    } else {
                        mRecentLocation1.setVisibility(View.VISIBLE);
                    }
                    allLocation1Adapter.getFilter().filter(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        } else {
            mAllLocation1.setVisibility(View.INVISIBLE);
        }

        RelativeLayout rlLoc1NextUpdate = rootView.findViewById(R.id.rlLocation1NextUpdate);
        rlLoc1NextUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ApiData.getInstance().getCurrentForm() == null) {
                    ApiData.getInstance().setCurrentForm(new RoomDb_Form());
                }

                ApiData.getInstance().getCurrentForm().setFarmerLocation1(etLoc1Name.getText().toString());

                String selectFarmerFindLocation = "Find sub-county";
                if (ApiData.getInstance().getMetadataTranslationsList() != null && !TextUtils.isEmpty(ApiData.getInstance().getMetadataTranslationsList().get("SelectFarmerFindLocation").getValue())) {
                    selectFarmerFindLocation = ApiData.getInstance().getMetadataTranslationsList().get("SelectFarmerFindLocation").getValue().toString();
                }
                ((FormActivity) getActivity()).loadNextSection(selectFarmerFindLocation, fragmentId);
            }
        });

        return rootView;
    }

    private void OnListItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < 1)
            return;
        if (TextUtils.isEmpty(((TextView) view.findViewById(R.id.title)).getText())) {
            return;
        }
        etLoc1Name.setText(((TextView) view.findViewById(R.id.title)).getText());
    }
}