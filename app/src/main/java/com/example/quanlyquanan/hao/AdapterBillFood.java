package com.example.quanlyquanan.hao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquanan.R;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterBillFood extends RecyclerView.Adapter<HolderFoodBill> {
    Context context;
    List<FoodInfo> data;
    int idtable;
    public AdapterBillFood(Context context, List<FoodInfo> data,int idtable){
        this.context = context;
        this.data = data;
        this.idtable = idtable;
    }
    @NonNull
    @Override
    public HolderFoodBill onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemfoodbill,parent,false);
        return new HolderFoodBill(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderFoodBill holder, int position) {
        FoodInfo Food = data.get(position);
        holder.name.setText(Food.getFoodName());
        DecimalFormat f = new DecimalFormat("#,###");
        holder.price.setText(Food.getFoodSelAmount()+" X "+f.format(Food.getFoodPrice())+" VNĐ");
        holder.note.setText(Food.getFoodNote());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
