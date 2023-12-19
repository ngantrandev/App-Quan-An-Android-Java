package com.example.quanlyquanan.api;

import com.example.quanlyquanan.model.User;
import com.example.quanlyquanan.response.ResponseUser;
import com.example.quanlyquanan.response.ResponseUserLogin;
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
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserApi {

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    UserApi userApi = new Retrofit.Builder()
            .baseUrl(MyApplication.BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(UserApi.class);
    //
    @GET("/users/")
    Call<ResponseUser> getUsers();
    @GET("/users/{username}")
    Call<ResponseUserLogin> getUser(@Path("username") String username);
    @POST("/users/signup")
    Call<ResponseUser> postUser(@Body User user);
    @POST("/users/login")
    Call<ResponseUserLogin> LoginUser(@Body User user);
}
