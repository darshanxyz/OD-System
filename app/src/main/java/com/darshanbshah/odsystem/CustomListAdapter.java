package com.darshanbshah.odsystem;

/**
 * Created by iamDarshan on 22/04/17.
 */
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter extends ArrayAdapter {


    static class DataHandler {
        TextView tv, tv1, tv2, tv3, tv4, tv5, tv6;
    }

    List list = new ArrayList();
    public CustomListAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void add(Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public void remove(Object object) {
        super.remove(object);
//        DataProvider test = (DataProvider)object;
//        Log.e("list debug",test.getKey());
        list.remove(object);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        final DataHandler handler;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_item, parent, false);
            handler = new DataHandler();
            handler.tv = (TextView)row.findViewById(R.id.listText);
            handler.tv1 = (TextView)row.findViewById(R.id.listTextReason);
            handler.tv2 = (TextView)row.findViewById(R.id.fromText);
            handler.tv3 = (TextView)row.findViewById(R.id.toText);
            handler.tv4 = (TextView)row.findViewById(R.id.reasonLabel);
            handler.tv5 = (TextView)row.findViewById(R.id.fromLabel);
            handler.tv6 = (TextView)row.findViewById(R.id.toLabel);
            row.setTag(handler);
        }
        else {
            handler = (DataHandler) row.getTag();
        }

        Typeface one = Typeface.createFromAsset(parent.getContext().getAssets(), "fonts/AvenirLTStd-Book.otf");
        DataProvider provider = (DataProvider) this.getItem(position);
        handler.tv.setText(provider.getRollno());
        handler.tv1.setText(provider.getReason());
        handler.tv2.setText(provider.getFrom());
        handler.tv3.setText(provider.getTo());
        handler.tv.setTypeface(one);
        handler.tv1.setTypeface(one);
        handler.tv2.setTypeface(one);
        handler.tv3.setTypeface(one);
        handler.tv4.setTypeface(one);
        handler.tv5.setTypeface(one);
        handler.tv6.setTypeface(one);
        handler.tv.setTextSize(30);
        handler.tv1.setTextSize(20);
        handler.tv2.setTextSize(20);
        handler.tv3.setTextSize(20);
        return row;
    }
}
