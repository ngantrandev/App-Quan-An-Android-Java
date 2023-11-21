package com.example.quanlyquanan.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.quanlyquanan.R;
import com.example.quanlyquanan.adapter.AdapterListTable;
import com.example.quanlyquanan.api.ApiError;
import com.example.quanlyquanan.api.TableApi;
import com.example.quanlyquanan.model.Table;
import com.example.quanlyquanan.response.ResponseTable;
import com.example.quanlyquanan.setting.MyApplication;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityListTable extends AppCompatActivity {
    private ImageView btnBack;
    private RecyclerView recyclerView;
    List<Table> tableList;
    AdapterListTable adapterListTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_table);

        setControl();
        setEvent();
    }

    private void setEvent() {

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityListTable.this.finish();
            }
        });
    }

    private void setControl() {
        btnBack = findViewById(R.id.ic_back_activity_listtable);
        recyclerView = findViewById(R.id.recyclerview_activity_listtable);
        tableList = new ArrayList<>();

        adapterListTable = new AdapterListTable(this, tableList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterListTable);

        loadTableList();
    }

    private void loadTableList() {
        TableApi.tableApi.getTables().enqueue(new Callback<ResponseTable>() {
            @Override
            public void onResponse(Call<ResponseTable> call, Response<ResponseTable> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("Success")) {
//                        Toast.makeText(ActivityListTable.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        tableList.clear();
                        tableList.addAll(response.body().getTableList());
                        adapterListTable.notifyDataSetChanged();
                    }
                } else {
                    showErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseTable> call, Throwable t) {
                Toast.makeText(ActivityListTable.this, MyApplication.MESSAGE_TOAST_SERVER_NOTRESPONSE, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showErrorResponse(Response<?> response) {
        JsonParser parser = new JsonParser();
        JsonElement mJson = null;
        try {
            mJson = parser.parse(response.errorBody().string());
            Gson gson = new Gson();
            ApiError errorResponse = gson.fromJson(mJson, ApiError.class);

            Toast.makeText(this, errorResponse.getError(), Toast.LENGTH_SHORT).show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}