package com.example.quanlyquanan.api;

import com.example.quanlyquanan.response.ResponseBill;
import com.example.quanlyquanan.setting.MyApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface BillApi {

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    BillApi foodApi = new Retrofit.Builder()
            .baseUrl(MyApplication.BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(BillApi.class);

    @GET("/bills")
    Call<ResponseBill> getBills();
}
