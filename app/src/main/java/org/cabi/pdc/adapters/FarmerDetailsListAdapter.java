package org.cabi.pdc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.cabi.pdc.R;
import org.cabi.pdc.viewmodels.FarmerDetails;

import java.util.ArrayList;

public class FarmerDetailsListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<FarmerDetails> mFarmerDetailsListItems;

    public FarmerDetailsListAdapter(Context context, ArrayList<FarmerDetails> farmerDetailsListItems) {
        mContext = context;
        mFarmerDetailsListItems = farmerDetailsListItems;
    }

    @Override
    public int getCount() {
        return mFarmerDetailsListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mFarmerDetailsListItems.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.farmer_details_listview_item, null);
        } else {
            view = convertView;
        }

        FarmerDetails farmer = mFarmerDetailsListItems.get(position);

        TextView farmerName = view.findViewById(R.id.farmerName);
        farmerName.setText(farmer.getFarmerName());
        TextView farmerLocation3 = view.findViewById(R.id.farmerLocation3);
        farmerLocation3.setText(farmer.getFarmerLocation3());
        TextView farmerPhoneNumber = view.findViewById(R.id.farmerPhoneNumber);
        farmerPhoneNumber.setText(farmer.getFarmerPhoneNumber());

        return view;
    }
}