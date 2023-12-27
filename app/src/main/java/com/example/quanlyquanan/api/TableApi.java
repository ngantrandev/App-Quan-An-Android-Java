package com.example.quanlyquanan.api;

import com.example.quanlyquanan.model.Category;
import com.example.quanlyquanan.model.Table;
import com.example.quanlyquanan.model.TransferTable;
import com.example.quanlyquanan.response.ResponseCategoryById;
import com.example.quanlyquanan.response.ResponseTable;
import com.example.quanlyquanan.response.ResponseTableById;
import com.example.quanlyquanan.setting.MyApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface TableApi {

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    TableApi tableApi = new Retrofit.Builder()
            .baseUrl(MyApplication.BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(TableApi.class);

    @GET("/tables")
    Call<ResponseTable> getTables();

    @GET("/tables/{id}")
    Call<ResponseTableById> getTalbe(@Path("id") String id);

    @PATCH("/tables/{id}")
    Call<ResponseTableById> changeTableInfo(@Path("id") String id, @Body Table table);

    @PUT("/tables/chuyenban")
    Call<ResponseTableById> chuyenban(@Body TransferTable transferTable);



//    @DELETE("/tables/{id}")
//    Call<ResponseTableById> deleteById(@Path("id") String id);
}
