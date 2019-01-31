package org.cabi.pdc.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.cabi.pdc.R;
import org.cabi.pdc.RoomDb.RoomDB_Recommendations;
import org.cabi.pdc.activities.FormActivity;
import org.cabi.pdc.adapters.FindRecommendationListAdapter;
import org.cabi.pdc.common.ApiData;
import org.cabi.pdc.viewmodels.PreviousRecommendationDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FindRecommendationFragment extends Fragment {

    private Context mContext;
    private ListView mAllRecommendations;
    private ArrayList<PreviousRecommendationDetails> mAllRecommendationsList = new ArrayList<>();
    private ListView mRecentRecommendations;
    private ArrayList<PreviousRecommendationDetails> mRecentRecommendationsList = new ArrayList<>();
    private EditText etPrevRecommendation;

    public FindRecommendationFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.mContext = getActivity();

        View rootView = inflater.inflate(R.layout.fragment_find_recommendation, container, false);

        etPrevRecommendation = rootView.findViewById(R.id.etPreviousRecommendation);

        List<RoomDB_Recommendations> recommendationsList = ((FormActivity) getActivity()).dcaFormDatabase.dcaRecommendationDao().getAllRecommendations();

        if (recommendationsList != null && recommendationsList.size() > 0) {
            for (RoomDB_Recommendations recomItem : recommendationsList) {
                if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getCropName()) && ApiData.getInstance().getCurrentForm().getCropName().toUpperCase().equals(recomItem.getCrop().toUpperCase())) {
                    PreviousRecommendationDetails prevDetail = new PreviousRecommendationDetails();
                    prevDetail.setRecommendationToManageNow(recomItem.getRecommendationToManageNow());
                    prevDetail.setRecommendationToManageNowAltLang(recomItem.getRecommendationToManageNowAltLang());
                    prevDetail.setRecommendationForFuturePrevention(recomItem.getRecommendationForFuturePrevention());
                    prevDetail.setRecommendationForFuturePreventionAltLang(recomItem.getRecommendationForFuturePreventionAltLang());
                    prevDetail.setLastUsed(recomItem.getLastUsed());
                    mAllRecommendationsList.add(prevDetail);
                }
            }
        }

        mAllRecommendations = rootView.findViewById(R.id.lvAllRecommendations);
        if(mAllRecommendationsList.size()>0) {
            final FindRecommendationListAdapter previousRecommendationsAdapter = new FindRecommendationListAdapter(getActivity(), mAllRecommendationsList);
            mAllRecommendations.setAdapter(previousRecommendationsAdapter);
            View headerViewAllRecommendations = inflater.inflate(R.layout.generic_listview_header, container, false);
            TextView headerTextAll = headerViewAllRecommendations.findViewById(R.id.headerText);
            headerTextAll.setText("All recommendations:");
            mAllRecommendations.addHeaderView(headerViewAllRecommendations);

            mAllRecommendations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    OnListItemClick(adapterView, view, i, l);
                }
            });

            etPrevRecommendation.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() > 0) {
                        mRecentRecommendations.setVisibility(View.GONE);
                    } else {
                        mRecentRecommendations.setVisibility(View.VISIBLE);
                    }
                    previousRecommendationsAdapter.getFilter().filter(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }else {
            mAllRecommendations.setVisibility(View.INVISIBLE);
        }

        mRecentRecommendations = rootView.findViewById(R.id.lvRecentRecommendations);

        if (mAllRecommendationsList != null && mAllRecommendationsList.size() > 0) {
            mRecentRecommendationsList = mAllRecommendationsList;

            Collections.sort(mRecentRecommendationsList, new Comparator<PreviousRecommendationDetails>() {
                @Override
                public int compare(PreviousRecommendationDetails p1, PreviousRecommendationDetails p2) {
//                    int order1 = section.getOrder();
//                    int order2 = t1.getOrder();
//                    if (order1 < order2) {
//                        return 1;
//                    } else {
//                        return 0;
//                    }
                    return p2.getLastUsed().compareToIgnoreCase(p1.getLastUsed());
                }
            });

            if (mRecentRecommendationsList.size() > 2) {
                mRecentRecommendationsList = new ArrayList<PreviousRecommendationDetails>(mRecentRecommendationsList.subList(0, 3));
            } else {
                mRecentRecommendationsList = new ArrayList<PreviousRecommendationDetails>(mRecentRecommendationsList.subList(0, mRecentRecommendationsList.size()));
            }

            FindRecommendationListAdapter recentRecommendationsAdapter = new FindRecommendationListAdapter(getActivity(), mRecentRecommendationsList);
            mRecentRecommendations.setAdapter(recentRecommendationsAdapter);
            View headerViewRecentRecom = inflater.inflate(R.layout.generic_listview_header, container, false);
            TextView headerTextRecent = headerViewRecentRecom.findViewById(R.id.headerText);
            headerTextRecent.setText("Recent recommendations:");
            mRecentRecommendations.addHeaderView(headerViewRecentRecom);

            mRecentRecommendations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    OnListItemClick(adapterView, view, i, l);
                }
            });
        } else {
            mRecentRecommendations.setVisibility(View.INVISIBLE);
        }

        RelativeLayout btnNextFindRecommendation = rootView.findViewById(R.id.btnNextFindRecommendation);
        btnNextFindRecommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ApiData.getInstance().getCurrentForm() != null) {
                    ApiData.getInstance().getCurrentForm().setRecommendationToManageNow(etPrevRecommendation.getText().toString());
                    ApiData.getInstance().getCurrentForm().setRecommendationForFuturePrevention(etPrevRecommendation.getText().toString());
                }

                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStackImmediate();
                }

                if (getFragmentManager() != null & getFragmentManager().getBackStackEntryCount() > 0) {
                    TextView sectionTitle = ((FormActivity) getActivity()).findViewById(R.id.tvSectionTitle);
                    sectionTitle.setText(getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1).getName());
                }
            }
        });

        return rootView;
    }

    private void OnListItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (position < 1)
            return;
        if (TextUtils.isEmpty(((TextView) view.findViewById(R.id.prevRecommendationCurrentControl)).getText())) {
            return;
        }
        etPrevRecommendation.setText(((TextView) view.findViewById(R.id.prevRecommendationCurrentControl)).getText());
    }
}