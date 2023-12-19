package com.example.quanlyquanan.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.quanlyquanan.R;
import com.example.quanlyquanan.api.ApiError;
import com.example.quanlyquanan.api.CategoryApi;
import com.example.quanlyquanan.api.FoodApi;
import com.example.quanlyquanan.fragment.FragmentListFood;
import com.example.quanlyquanan.fragment.FragmentUpdateFood;
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
    List<Food> foodList;
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
        frameLayout = findViewById(R.id.layout_content_activity_listfood);

        foodList = new ArrayList<>();
        categoryList = new ArrayList<>();

        openFragmentListFood();
        loadCategoryData();
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

                    FragmentListFood fragmentListFood = (FragmentListFood) getSupportFragmentManager().findFragmentById(R.id.layout_content_activity_listfood);
                    if (fragmentListFood != null) {
                        fragmentListFood.notifyDataLoaded();
                    }
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

                        FragmentListFood fragmentListFood = (FragmentListFood) getSupportFragmentManager().findFragmentById(R.id.layout_content_activity_listfood);
                        if (fragmentListFood != null) {
                            fragmentListFood.notifyDataLoaded();
                        }
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
        bundle.putSerializable("food_list", (Serializable) foodList);
        bundle.putSerializable("category_list", (Serializable) categoryList);

        fragmentListFood.setArguments(bundle);


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.layout_content_activity_listfood, fragmentListFood);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void openFragmentUpdateFood(Food targetFood) {

        FragmentUpdateFood fragmentUpdateFood = new FragmentUpdateFood();


        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("category_list", new ArrayList<>(categoryList));
        bundle.putParcelable("food", targetFood);
        fragmentUpdateFood.setArguments(bundle);


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.layout_content_activity_listfood, fragmentUpdateFood);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}