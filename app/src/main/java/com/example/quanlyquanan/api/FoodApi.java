package com.example.quanlyquanan.api;

import com.example.quanlyquanan.model.Food;
import com.example.quanlyquanan.model.UpdateFood;
import com.example.quanlyquanan.response.ResponseFood;
import com.example.quanlyquanan.response.ResponseFoodById;
import com.example.quanlyquanan.setting.MyApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

public interface FoodApi {

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    FoodApi foodApi = new Retrofit.Builder()
            .baseUrl(MyApplication.BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(FoodApi.class);

    //
    @GET("/foods/")
    Call<ResponseFood> getFoods();

    @Multipart
    @POST("/foods/")
    Call<ResponseFoodById> postFood(
            @Part MultipartBody.Part file,
            @Part("name") String name,
            @Part("price") String price,
            @Part("description") String description,
            @Part("soLuongTon") String soLuongTon,
            @Part("category") String categoryID
    );

//    Call<ResponseFoodById> postFood(@Part MultipartBody.Part file,
//                                    @Part("name") String name,
//                                    @Part("price") int price,
//                                    @Part("description") String description,
//                                    @Part("soLuongTon") int soLuongTon,
//                                    @Part("category") String categoryID);
//    Call<ResponseFoodById> postFood(@Part MultipartBody.Part file, @Part("food") RequestBody food);

    @DELETE("/foods/{id}")
    Call<ResponseFoodById> deleteFood(@Path("id") String id);

//    @Multipart
//    @PATCH("/foods/{id}")
//    Call<ResponseFoodById> updateFood(@Path("id")String id,
//                                      @Part("name")String name,
//                                      @Part("price") String price,
//                                      @Part("discount") String discount,
//                                      @Part("description") String description,
//                                      @Part("soLuongTon") String soLuongTon,
//                                      @Part("status") String status,
//                                      @Part("category") String category);

    @PATCH("/foods/{id}")
    Call<ResponseFoodById> updateFood(@Path("id") String id, @Body UpdateFood updateFood);
//    @Multipart
//    @PATCH("/foods/{id}")
//    Call<ResponseFoodById> updateFood(
//            @Path("id") String id,
//            @Part MultipartBody.Part file,
//            @Part("name") String name,
//            @Part("price") String price,
//            @Part("discount") String discount,
//            @Part("description") String description,
//            @Part("soLuongTon") String soLuongTon,
//            @Part("category") String categoryID
//    );
}
