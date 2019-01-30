package org.cabi.pdc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.cabi.pdc.R;

import java.util.ArrayList;

public class GenericListAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<String> mListItems;

    public GenericListAdapter(Context context, ArrayList<String> listItems) {
        mContext = context;
        mListItems = listItems;
    }

    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.generic_listview_item, null);
        } else {
            view = convertView;
        }

        TextView titleText = view.findViewById(R.id.title);
        titleText.setText(mListItems.get(position).toString());

        return view;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mListItems.get(position);
    }
}