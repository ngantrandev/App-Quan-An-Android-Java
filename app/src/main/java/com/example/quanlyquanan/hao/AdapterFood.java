package com.example.quanlyquanan.hao;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquanan.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterFood extends RecyclerView.Adapter<HolderFood> {
    Context context;
    List<FoodInfo> data;
    int idtable;
    public AdapterFood(Context context, List<FoodInfo> data,int idtable){
        this.context = context;
        this.data = data;
        this.idtable = idtable;
    }
    @NonNull
    @Override
    public HolderFood onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food_hao,parent,false);
        return new HolderFood(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderFood holder, int position) {
        FoodInfo Food = data.get(position);
        Picasso.get().load(Food.getFoodThumb()).into(holder.thumb);
        holder.name.setText(Food.getFoodName());
        if(Manager.getFoodRemaining(Food.getFoodId())>0)
        holder.itemlayer.setCardBackgroundColor(ContextCompat.getColor(context,R.color.white));
        else{
            holder.itemlayer.setCardBackgroundColor(ContextCompat.getColor(context,R.color.gray));
        }
        holder.amount.setText(Food.getFoodSelAmount()+"");
        if(Food.getFoodNote().length()<1){
            holder.note.setText("Thêm mô tả hoặc ghi chú");
        }else{
            holder.note.setText(Food.getFoodNote());
        }

        DecimalFormat f = new DecimalFormat("#,###");
        holder.price.setText(f.format(Food.getFoodPrice())+" VNĐ");
        holder.remains.setText(Manager.getFoodRemaining(Food.getFoodId())+"");
        if(Food.getFoodOldPrice()!=Food.getFoodPrice()) {
            holder.discount.setVisibility(View.VISIBLE);
            holder.discount.setText(f.format(Food.getFoodOldPrice())+" VNĐ");
        }else{
            holder.discount.setVisibility(View.INVISIBLE);
        }
        holder.btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Manager.AddFood(Food.getFoodId(),idtable);
                notifyDataSetChanged();
            }
        });
        holder.btnminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Manager.MinusFood(Food.getFoodId(),idtable);
                notifyDataSetChanged();
            }
        });
        holder.btnnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenNote(Food.getFoodId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public void OpenNote(int idFood){
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
            if (data.get(Manager.FindIdFoodInTable(idFood)).getFoodNote().length()>0){
                notecontent.setText(data.get(Manager.FindIdFoodInTable(idFood)).getFoodNote());
            }
            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String msg = "";
                    msg = notecontent.getText().toString();
                    data.get(Manager.FindIdFoodInTable(idFood)).setFoodNote(msg);
                    dialog.cancel();
                    notifyDataSetChanged();
                }
            });
            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
