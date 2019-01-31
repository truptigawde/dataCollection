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
import org.cabi.pdc.RoomDb.RoomDB_Location2;
import org.cabi.pdc.RoomDb.RoomDb_Form;
import org.cabi.pdc.activities.FormActivity;
import org.cabi.pdc.common.ApiData;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SubCountyFragment extends Fragment {

    private Context mContext;
    ListView mAllLocation2Names;
    ArrayList<String> mAllLocation2List = new ArrayList<>();
    ListView mRecentLocation2;
    ArrayList<String> mRecentLocation2List = new ArrayList<>();
    EditText etLoc2Name;
    TextView baseTitleLocation2;

    public SubCountyFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.mContext = getActivity();

        final View rootView = inflater.inflate(R.layout.fragment_find_subcounty, container, false);

        baseTitleLocation2 = rootView.findViewById(R.id.baseTitleLocation2);
        String strBaseTitleLoc2 = "Select or enter sub-county";
        if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("SelectLocation")) {
            strBaseTitleLoc2 = ApiData.getInstance().getMetadataTranslationsList().get("SelectLocation").getValue();
        }
        baseTitleLocation2.setText(strBaseTitleLoc2);

        etLoc2Name = rootView.findViewById(R.id.etLocation2);
        String strLoc2Hint = "Start typing sub-county";
        if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("SelectLocationStartTyping")) {
            strLoc2Hint = ApiData.getInstance().getMetadataTranslationsList().get("SelectLocationStartTyping").getValue();
        }
        etLoc2Name.setHint(strLoc2Hint);

        SharedPreferences sp = getActivity().getSharedPreferences("FormData", MODE_PRIVATE);
        final String strRecentLoc2 = sp.getString("Recent_Location2", "");
        if (!TextUtils.isEmpty(strRecentLoc2)) {
            try {
                JSONArray jArrRecentLoc2 = new JSONArray(strRecentLoc2);
                for (int i = 0; i < jArrRecentLoc2.length(); i++) {
                    if (!mRecentLocation2List.contains(jArrRecentLoc2.getString(i))) {
                        mRecentLocation2List.add(jArrRecentLoc2.getString(i));
                    }
                }
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }

        mRecentLocation2 = rootView.findViewById(R.id.lvRecentLocation2);
        if (mRecentLocation2List.size() > 0) {
            final ArrayAdapter<String> recentLocation2Adapter = new ArrayAdapter<String>(getActivity(), R.layout.generic_listview_item, R.id.title, mRecentLocation2List);
            mRecentLocation2.setAdapter(recentLocation2Adapter);
            View headerViewRecentLocation2 = inflater.inflate(R.layout.generic_listview_header, container, false);
            TextView headerTextRecent = headerViewRecentLocation2.findViewById(R.id.headerText);
            String recentLocation2Header = "Recent sub-county";
            if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("LocationRecent")) {
                recentLocation2Header = ApiData.getInstance().getMetadataTranslationsList().get("LocationRecent").getValue();
            }
            headerTextRecent.setText(recentLocation2Header);
            mRecentLocation2.addHeaderView(headerViewRecentLocation2);
            mRecentLocation2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    OnListItemClick(adapterView, view, i, l);
                }
            });
        } else {
            mRecentLocation2.setVisibility(View.GONE);
        }

//        mAllLocation2List.add("New Delhi");

        List<RoomDB_Location2> loc2List = ((FormActivity) getActivity()).dcaFormDatabase.dcaLocation2Dao().getAllLocation2();
        if (loc2List != null && loc2List.size() > 0) {
            for (RoomDB_Location2 subCounty : loc2List) {
                if (subCounty.getLocation1Name().toUpperCase().equals(ApiData.getInstance().getCurrentForm().getFarmerLocation1().toUpperCase()) && !mAllLocation2List.contains(subCounty.getLocation2Name())) {
                    mAllLocation2List.add(subCounty.getLocation2Name());
                }
            }
        }

        mAllLocation2Names = rootView.findViewById(R.id.lvAllLocation2);
        if (mAllLocation2List.size() > 0) {
            final ArrayAdapter<String> allLocation2Adapter = new ArrayAdapter<>(getActivity(), R.layout.generic_listview_item, R.id.title, mAllLocation2List);
            mAllLocation2Names.setAdapter(allLocation2Adapter);

            View headerAllLocation2Names = inflater.inflate(R.layout.generic_listview_header, container, false);
            TextView headerTextAllLocation2Names = headerAllLocation2Names.findViewById(R.id.headerText);
            String allLoc2Header = "All sub-county";
            if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("LocationAll")) {
                allLoc2Header = ApiData.getInstance().getMetadataTranslationsList().get("LocationAll").getValue();
            }
            headerTextAllLocation2Names.setText(allLoc2Header);
            mAllLocation2Names.addHeaderView(headerAllLocation2Names);

            mAllLocation2Names.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    OnListItemClick(adapterView, view, i, l);
                }
            });

            etLoc2Name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() > 0) {
                        mRecentLocation2.setVisibility(View.GONE);
                    } else {
                        mRecentLocation2.setVisibility(View.VISIBLE);
                    }
                    allLocation2Adapter.getFilter().filter(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        } else {
            mAllLocation2Names.setVisibility(View.INVISIBLE);
        }

        RelativeLayout rlLocation2NextUpdate = rootView.findViewById(R.id.rlLocation2NextUpdate);
        rlLocation2NextUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ApiData.getInstance().getCurrentForm() == null) {
                    ApiData.getInstance().setCurrentForm(new RoomDb_Form());
                }

                ApiData.getInstance().getCurrentForm().setFarmerLocation2(etLoc2Name.getText().toString());

                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStackImmediate();
                    if (getFragmentManager() != null) {
                        getFragmentManager().popBackStackImmediate();
                    }
                }

                if (getFragmentManager() != null & getFragmentManager().getBackStackEntryCount() > 0) {
                    TextView sectionTitle = ((FormActivity) getActivity()).findViewById(R.id.tvSectionTitle);
                    sectionTitle.setText(getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1).getName());
                }
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
        etLoc2Name.setText(((TextView) view.findViewById(R.id.title)).getText());
    }
}