package com.example.quanlyquanan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quanlyquanan.R;
import com.example.quanlyquanan.model.Category;
import com.example.quanlyquanan.model.MyDate;

import java.util.List;

public class AdapterSpinnerDate extends ArrayAdapter<MyDate> {

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spinner_selected,parent,false );
        TextView text = convertView.findViewById(R.id.tv_spinner_selected);
        text.setText(this.getItem(position).getYear());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spinner_dropdown,parent,false );
        TextView text = convertView.findViewById(R.id.tv_spinner_dropdown);
        text.setText(this.getItem(position).getYear());
        return convertView;
    }

    public AdapterSpinnerDate(@NonNull Context context, int resource, @NonNull List<MyDate> objects) {
        super(context, resource, objects);
    }
}
