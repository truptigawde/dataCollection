package org.cabi.pdc.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import org.cabi.pdc.R;
import org.cabi.pdc.viewmodels.PreviousRecommendationDetails;

import java.util.ArrayList;

public class FindRecommendationListAdapter extends ArrayAdapter {
    Context mContext;
    ArrayList<PreviousRecommendationDetails> mFindRecommendationsListItems = new ArrayList<>();

    public FindRecommendationListAdapter(Context context, ArrayList<PreviousRecommendationDetails> listItems) {
        super(context,R.layout.find_recommendation_listview_item, R.id.prevRecommendationCurrentControl);
        mContext = context;
        mFindRecommendationsListItems = listItems;
    }

    @Override
    public int getCount() {
        return mFindRecommendationsListItems.size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mFindRecommendationsListItems.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.find_recommendation_listview_item, null);
        } else {
            view = convertView;
        }

        TextView tvFRCurrentControl = view.findViewById(R.id.prevRecommendationCurrentControl);
        tvFRCurrentControl.setText(mFindRecommendationsListItems.get(position).getRecommendationToManageNow());
        TextView tvFRFuturePrevention = view.findViewById(R.id.prevRecommendationFuturePrevention);
        tvFRFuturePrevention.setText(mFindRecommendationsListItems.get(position).getRecommendationForFuturePrevention());

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return super.getFilter();
    }
}