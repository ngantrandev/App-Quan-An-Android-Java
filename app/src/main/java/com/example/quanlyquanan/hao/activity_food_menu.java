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
import com.example.quanlyquanan.api.FoodApi;
import com.example.quanlyquanan.model.Bill;
import com.example.quanlyquanan.model.Food;
import com.example.quanlyquanan.model.Table;
import com.example.quanlyquanan.response.ResponseBillById;
import com.example.quanlyquanan.response.ResponseFood;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_food_menu extends AppCompatActivity {

    Table mtable;
    Bill mBill;
    Button exportbtn;
    ImageButton btnBack;
    List<Food> dsFood = new ArrayList<>();
    List<Food> dsFoodFilter = new ArrayList<>();
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
                createNewBill();
            } else {
                Log.d("TAG", "onCreate: table cos bill");
                loadBillById(mtable.getBill());
            }
        }


        setcontrol();
        setevent();
    }

    private void loadBillById(String billId) {
        BillApi.billApi.getBillById(billId).enqueue(new Callback<ResponseBillById>() {
            @Override
            public void onResponse(Call<ResponseBillById> call, Response<ResponseBillById> response) {
                if (response.isSuccessful() && response.body().getStatus().equals("Success")) {
                    try {
                        mBill = response.body().getBill();
                    } catch (Exception e) {
                        Log.d("BILLLOAD", "onResponse: bill" + e);
                    }

                    try {
                        foodAdapter = new AdapterFood(activity_food_menu.this, dsFoodFilter, mBill);
                        foodMenuLv.setAdapter(foodAdapter);
                        foodMenuLv.setLayoutManager(new LinearLayoutManager(activity_food_menu.this, LinearLayoutManager.VERTICAL, false));
                    } catch (Exception e) {
                        Log.d("BILLLOAD", "onResponse: " + e);
                    }
                    notifyBillAvaiable();

                } else {
                    Toast.makeText(activity_food_menu.this, "Khong tim thay bill", Toast.LENGTH_SHORT).show();
                    openTableMenu();
                }
            }

            @Override
            public void onFailure(Call<ResponseBillById> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t);
                Toast.makeText(activity_food_menu.this, "Khong the ket noi den server", Toast.LENGTH_LONG).show();
                openTableMenu();
            }
        });
    }

    private void createNewBill() {
        long timeCheckIn = getCurrenTime();
        String tableId = mtable.get_id();
        String sellerId = getUserId();

//        Toast.makeText(this, "Create bill " + timeCheckIn + "   " + tableId + "  " + sellerId, Toast.LENGTH_SHORT).show();
        Log.d("CREATEBILL", "createNewBill: tableinfo truoc callapi" + mtable.toString());


        BillApi.billApi.createNewBill(String.valueOf(timeCheckIn),
                        tableId,
                        sellerId)
                .enqueue(new Callback<ResponseBillById>() {
                    @Override
                    public void onResponse(Call<ResponseBillById> call, Response<ResponseBillById> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equals("Success")) {

                                Bill newBill = response.body().getBill();
                                mtable.setBill(newBill.get_id());

                                Log.d("CREATEBILL", "onResponse: newbill" + newBill.toString());
                                mBill = newBill;

                                Log.d("CREATEBILL", "onResponse: tao bill thanh con");
                                notifyBillAvaiable();

                                loadBillById(newBill.get_id());
                            }
                        } else {
                            Toast.makeText(activity_food_menu.this, "TAO BILL THAT BAI " + response.body().getError(), Toast.LENGTH_SHORT).show();
                            Log.d("CREATEBILL", "onResponse: " + "TAO BILL THAT BAI " + response.body().getError());
                            Log.d("CREATEBILL", "onResponse: " + "TAO BILL THAT BAI " + response.body().getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBillById> call, Throwable t) {
                        Log.d("CREATEBILL", "createNewBill: tableinfo callapi that bai" + mtable.toString());
                        openTableMenu();
                        Toast.makeText(activity_food_menu.this, "May chu dang BAN", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void notifyBillAvaiable() {
        Log.d("TAG", "notifyBillAvaiable: " + mBill);
        foodAdapter.notifyDataSetChanged();
        Log.d("TAG", "sau khi notify: " + mBill);
    }


    private void setcontrol() {
        foodmenucardviewspinner = findViewById(R.id.foodMenuCardSpinner);
        btnsearch = findViewById(R.id.foodMenuBtnSearch);
        foodMenuLv = findViewById(R.id.foodMenuLv);
        btnBack = findViewById(R.id.foodMenuBackBtn);
        exportbtn = findViewById(R.id.foodMenuExportBtn);
        btnFilter = findViewById(R.id.foodSpinnerFilter);
        CreateDataFilter();

//        Log.d("TAG", "truoc khi gan bill" + mBill.toString());

        foodAdapter = new AdapterFood(this, dsFoodFilter, mBill);
        foodMenuLv.setAdapter(foodAdapter);
        foodMenuLv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        loadFoodList();

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
                    for (Food f : dsFood)
                        if ((f.getDescription().toLowerCase().contains(newText) || f.getName().toLowerCase().contains(newText) || f.getCategory().getName().toLowerCase().contains(newText))) {
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
                if(mBill.getBillinfos().size()==0){
                    Toast.makeText(activity_food_menu.this, "Danh sách món ăn trống", Toast.LENGTH_LONG).show();
                }else {
                    Log.d("TAG", "onclick: show bill table");
                    openbillTable();
                }
            }
        });
    }

    private void OrderCheck() {
        if (mBill.getBillinfos().size()>0) {
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
            intent.putParcelableArrayListExtra("foodlist", new ArrayList<>(dsFood));
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
                for (Food f : dsFood) {
                    dsFoodFilter.add(f);
                }
                break;
            }
            //Còn
            case 1: {
                for (Food f : dsFood) {
//                    if (Manager.getFoodRemaining(Manager.FindIdFoodInTable(f.get_id())) > 0) {
//                        dsFoodFilter.add(f);
//                    }
                    if (f.getSoLuongTon() > 0)
                        dsFoodFilter.add(f);
                }
                break;
            }
            //Hết
            case 2: {
                for (Food f : dsFood) {
//                    if (Manager.getFoodRemaining(Manager.FindIdFoodInTable(f.get_id())) < 1) {
//                        dsFoodFilter.add(f);
//                    }
                    if (f.getSoLuongTon() == 0)
                        dsFoodFilter.add(f);
                }
                break;
            }
//            //Đã chọn
            case 3: {
//                for (Food f : dsFood) {
//                    if (f.getFoodSelAmount() > 0) {
//                        dsFoodFilter.add(f);
//                    }
//                }

                dsFoodFilter.addAll(Food.filterListFoodInListBillInfo(mBill.getBillinfos(), dsFood));
                break;
            }
        }
        foodAdapter.notifyDataSetChanged();
    }

    private long getCurrenTime() {
        // Chọn múi giờ (Ví dụ: UTC+0)
        TimeZone timeZone = TimeZone.getTimeZone("UTC+7");

        // Lấy thời gian hiện tại với múi giờ chỉ định
        long currentTimeMillis = System.currentTimeMillis();
        long currentTimeMillisInTimeZone = currentTimeMillis + timeZone.getRawOffset();

        return currentTimeMillisInTimeZone;
    }

    private String getUserId() {
        return "654905e6e9cdc122868b778b";
    }

    private void loadFoodList() {
        FoodApi.foodApi.getFoods().enqueue(new Callback<ResponseFood>() {
            @Override
            public void onResponse(Call<ResponseFood> call, Response<ResponseFood> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("Success")) {
                        dsFood.clear();
                        dsFood.addAll(response.body().getFoodList());
//                        dsFoodFilter.clear();
//                        dsFoodFilter.addAll(dsFood);
                        HandleSpinnerSelection(btnFilter.getSelectedItemPosition());
                        foodAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseFood> call, Throwable t) {
                Toast.makeText(activity_food_menu.this, "Khong the lay du lieu mon an", Toast.LENGTH_LONG).show();
            }
        });
    }
}