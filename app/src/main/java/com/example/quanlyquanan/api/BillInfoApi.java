package com.example.quanlyquanan.api;

import com.example.quanlyquanan.model.BillInfo;
import com.example.quanlyquanan.response.ResponseBillInfo;
import com.example.quanlyquanan.response.ResponseBillInfoById;
import com.example.quanlyquanan.setting.MyApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BillInfoApi {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    BillInfoApi billInfoApi = new Retrofit.Builder()
            .baseUrl(MyApplication.BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(BillInfoApi.class);

    @GET("/billinfo")
    Call<ResponseBillInfo> getBillInfoList();

    @GET("/billinfo/{id}")
    Call<ResponseBillInfoById> getBillInfoById();

    @PATCH("/billinfo/{id}")
    Call<ResponseBillInfoById> updateBillInfo(@Path("id")String id, @Body BillInfo tempBillInfo);

    @POST("/billinfo/")
    Call<ResponseBillInfoById> createBillInfo(@Body BillInfo billinfo);
}
