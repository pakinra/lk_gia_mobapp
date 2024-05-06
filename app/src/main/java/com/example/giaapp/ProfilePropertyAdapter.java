package com.example.giaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.giaapp.api.LoginResult;

public class ProfilePropertyAdapter extends ArrayAdapter<ProfileProperty> {
    private final LayoutInflater inflater;
    private final int layout;
    private final ArrayList<ProfileProperty> profilePropertyList;

    ProfilePropertyAdapter(Context context, int resource, ArrayList<ProfileProperty> properties) {
        super(context, resource, properties);
        this.profilePropertyList = properties;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final ProfileProperty product = profilePropertyList.get(position);

        viewHolder.propertyName.setText(product.getPropertyName());
        viewHolder.propertyValue.setText(product.getPropertyValue());

        return convertView;
    }
    private static class ViewHolder {
        final TextView propertyName, propertyValue;
        ViewHolder(View view){
            propertyName = view.findViewById(R.id.propertyNameTV);
            propertyValue = view.findViewById(R.id.propertyValueTV);
        }
    }
}
