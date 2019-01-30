package org.cabi.pdc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.cabi.pdc.R;

import java.util.ArrayList;

public class GenericCheckBoxListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<String> mCheckBoxListItems = new ArrayList<>();

    public GenericCheckBoxListAdapter(Context context, ArrayList<String> checkBoxListItems) {
        mContext = context;
        mCheckBoxListItems = checkBoxListItems;
    }

    @Override
    public int getCount() {
        return mCheckBoxListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mCheckBoxListItems.get(position);
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
            view = inflater.inflate(R.layout.generic_checkboxlist_item, null);
        } else {
            view = convertView;
        }

//        TextView titleText = view.findViewById(R.id.checkBoxItem);
//        titleText.setText(mCheckBoxListItems.get(position).toString());

        return view;
    }
}