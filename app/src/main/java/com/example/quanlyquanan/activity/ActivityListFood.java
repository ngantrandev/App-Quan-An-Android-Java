package com.example.quanlyquanan.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quanlyquanan.R;
import com.example.quanlyquanan.api.ApiError;
import com.example.quanlyquanan.api.CategoryApi;
import com.example.quanlyquanan.api.FoodApi;
import com.example.quanlyquanan.fragment.FragmentListFood;
import com.example.quanlyquanan.model.Category;
import com.example.quanlyquanan.model.Food;
import com.example.quanlyquanan.response.ResponseCategories;
import com.example.quanlyquanan.response.ResponseFood;
import com.example.quanlyquanan.setting.MyApplication;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityListFood extends AppCompatActivity {
    FrameLayout frameLayout;
    Spinner spinner;
    List<Food> foodList;
    List<String> categoryLables;
    List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listfood);

        Log.d("CHECKACTIVITY", "ACtivityListFood On create");

        setControl();
        setEvent();
    }

    private void setEvent() {

    }

    private void setControl() {
        spinner = findViewById(R.id.sp_fragment_listfood);

        frameLayout = findViewById(R.id.layout_content);

        foodList = new ArrayList<>();
        categoryLables = new ArrayList<>();
        categoryList = new ArrayList<>();


        loadListFoodData();
    }

    private void loadListFoodData() {
        FoodApi.foodApi.getFoods().enqueue(new Callback<ResponseFood>() {
            @Override
            public void onResponse(Call<ResponseFood> call, Response<ResponseFood> response) {
                if (response.isSuccessful()) {
                    ResponseFood responseFood = response.body();
                    foodList.clear();
                    foodList.addAll(responseFood.getFoodList());

                    loadCategoryData();
                } else {
                    showErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseFood> call, Throwable t) {
                showFailedResponse();
            }
        });
    }


    private void loadCategoryData() {

        CategoryApi.categoryApi.getCategories().enqueue(new Callback<ResponseCategories>() {
            @Override
            public void onResponse(Call<ResponseCategories> call, Response<ResponseCategories> response) {
                if (response.isSuccessful()) {
                    ResponseCategories responseCategories = response.body();

                    if (responseCategories.getStatus().equals("Success")) {
                        categoryList.clear();
                        categoryList.addAll(responseCategories.getCategoryList());

                        setCategoryLables();

                        openFragmentListFood();
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

    private void setCategoryLables() {
        categoryLables.clear();

        for (int i = 0; i < categoryList.size(); i++) {
            categoryLables.add(categoryList.get(i).getName());
        }
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


    private void openFragmentListFood() {
        FragmentListFood fragmentListFood = new FragmentListFood();
        Bundle bundle = new Bundle();
        if (foodList != null && categoryLables != null) {
            bundle.putSerializable("food_list", (Serializable) foodList);
            bundle.putStringArrayList("category_labels", (ArrayList<String>) categoryLables);
            bundle.putSerializable("category_list", (Serializable) categoryList);

        }
        fragmentListFood.setArguments(bundle);


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.layout_content, fragmentListFood);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("CHECKACTIVITY", "ACtivityListFood On start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("CHECKACTIVITY", "ACtivityListFood On resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("CHECKACTIVITY", "ACtivityListFood On pause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("CHECKACTIVITY", "ACtivityListFood On stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("CHECKACTIVITY", "ACtivityListFood On destroy");
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public List<String> getCategoryLables() {
        return categoryLables;
    }
}