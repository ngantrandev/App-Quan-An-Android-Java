package com.example.quanlyquanan.api;

import com.example.quanlyquanan.model.Category;
import com.example.quanlyquanan.response.ResponseCategories;
import com.example.quanlyquanan.response.ResponseCategoryById;
import com.example.quanlyquanan.response.ResponseFood;
import com.example.quanlyquanan.setting.MyApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface CategoryApi {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    CategoryApi categoryApi = new Retrofit.Builder().baseUrl(MyApplication.BASE_API_URL).addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CategoryApi.class);

    @GET("/categories/")
    Call<ResponseCategories> getCategories();

    @FormUrlEncoded
    @POST("/categories")
    Call<ResponseCategoryById> createCategory(@Field("name") String name);

    @PATCH("/categories/{id}")
    Call<ResponseCategoryById> changeCategoryName(@Path("id") String id, @Body Category category);

    @DELETE("/categories/{id}")
    Call<ResponseCategoryById> deleteById(@Path("id") String id);

}
