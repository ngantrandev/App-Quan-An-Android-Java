package com.example.quanlyquanan.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlyquanan.R;
import com.example.quanlyquanan.adapter.AdapterListCategory;
import com.example.quanlyquanan.adapter.AdapterSpinner;
import com.example.quanlyquanan.api.ApiError;
import com.example.quanlyquanan.api.CategoryApi;
import com.example.quanlyquanan.model.Category;
import com.example.quanlyquanan.response.ResponseCategories;
import com.example.quanlyquanan.response.ResponseCategoryById;
import com.example.quanlyquanan.setting.MyApplication;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityListCategory extends AppCompatActivity {
    private RecyclerView recyclerView;
    AdapterListCategory adapterListCategory;
    List<Category> categoryList, categoryListTemp, categoryLabel;
    AdapterSpinner adapterSpinner;
    Spinner spinner;
    ImageButton btnAddCategory;
    ImageView imgBack;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category);

        setControl();
        setEvent();
    }

    private void setEvent() {
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAddCategory();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityListCategory.this.finish();

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryListTemp.clear();
                Category spinnerLabel = categoryLabel.get(position);

                for (int i = 0; i < categoryList.size(); i++) {
                    Category category = categoryList.get(i);

                    if (spinnerLabel.getName().equals(category.getName()) || spinnerLabel.getName().equals("Tất cả")) {
                        categoryListTemp.add(category);
                    }

                    adapterListCategory.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                // add category contain text in searchView
                categoryListTemp.clear();
                for(int i = 0;i<categoryList.size();i++) {
                    Category category = categoryList.get(i);

                    if(category.getName().contains(newText)) {
                        categoryListTemp.add(category);

                    }
                }

                adapterListCategory.notifyDataSetChanged();
                return false;
            }
        });
    }

    private void onClickAddCategory() {
        View viewBottomSheet = LayoutInflater.from(this).inflate(R.layout.layout_bottomsheet_category, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(viewBottomSheet);
        Button btnXacNhan = bottomSheetDialog.findViewById(R.id.btn_submitname_bottomsheet_category);
        TextView tvTitle = bottomSheetDialog.findViewById(R.id.tv_title_bottomsheet_category);
        EditText edtName = bottomSheetDialog.findViewById(R.id.edt_name_bottomsheet_category);
        btnXacNhan.setText("Thêm mới");
        tvTitle.setText("Thêm mới danh mục");
        edtName.setText("");

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(ActivityListCategory.this).create();
                alertDialog.setMessage("Xác nhận thêm danh mục mới");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Xác nhận", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
//                        bottomSheetDialog.dismiss();

                        createCategory(bottomSheetDialog, edtName.getText().toString().trim());

                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });


        bottomSheetDialog.show();
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from((View) viewBottomSheet.getParent());
        bottomSheetBehavior.setPeekHeight(800);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }

    private void createCategory(BottomSheetDialog bottomSheetDialog, String newCategoryName) {
        CategoryApi.categoryApi.createCategory(newCategoryName).enqueue(new Callback<ResponseCategoryById>() {
            @Override
            public void onResponse(Call<ResponseCategoryById> call, Response<ResponseCategoryById> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("Success")) {
                        bottomSheetDialog.dismiss();
                        Category newCategory = response.body().getCategory();

                        categoryList.add(newCategory);
                        notifyCategoryListChanged();

                        Toast.makeText(ActivityListCategory.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    showErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseCategoryById> call, Throwable t) {
                showFailedResponse();
            }
        });
    }

    private void setControl() {

        recyclerView = findViewById(R.id.recycler_view_activity_listcategory);
        btnAddCategory = findViewById(R.id.btn_add_category_activity_listcategory);
        imgBack = findViewById(R.id.ic_back_activity_list_category);
        searchView = findViewById(R.id.sv_activity_listcategory);


        categoryList = new ArrayList<>();
        categoryListTemp = new ArrayList<>();
        categoryLabel = new ArrayList<>();

        spinner = findViewById(R.id.sp_activity_listcategory);
        adapterSpinner = new AdapterSpinner(this, R.layout.item_spinner_dropdown, categoryLabel);
        spinner.setAdapter(adapterSpinner);

        adapterListCategory = new AdapterListCategory(this, categoryListTemp);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterListCategory);

        loadCategoryList();
//
    }

    private void loadCategoryList() {

        CategoryApi.categoryApi.getCategories().enqueue(new Callback<ResponseCategories>() {
            @Override
            public void onResponse(Call<ResponseCategories> call, Response<ResponseCategories> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("Success")) {
                        categoryList.clear();
                        categoryList.addAll(response.body().getCategoryList());

                        categoryLabel.add(new Category("0", "Tất cả", 0)); // phan tu dau cua spinner
                        categoryLabel.addAll(categoryList);

                        adapterSpinner.notifyDataSetChanged();

                        notifyCategoryListChanged();
                    }
                } else {
                    showErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseCategories> call, Throwable t) {
                showFailedResponse();
            }
        });
    }

    private void notifyCategoryListChanged() {
        categoryListTemp.clear();
        categoryListTemp.addAll(categoryList);
        adapterListCategory.notifyDataSetChanged();
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

    private void showFailedResponse() {
        Toast.makeText(this, MyApplication.MESSAGE_TOAST_SERVER_NOTRESPONSE, Toast.LENGTH_SHORT).show();
    }
}