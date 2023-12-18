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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquanan.R;
import com.example.quanlyquanan.model.Bill;
import com.example.quanlyquanan.model.Table;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class activity_show_bill_table extends AppCompatActivity {

    TextView billDate, billPriceTV, billTotalTV, billIdTable;
    ImageButton btnBack;
    Button btnCheck;
    Bill mBill;
    Table mTable;
    LinearLayout iconcheck;
    Date currentTime;
    int idtable, PriceBill, Total, tip;
    EditText TipInput;
    List<FoodInfo> DsFoodBill = new ArrayList<>();
    RecyclerView Foodlv;
    AdapterBillFood FoodbillAdapter;

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

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("table") && intent.hasExtra("bill")) {
            mBill =intent.getParcelableExtra("bill");
            mTable =  intent.getParcelableExtra("table");
            if (mBill == null) {
                // Bạn có thể sử dụng đối tượng ở đây
                openFoodMenu();
                return;
            }
        }
//
//        currentTime = Calendar.getInstance().getTime();
//        setcontrol();
//        setevent();
    }

    private void setevent() {
        DecimalFormat nf = new DecimalFormat("#,###");
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCheck.setVisibility(View.GONE);
                iconcheck.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Manager.ReturnTable(idtable);
                        openTableMenu();
                    }
                }, 2000);
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
                    tip = (int) Integer.parseInt(str);
                } else {
                    TipInput.setText("0");
                    tip = 0;
                }
                Total = PriceBill + tip;
                billTotalTV.setText(nf.format(Total) + " VNĐ");
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
        billTotalTV.setText(nf.format(Total) + " VNĐ");
    }

    private void setcontrol() {
        btnCheck = findViewById(R.id.billTableBtnCheck);
        iconcheck = findViewById(R.id.billTableCheck);
        btnBack = findViewById(R.id.billTableBack);
        billDate = findViewById(R.id.billTableDate);
        billPriceTV = findViewById(R.id.billTablePrice);
        billTotalTV = findViewById(R.id.billTableTotal);
        billIdTable = findViewById(R.id.BillIdTable);
        Foodlv = findViewById(R.id.BillFoodLv);
        FoodbillAdapter = new AdapterBillFood(this, DsFoodBill, idtable);
        Foodlv.setAdapter(FoodbillAdapter);
        TipInput = findViewById(R.id.billTableInputTip);
        CheckFood();
        billIdTable.setText((idtable + 1) + "");
        Foodlv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
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
//        for (FoodInfo f:Table.getDsFoodSelection()) {
//            if(f.getFoodSelAmount()>0){
//                DsFoodBill.add(f);
//                PriceBill +=(f.getFoodPrice()*f.getFoodSelAmount());
//            }
//        }
//        DecimalFormat nf = new DecimalFormat("#,###");
//        billPriceTV.setText(nf.format(PriceBill)+" VNĐ");
//        Total = PriceBill;
    }
}