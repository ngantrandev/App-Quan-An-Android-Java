package com.example.quanlyquanan.hao;

import android.graphics.Paint;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquanan.R;

public class HolderFood extends RecyclerView.ViewHolder{
    ImageView thumb;
    TextView name,note,amount,discount,price,remains;
    Button btnnote;
    ImageButton btnadd,btnminus;
    CardView itemlayer;
    public HolderFood(@NonNull View itemView) {
        super(itemView);
        itemlayer = itemView.findViewById(R.id.itemFoodContainer);
        name = itemView.findViewById(R.id.itemFoodName);
        thumb = itemView.findViewById(R.id.itemFoodThumb);
        note = itemView.findViewById(R.id.itemFoodNote);
        amount = itemView.findViewById(R.id.itemFoodNumber);
        discount = itemView.findViewById(R.id.itemFoodOldPrice);
        discount.setPaintFlags(discount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        price = itemView.findViewById(R.id.itemFoodPrice);
        remains = itemView.findViewById(R.id.itemFoodRemains);
        btnadd = itemView.findViewById(R.id.itemFoodBtnAdd);
        btnminus = itemView.findViewById(R.id.itemFoodBtnMinus);
        btnnote = itemView.findViewById(R.id.itemFoodBtnNote);
    }
}
