package com.example.quanlyquanan.hao;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquanan.R;
import com.example.quanlyquanan.activity.MainActivity;
import com.example.quanlyquanan.activity.StartActivity;
import com.example.quanlyquanan.api.TableApi;
import com.example.quanlyquanan.model.Table;
import com.example.quanlyquanan.response.ResponseTable;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_table_menu extends AppCompatActivity {
    List<Table> dsTable = new ArrayList<>();
    List<Table> dsTableFilter = new ArrayList<>();
    RecyclerView TableLv;
    SearchView btnsearch;
    Spinner FilterBtn;
    CardView tableMenuCardBtnSearch,tableMenuCardSpinner;
    AdapterTable tableAdapter;
    ArrayAdapter filterAdapter;
    List<String> FilterOption = new ArrayList<String>();
    private static final String TAG = "activity_table_menu";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_menu);
        setcontrol();
        setevent();
    }
    private void setcontrol() {
//        dsTable = Manager.getDsTable();
        TableLv = findViewById(R.id.tableMenuListView);
        FilterBtn = findViewById(R.id.spinnerfilter);
        tableMenuCardSpinner = findViewById(R.id.tableMenuCardSpinner);
        tableMenuCardBtnSearch = findViewById(R.id.tableMenuCardBtnSearch);
        btnsearch = findViewById(R.id.tableMenuBtnSearch);
        createOptionFilter();
        tableAdapter = new AdapterTable(this,dsTableFilter);
        TableLv.setAdapter(tableAdapter);
        TableLv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        loadTableList();
        Log.d("TAG", "setcontrol: activity table menu");
    }
//    @Override
//    public void onBackPressed() {
//        int count = 1;
//        if (count == 0) {
//            super.onBackPressed();
//            //additional code
//        }
//
//    }
    private void createOptionFilter(){
        FilterOption.add("Tăng dần");
        FilterOption.add("Giảm dần");
        FilterOption.add("Đang hoạt động");
        FilterOption.add("Trống");
        filterAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,FilterOption);
        FilterBtn.setAdapter(filterAdapter);
    }

    private void setevent() {
//        TableLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//        });
        findViewById(R.id.tableMenuBtnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_table_menu.this, MainActivity.class);
                activity_table_menu.this.startActivity(intent);
                activity_table_menu.this.finish();
            }
        });

        FilterBtn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                HandleSpinnerSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(activity_table_menu.this, "Spinner nothing", Toast.LENGTH_SHORT).show();
            }
        });
        btnsearch.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tableMenuCardSpinner.setVisibility(View.GONE);
                } else {
                     if(btnsearch.getQuery().length()<1) {
                         tableMenuCardSpinner.setVisibility(View.VISIBLE);
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
                if(newText.length()>0) {
                    dsTableFilter.clear();
                    for (Table t : dsTable)
                        if (t.getTableName().contains(newText) || t.getNote().contains(newText)) {
                            dsTableFilter.add(t);
                        }
                    tableAdapter.notifyDataSetChanged();
                }
                else {
                    FilterBtn.setSelection(FilterBtn.getSelectedItemPosition());
                    HandleSpinnerSelection(FilterBtn.getSelectedItemPosition());
                }
                return false;
            }
        });
    }
    void HandleSpinnerSelection(int index){
        dsTableFilter.clear();
        int i =0;
        if(index==0){
            for(i=0;i<=dsTable.size()-1;i++){
                dsTableFilter.add(dsTable.get(i));
            }
        }
        if(index==1){
            for(i=dsTable.size()-1;i>=0;i--){
                dsTableFilter.add(dsTable.get(i));
            }
        }
        if(index==2){
            for(Table t: dsTable){
                if(t.getStatus() == 1){
                    dsTableFilter.add(t);
                }
            }
        }
        if(index==3){
            for (Table t: dsTable){
                if(t.getStatus() == 0){
                    dsTableFilter.add(t);
                }
            }
        }
//                Toast.makeText(table_menu.this, FilterBtn.getSelectedItem().toString()+"", Toast.LENGTH_SHORT).show();
        tableAdapter.notifyDataSetChanged();
    }

    void loadTableList(){
        TableApi.tableApi.getTables().enqueue(new Callback<ResponseTable>() {
            @Override
            public void onResponse(Call<ResponseTable> call, Response<ResponseTable> response) {
                if(response.isSuccessful() && response.body().getStatus().equals("Success")){
                    dsTable.clear();
                    dsTableFilter.clear();
                    dsTable = response.body().getTableList();
                    dsTableFilter.addAll(dsTable);
                    tableAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(activity_table_menu.this, response.body().getError(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseTable> call, Throwable t) {
                Toast.makeText(activity_table_menu.this, "Ket noi server that bai", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        tableAdapter.notifyDataSetChanged();
    }
}