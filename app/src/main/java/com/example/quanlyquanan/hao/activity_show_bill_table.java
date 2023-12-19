package com.example.quanlyquanan.hao;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquanan.R;
import com.example.quanlyquanan.adapter.AdapterListFood;
import com.example.quanlyquanan.api.BillApi;
import com.example.quanlyquanan.model.Bill;
import com.example.quanlyquanan.model.BillInfo;
import com.example.quanlyquanan.model.Food;
import com.example.quanlyquanan.model.Table;
import com.example.quanlyquanan.response.ResponseBillById;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_show_bill_table extends AppCompatActivity {

    TextView billDate, billPriceTV, billTotalTV, billIdTable;
    ImageButton btnBack;
    Button btnCheck;
    Bill mBill;
    List<Food> mFoodList;
    Table mTable;
    LinearLayout iconcheck;
    Date currentTime;
    EditText TipInput;
    List<FoodInfo> DsFoodBill = new ArrayList<>();
    RecyclerView recyclerViewFood;
    AdapterBillFood adapterBillFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_table);

        Log.d("TAG", "onCreate: show bill table");


//        Bundle extras = getIntent().getExtras();
//        if (extras!=null) {
//            idtable = extras.getInt("idtable");
//            if (idtable>=0 && idtable<Manager.getDsTable().size()) {
//                Table = Manager.getDsTable().get(idtable);
////                Toast.makeText(this,Table.getDsFoodSelection().get(0).getFoodSelAmount()+"", Toast.LENGTH_SHORT).show();
//            }
//            else{
//                openTableMenu();
//            }
//        }else{
//            openTableMenu();
//        }

        try {
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra("table") && intent.hasExtra("bill") && intent.hasExtra("foodlist")) {
                mBill = intent.getParcelableExtra("bill");
                mTable = intent.getParcelableExtra("table");
                mFoodList = intent.getParcelableArrayListExtra("foodlist");
                if (mBill == null) {
                    // Bạn có thể sử dụng đối tượng ở đây
                    openFoodMenu();
                    return;
                }

                currentTime = Calendar.getInstance().getTime();
                setcontrol();
                setevent();
            }
        } catch (Exception e) {
            Log.d("BILLCHECKOUT", "onCreate: " + e);
        }
//

    }

    private void setevent() {
        DecimalFormat nf = new DecimalFormat("#,###");
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thanhToan();

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFoodMenu();
            }
        });
//        billDate.setText(currentTime.toString());
        TipInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TipInput.getText().toString().length() != 0) {
                    String str = TipInput.getText().toString();
                    str = str.replaceAll("[,.]", "");
//                    tip = (int) Integer.parseInt(str);
                } else {
                    TipInput.setText("0");
//                    tip = 0;
                }
//                Total = PriceBill + tip;
//                billTotalTV.setText(nf.format(Total) + " VNĐ");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                TipInput.removeTextChangedListener(this);
                String str = TipInput.getText().toString();
                str = str.replaceAll("[,.]", "");
                TipInput.setText(nf.format(Integer.parseInt(str)));
                TipInput.setSelection(TipInput.getText().length());
                TipInput.addTextChangedListener(this);
            }
        });
//        billTotalTV.setText(nf.format(Total) + " VNĐ");
    }

    private void thanhToan() {
        int tip = 0;
//        try {
        if (TipInput.getText().toString().trim().length() > 0)
            tip = Integer.parseInt(TipInput.getText().toString());
        mBill.setTips(tip);
        Log.d("BILLCHECKOUT", "onResponse: truoc khi call" );
        BillApi.billApi.thanhtoan(mBill.get_id(), mBill).enqueue(new Callback<ResponseBillById>() {
            @Override
            public void onResponse(Call<ResponseBillById> call, Response<ResponseBillById> response) {
                Log.d("BILLCHECKOUT", "onResponse: server tra ve ket qua" );
                if(response.isSuccessful()) {
                    if(response.body().getStatus().equals("Success")){
                        btnCheck.setVisibility(View.GONE);
                        iconcheck.setVisibility(View.VISIBLE);
                        Log.d("BILLCHECKOUT", "onResponse: da call api thanh cong" );
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
//                        Manager.ReturnTable(idtable);
                                openTableMenu();
                            }
                        }, 2000);
                    }

                    else {
                        Log.d("BILLCHECKOUT", "cap nhat that bai" );
                        Toast.makeText(activity_show_bill_table.this, response.body().getError(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBillById> call, Throwable t) {
                Log.d("BILLCHECKOUT", "onFailure: " + t);
                Toast.makeText(activity_show_bill_table.this, "Khong the thanh toan", Toast.LENGTH_LONG).show();
            }
        });
//        }
//        catch (Exception e) {
//            Log.d("BILLCHECKOUT", "exception: " + e);
//        }
    }

    private void setcontrol() {
        btnCheck = findViewById(R.id.billTableBtnCheck);
        iconcheck = findViewById(R.id.billTableCheck);
        btnBack = findViewById(R.id.billTableBack);
        billDate = findViewById(R.id.billTableDate);
        billPriceTV = findViewById(R.id.billTablePrice);
        billTotalTV = findViewById(R.id.billTableTotal);
        billIdTable = findViewById(R.id.BillIdTable);
        recyclerViewFood = findViewById(R.id.BillFoodLv);
        TipInput = findViewById(R.id.billTableInputTip);
        billIdTable.setText(mTable.getTableName());


        adapterBillFood = new AdapterBillFood(this, mBill.getBillinfos(), mFoodList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewFood.setLayoutManager(linearLayoutManager);
        recyclerViewFood.setAdapter(adapterBillFood);
        adapterBillFood.notifyDataSetChanged();

        CheckFood();
    }

    void openFoodMenu() {
        Intent intent = new Intent(this, activity_food_menu.class);
        intent.putExtra("table", mTable);
        startActivity(intent);
    }

    void openTableMenu() {
        Intent intent = new Intent(this, activity_table_menu.class);
        startActivity(intent);
    }

    void CheckFood() {
//        PriceBill=0;
        int totalPrice = 0;
        int tip = 0;
        for (BillInfo billInfo : mBill.getBillinfos()) {
            totalPrice += billInfo.getPrice() * billInfo.getQuantity();
        }

        DecimalFormat nf = new DecimalFormat("#,###");
        billPriceTV.setText(nf.format(totalPrice) + " VNĐ");

        billTotalTV.setText(nf.format(totalPrice + tip) + "VNĐ");
        billDate.setText("" + currentTime);

    }
}