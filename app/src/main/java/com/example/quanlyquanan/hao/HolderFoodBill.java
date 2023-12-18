package com.example.quanlyquanan.hao;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquanan.R;

public class HolderFoodBill extends RecyclerView.ViewHolder {
    TextView name,price,note;
    public HolderFoodBill(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.itemFoodBillName);
        price = itemView.findViewById(R.id.itemFoodBillPrice);
        note = itemView.findViewById(R.id.itemFoodBillNote);
    }
}
