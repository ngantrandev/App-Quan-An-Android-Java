package com.example.quanlyquanan.hao;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquanan.R;
import com.example.quanlyquanan.model.Table;

import java.util.List;

public class AdapterTable extends RecyclerView.Adapter<HolderTable> {
    Context context;
    List<Table> data;
    public AdapterTable(Context context, List<Table> data){
        this.context = context;
        this.data = data;
    }
    @NonNull
    @Override
    public HolderTable onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.item_table_hao,parent,false);
        return new HolderTable(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderTable holder, int position) {
        Table table = data.get(position);
        holder.tableTitle.setText(table.getTableName());

        Log.d("TAG", "onBindViewHolder: " + table.getTableName());
        //Check Button StatusTable
        if(table.getStatus() == 1){
            holder.tableStatus.setText("Đang sử dụng");
            holder.btnReturn.setVisibility(View.VISIBLE);
            holder.itemlayer.setCardBackgroundColor(ContextCompat.getColor(context,R.color.red));
            holder.thumb.setImageResource(R.drawable.baseline_table_bar_24_white);
            holder.tableTitle.setTextColor(ContextCompat.getColor(context,R.color.white));
            holder.tableNote.setTextColor(ContextCompat.getColor(context,R.color.white));
            holder.tableStatus.setTextColor(ContextCompat.getColor(context,R.color.green));
            holder.tableLabelStatus.setTextColor(ContextCompat.getColor(context,R.color.white));
        }else{
            holder.tableStatus.setText("Trống");
            holder.itemlayer.setCardBackgroundColor(ContextCompat.getColor(context,R.color.white));
            holder.thumb.setImageResource(R.drawable.baseline_table_bar_24_black);
            holder.tableTitle.setTextColor(ContextCompat.getColor(context,R.color.black));
            holder.tableNote.setTextColor(ContextCompat.getColor(context,R.color.black));
            holder.tableStatus.setTextColor(ContextCompat.getColor(context,R.color.black));
            holder.tableLabelStatus.setTextColor(ContextCompat.getColor(context,R.color.black));
            holder.btnReturn.setVisibility(View.INVISIBLE);
        }
        if(table.getNote().length()<1){
            holder.tableNote.setText("Thêm ghi chú");
        }else{
            holder.tableNote.setText(table.getNote());
        }
        //Click Button Select Table
        holder.itemlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.btnSel.performClick();
            }
        });
        holder.btnSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                table.setStatus(1);
//                notifyDataSetChanged();
                openFoodMenu(table);
            }
        });
        //Click Button Note Table
        holder.btnNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
//                showdialog(table.getIdTable());
            }
        });
        holder.btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckReturn(table);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void openFoodMenu(Table table){
        Intent intent = new Intent(context,activity_food_menu.class);
        intent.putExtra("table",table);
//        intent.putExtra("dsFood", (Serializable) dsfood);
        context.startActivity(intent);
    }
    public void showdialog(int id){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_button_sheet_note);
        Button btnDone = dialog.findViewById(R.id.btndone);
        EditText notecontent = dialog.findViewById(R.id.notecontent);
        TextView limitvaluetext = dialog.findViewById(R.id.limitvalue);
        notecontent.setTextColor(ContextCompat.getColor(context,R.color.black));
        notecontent.setHintTextColor(ContextCompat.getColor(context,R.color.black));
        notecontent.setFilters(new InputFilter[] { new InputFilter.LengthFilter(100) });
        notecontent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                limitvaluetext.setText(charSequence.length()+"");
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        if (data.get(id).getNote().length()>0){
            notecontent.setText(data.get(id).getNote());
        }
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = "";
                    msg = notecontent.getText().toString();
                    data.get(id).setNote(msg);
                    dialog.cancel();
                    notifyDataSetChanged();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
    public void CheckReturn(Table table){
//        boolean check = true;
//        for (FoodInfo f:table.getDsFoodSelection()) {
//            if(f.getFoodSelAmount()!=0){
//                check = false;
//                break;
//            }
//        }
//        if(!check){
//            Toast.makeText(context, "Vui lòng thanh toán", Toast.LENGTH_SHORT).show();
//        }else{
//            Manager.ReturnTable(table.getIdTable());
//            notifyDataSetChanged();
//        }

        if(table.getBill().equals("")){
            table.setStatus(0);
            notifyDataSetChanged();
        }
        else {
            Toast.makeText(context, "Vui lòng thanh toán", Toast.LENGTH_SHORT).show();
        }
    }
}
