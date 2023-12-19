package com.example.quanlyquanan.hao;

import android.app.Dialog;
import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquanan.R;
import com.example.quanlyquanan.model.Bill;
import com.example.quanlyquanan.model.BillInfo;
import com.example.quanlyquanan.model.Food;
import com.example.quanlyquanan.setting.MyApplication;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterFood extends RecyclerView.Adapter<HolderFood> {
    Context context;
    List<Food> data;
    private Bill mBill;

    public AdapterFood(Context context, List<Food> data, Bill mBill) {
//        Log.d("CHECKADAPER", "AdapterFood: " + mBill.toString());
        this.context = context;
        this.data = data;
        this.mBill = mBill;
    }

    @NonNull
    @Override
    public HolderFood onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food_hao, parent, false);
        return new HolderFood(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderFood holder, int position) {
        Food food = data.get(position);


//        Picasso.get().load(food.getImgUrl()).into(holder.thumb);
        try {
            String imageUri = MyApplication.BASE_API_URL + "/" + food.getImgUrl();
            Picasso.get().load(imageUri).fit().centerCrop()
                    .placeholder(R.drawable.food_img_default)
                    .error(R.drawable.food_img_default)
                    .into(holder.thumb);
        } catch (Exception e) {
            holder.thumb.setImageResource(R.drawable.food_img_default);

        }


        holder.name.setText(food.getName());
        if (food.getSoLuongTon() > 0)
            holder.itemlayer.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));
        else {
            holder.itemlayer.setCardBackgroundColor(ContextCompat.getColor(context, R.color.gray));
        }

        // hien thi so luong chon
        holder.amount.setText("0");

        if (mBill == null) {
            Log.d("LOADBILLINFO", "onBindViewHolder: " + "bill khong co san");
        }

        if (mBill != null) {
//            Log.d("LOADBILLINFO", "onBindViewHolder: billinfo size" + mBill.getBillinfos().size());
//            List<BillInfo> billInfoList = mBill.getBillinfos();
//            for (int i = 0; i < billInfoList.size(); i++) {
//                Log.d("LOADBILLINFO", "onBindViewHolder: billinfo quantity" + billInfoList.get(i).getQuantity());
//                if (billInfoList.get(i).getFood().equals(food.get_id())) {
//                    holder.amount.setText("" + billInfoList.get(i).getQuantity());
//                    break;
//                }
//            }

            BillInfo billInfo = findBillInfo(mBill.getBillinfos(), food.get_id());

            if (billInfo != null) {
                holder.amount.setText("" + billInfo.getQuantity());
            }
        }


        if (food.getDescription().length() < 1) {
            holder.note.setText("Thêm mô tả hoặc ghi chú");
        } else {
            holder.note.setText(food.getDescription());
        }

        DecimalFormat f = new DecimalFormat("#,###");
        holder.price.setText(f.format(food.getNewPrice()) + " VNĐ");


        holder.remains.setText("" + food.getSoLuongTon());

        if (food.getDiscount() != 0) {
            holder.discount.setVisibility(View.VISIBLE);
            holder.discount.setText(f.format(food.getPrice()) + " VNĐ");
        } else {
            holder.discount.setVisibility(View.INVISIBLE);
        }
        holder.btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Manager.AddFood(food.getFoodId(),idtable);
//                int soluongfood = Integer.valueOf(holder.amount.getText().toString());
//
//                if (soluongfood < food.getSoLuongTon()) {
//                    soluongfood++;
//                    holder.amount.setText("" + soluongfood);
//                    Log.d("ONCLICHPLUS", "onClick: plus" + soluongfood);
//                }

                BillInfo billInfo = findBillInfo(mBill.getBillinfos(), food.get_id());

                if (food.getSoLuongTon() > 0) {
                    if (billInfo != null) {
                        billInfo.setQuantity(billInfo.getQuantity() + 1);
                    } else {

                        BillInfo newBillInfo = new BillInfo(mBill.get_id(), food.get_id(), 1, food.getNewPrice());
                        mBill.getBillinfos().add(newBillInfo);
                    }

                    food.setSoLuongTon(food.getSoLuongTon() - 1);
                    notifyDataSetChanged();
                }
            }
        });
        holder.btnminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Manager.MinusFood(food.getFoodId(),idtable);

                BillInfo billInfo = findBillInfo(mBill.getBillinfos(), food.get_id());
                if (billInfo != null) {
                    if (billInfo.getQuantity() > 0) {
                        billInfo.setQuantity(billInfo.getQuantity() - 1);
                        food.setSoLuongTon(food.getSoLuongTon() + 1);
                        if(billInfo.getQuantity()==0){
                            mBill.getBillinfos().remove(billInfo);
                        }
                        notifyDataSetChanged();
                    }
                }
            }
        });
//        holder.btnnote.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                OpenNote(food.getFoodId());
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void OpenNote(Food food) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_button_sheet_note);
        Button btnDone = dialog.findViewById(R.id.btndone);
        EditText notecontent = dialog.findViewById(R.id.notecontent);
        TextView limitvaluetext = dialog.findViewById(R.id.limitvalue);
        notecontent.setTextColor(ContextCompat.getColor(context, R.color.black));
        notecontent.setHintTextColor(ContextCompat.getColor(context, R.color.black));
        notecontent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
        notecontent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                limitvaluetext.setText(charSequence.length() + "");
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        if (food.getDescription().length() > 0) {
            notecontent.setText(food.getDescription());
        }
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = "";
                msg = notecontent.getText().toString();
                food.setDescription(msg);
                dialog.cancel();
                notifyDataSetChanged();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private BillInfo findBillInfo(List<BillInfo> billInfoList, String idFood) {
        for (BillInfo billinfo : billInfoList) {
            if (billinfo.getFood().equals(idFood))
                return billinfo;
        }

        return null;
    }
}
