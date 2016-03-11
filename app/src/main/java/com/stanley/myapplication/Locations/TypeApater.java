package com.stanley.myapplication.Locations;


import android.content.Context;
import android.support.v4.view.LayoutInflaterFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.stanley.myapplication.R;

public class TypeApater extends ArrayAdapter<String>{

    public TypeApater(Context context, String[] types) {
        super(context, R.layout.custom_typelist, types);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View custeomView = layoutInflater.inflate(R.layout.custom_typelist, parent, false);

        String temp = getItem(position);
        TextView textView = (TextView) custeomView.findViewById(R.id.type_tv);
        textView.setText(temp);


        return custeomView;
    }
}
