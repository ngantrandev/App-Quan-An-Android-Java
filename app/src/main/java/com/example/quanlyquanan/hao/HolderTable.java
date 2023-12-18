package com.example.quanlyquanan.hao;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquanan.R;

public class HolderTable extends RecyclerView.ViewHolder {
    ImageView thumb;
    TextView tableTitle,tableNote,tableStatus,tableLabelStatus;
    Button btnSel,btnNote,btnReturn;
    CardView itemlayer;
    public HolderTable(@NonNull View itemView){
        super(itemView);
        itemlayer = itemView.findViewById(R.id.itemTableContainer);
        thumb = itemView.findViewById(R.id.itemTableThumb);
        tableTitle = itemView.findViewById(R.id.itemTableName);
        tableNote = itemView.findViewById(R.id.itemTableNote);
        tableLabelStatus = itemView.findViewById(R.id.itemTableLabelStatus);
        tableStatus = itemView.findViewById(R.id.itemTableStatus);
        btnSel = itemView.findViewById(R.id.itemTableBtnSel);
        btnNote = itemView.findViewById(R.id.itemTableBtnNote);
        btnReturn = itemView.findViewById(R.id.itemTableBtnReturn);

    }
}
