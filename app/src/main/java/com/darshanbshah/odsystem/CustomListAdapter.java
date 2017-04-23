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
        TextView tv, tv1;
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
            handler.tv1 = (TextView)row.findViewById(R.id.listTextEmail);
            row.setTag(handler);
        }
        else {
            handler = (DataHandler) row.getTag();
        }

//        Typeface one = Typeface.createFromAsset(parent.getContext().getAssets(), "fonts/BebasNeue Bold.ttf");
        DataProvider provider = (DataProvider) this.getItem(position);
        handler.tv.setText(provider.getRollno());
        handler.tv1.setText(provider.getReason());
//        handler.tv.setTypeface(one);
        handler.tv.setTextSize(20);
        return row;
    }
}
