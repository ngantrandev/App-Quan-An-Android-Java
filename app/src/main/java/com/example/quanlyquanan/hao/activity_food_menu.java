package com.example.quanlyquanan.hao;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquanan.R;
import com.example.quanlyquanan.api.BillApi;
import com.example.quanlyquanan.model.Bill;
import com.example.quanlyquanan.model.Table;
import com.example.quanlyquanan.response.ResponseBillById;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_food_menu extends AppCompatActivity {

    Table mtable;
    Bill mBill;
    int idtable = 0;
    Button exportbtn;
    ImageButton btnBack;
    TableInfo table;
    List<FoodInfo> dsFood = new ArrayList<>();
    List<FoodInfo> dsFoodFilter = new ArrayList<>();
    SearchView btnsearch;
    RecyclerView foodMenuLv;
    AdapterFood foodAdapter;
    ArrayAdapter filterAdapter;
    CardView foodmenucardviewspinner;
    List<String> filterOption = new ArrayList<>();
    Spinner btnFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

//        Bundle extras = getIntent().getExtras();
//        if (extras!=null) {
//            idtable = extras.getInt("idtable");
//            if (idtable>=0 && idtable<Manager.getDsTable().size()) {
//                Table = Manager.getDsTable().get(idtable);
//                dsFood = Table.getDsFoodSelection();
////                Toast.makeText(this,Table.getDsFoodSelection().get(0).getFoodSelAmount()+"", Toast.LENGTH_SHORT).show();
//            }
//            else{
//                openTableMenu();
//            }
//        }else{
//            openTableMenu();
//        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("table")) {
            mtable = intent.getParcelableExtra("table");
            if (mtable == null) {
                // Bạn có thể sử dụng đối tượng ở đây
                openTableMenu();
                return;
            }

            if (mtable.getBill().equals("")) {
                Log.d("TAG", "onCreate: table khong cos bill");
                Log.d("TAG", "onCreate: " + mtable.getBill());
                createNewBill();
            } else {
                Log.d("TAG", "onCreate: table cos bill");
                loadBillById();
            }
        }

        setcontrol();
        setevent();
    }

    private void loadBillById() {
        BillApi.billApi.getBillById(mtable.getBill()).enqueue(new Callback<ResponseBillById>() {
            @Override
            public void onResponse(Call<ResponseBillById> call, Response<ResponseBillById> response) {
                if (response.isSuccessful() && response.body().getStatus().equals("Success")) {
                    mBill = response.body().getBill();

                    notifyBillAvaiable();
                } else {
                    Toast.makeText(activity_food_menu.this, "Khong tim thay bill", Toast.LENGTH_SHORT).show();
//                    openTableMenu();
                }
            }

            @Override
            public void onFailure(Call<ResponseBillById> call, Throwable t) {
                Toast.makeText(activity_food_menu.this, "Khong the ket noi den server", Toast.LENGTH_LONG).show();
                openTableMenu();
            }
        });
    }

    private void createNewBill() {
        int timeCheckIn = getCurrenTime();
        String tableId = mtable.get_id();
        String sellerId = getUserId();

        Toast.makeText(this, "Create bill " + timeCheckIn + "   " + tableId + "  " + sellerId, Toast.LENGTH_SHORT).show();
        Log.d("TAG", "createNewBill: tableinfo truoc callapi" + mtable.toString());


        BillApi.billApi.createNewBill(String.valueOf(timeCheckIn),
                        tableId,
                        sellerId)
                .enqueue(new Callback<ResponseBillById>() {
                    @Override
                    public void onResponse(Call<ResponseBillById> call, Response<ResponseBillById> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equals("Success")) {

//                            Bill newBill = response.body().getBill();
//                            mtable.setBill(newBill.get_id());
//
//                            mBill = newBill;
//
//                            notifyBillAvaiable();
                            }
                        } else {
                            Toast.makeText(activity_food_menu.this, "TAO BILL THAT BAI " + response.body().getError(), Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "onResponse: " + "TAO BILL THAT BAI " + response.body().getError());
                            Log.d("TAG", "onResponse: " + "TAO BILL THAT BAI " + response.body().getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBillById> call, Throwable t) {
                        Log.d("TAG", "createNewBill: tableinfo callapi that bai" + mtable.toString());
                        openTableMenu();
                        Toast.makeText(activity_food_menu.this, "May chu dang BAN", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void notifyBillAvaiable() {

    }


    private void setcontrol() {
        foodmenucardviewspinner = findViewById(R.id.foodMenuCardSpinner);
        btnsearch = findViewById(R.id.foodMenuBtnSearch);
        foodMenuLv = findViewById(R.id.foodMenuLv);
        btnBack = findViewById(R.id.foodMenuBackBtn);
        exportbtn = findViewById(R.id.foodMenuExportBtn);
        btnFilter = findViewById(R.id.foodSpinnerFilter);
        CreateDataFilter();
        foodAdapter = new AdapterFood(this, dsFoodFilter, idtable);
        foodMenuLv.setAdapter(foodAdapter);
        foodMenuLv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void setevent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTableMenu();
            }
        });
        btnFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                HandleSpinnerSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnsearch.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    foodmenucardviewspinner.setVisibility(View.GONE);
                } else {
                    if (btnsearch.getQuery().length() < 1) {
                        foodmenucardviewspinner.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        btnsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    dsFoodFilter.clear();
                    for (FoodInfo f : dsFood)
                        if ((f.getFoodNote().toLowerCase().contains(newText) || f.getFoodName().toLowerCase().contains(newText) || f.getFoodtype().toLowerCase().contains(newText))) {
                            dsFoodFilter.add(f);
                        }
                    foodAdapter.notifyDataSetChanged();
                } else {
                    btnFilter.setSelection(btnFilter.getSelectedItemPosition());
                    HandleSpinnerSelection(btnFilter.getSelectedItemPosition());
                }
                return false;
            }
        });
        foodAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
//                boolean billon = false;
//                for (FoodInfo f : dsFood) {
//                    if (f.getFoodSelAmount() > 0) {
//                        billon = true;
//                        break;
//                    }
//                }
                exportbtn.setVisibility(View.VISIBLE);

//                if(mtable == null) return;
//                if(mtable.getBill().equals("")) return;
//
//
//                if (mBill!=null)
//                    exportbtn.setVisibility(View.VISIBLE);
//                else
//                    exportbtn.setVisibility(View.GONE);
            }
        });
        exportbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "onclick: show bill table");
                openbillTable();
            }
        });
    }

    private void OrderCheck() {
        boolean count = false;
        for (FoodInfo f : table.getDsFoodSelection()) {
            if (f.getFoodSelAmount() > 0) {
                count = true;
                break;
            }
        }
        if (count) {
            exportbtn.setVisibility(View.VISIBLE);
        } else {
            exportbtn.setVisibility(View.GONE);
        }
    }

    void openTableMenu() {
        Intent intent = new Intent(this, activity_table_menu.class);
        startActivity(intent);
    }

    void CreateDataFilter() {
        filterOption.add("Tất cả");
        filterOption.add("Có thể chọn");
        filterOption.add("Đã Hết");
        filterOption.add("Đã chọn");
        filterAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, filterOption);
        btnFilter.setAdapter(filterAdapter);
    }

    public void openbillTable() {

        try {
            Intent intent = new Intent(activity_food_menu.this, activity_show_bill_table.class);
            intent.putExtra("bill", mBill);
            intent.putExtra("table", mtable);
//        intent.putExtra("dsFood", (Serializable) dsfood);
            this.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity_food_menu.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("TAG", "onOpenBillTable: " + e);
        }
    }

    public void HandleSpinnerSelection(int index) {
        dsFoodFilter.clear();
        switch (index) {
            //Tất cả
            case 0: {
                for (FoodInfo f : dsFood) {
                    dsFoodFilter.add(f);
                }
                break;
            }
            //Còn
            case 1: {
                for (FoodInfo f : dsFood) {
                    if (Manager.getFoodRemaining(Manager.FindIdFoodInTable(f.getFoodId())) > 0) {
                        dsFoodFilter.add(f);
                    }
                }
                break;
            }
            //Hết
            case 2: {
                for (FoodInfo f : dsFood) {
                    if (Manager.getFoodRemaining(Manager.FindIdFoodInTable(f.getFoodId())) < 1) {
                        dsFoodFilter.add(f);
                    }
                }
                break;
            }
            //Đã chọn
            case 3: {
                for (FoodInfo f : dsFood) {
                    if (f.getFoodSelAmount() > 0) {
                        dsFoodFilter.add(f);
                    }
                }
                break;
            }
        }
        foodAdapter.notifyDataSetChanged();
    }

    private int getCurrenTime() {
        // Getting the current date from Calendar class.
        Calendar calendar = Calendar.getInstance();

        // Getting the time in milliseconds.
        int milliSeconds = (int) calendar.getTimeInMillis();

        return milliSeconds;
    }

    private String getUserId() {
        return "654905e6e9cdc122868b778b";
    }
}