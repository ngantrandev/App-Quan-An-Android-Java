package com.example.quanlyquanan.hao;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquanan.R;
import com.example.quanlyquanan.model.BillInfo;
import com.example.quanlyquanan.model.Food;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterBillFood extends RecyclerView.Adapter<HolderFoodBill> {
    Context context;
    List<BillInfo> data;
    List<Food> listFood;

    public AdapterBillFood(Context context, List<BillInfo> data, List<Food> listFood) {
        Log.d("ADAPTER", "AdapterBillFood: " + data==null?"data null":"co data");
        this.context = context;
        this.data = data;
        this.listFood = listFood;
    }

    @NonNull
    @Override
    public HolderFoodBill onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemfoodbill, parent, false);
        return new HolderFoodBill(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderFoodBill holder, int position) {
        BillInfo billInfo = data.get(position);

        Log.d("BILLCHECKOUT", "onBindViewHolder: " + billInfo.getFood());

        Food food = Food.findFoodById(billInfo.getFood(), listFood);

        DecimalFormat f = new DecimalFormat("#,###");
        holder.price.setText(billInfo.getQuantity() + " X " + f.format(billInfo.getPrice()) + " VNĐ");
//
        if (food != null) {
            holder.name.setText(food.getName());
            holder.note.setText(food.getDescription());
        } else {
            holder.name.setText("Không xác định");
            holder.note.setText(food.getDescription());
        }
    }

    @Override
    public int getItemCount() {
        Log.d("BILLCHECKOUT", "getItemCount: " + data.size());
        if (data == null) return 0;
        else
            return data.size();
    }
}
