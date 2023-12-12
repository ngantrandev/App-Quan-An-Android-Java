package com.example.quanlyquanan.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquanan.R;
import com.example.quanlyquanan.activity.ActivityListFood;
import com.example.quanlyquanan.api.ApiError;
import com.example.quanlyquanan.api.FoodApi;
import com.example.quanlyquanan.model.Food;
import com.example.quanlyquanan.response.ResponseFoodById;
import com.example.quanlyquanan.setting.MyApplication;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterListFood extends RecyclerView.Adapter<AdapterListFood.ViewHolder> {
    private Context context;
    private List<Food> data;

    public AdapterListFood(Context context, List<Food> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.item_food, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food food = this.data.get(position);
        int oldPrice = food.getPrice();
        int newPrice = (int) (oldPrice * (1 - food.getDiscount()/100));

        holder.tvFoodName.setText(food.getName());
        holder.tvFoodDesc.setText(food.getDescription());
        holder.tvOldPrice.setText(String.valueOf(oldPrice));
        holder.tvNewPrice.setText(String.valueOf(newPrice));
        holder.tvSoLuongTon.setText(String.valueOf(food.getSoLuongTon()));
        holder.tvType.setText(food.getCategory().getName());

        if(oldPrice == newPrice) {
            holder.tvOldPrice.setVisibility(View.INVISIBLE);
            holder.tvCurrencyLabelOldPrice.setVisibility(View.INVISIBLE);
        }else {
            holder.tvOldPrice.setVisibility(View.VISIBLE);
            holder.tvCurrencyLabelOldPrice.setVisibility(View.VISIBLE);
        }

        try {
            String imageUri =MyApplication.BASE_API_URL +"/"+ food.getImgUrl();
            Picasso.get().load(imageUri).fit().centerCrop()
                    .placeholder(R.drawable.food_img_default)
                    .error(R.drawable.food_img_default)
                    .into(holder.foodImg);
        } catch (Exception e) {
            holder.foodImg.setImageResource(R.drawable.food_img_default);

        }

        setEvent(holder, position);

    }

    private void setEvent(ViewHolder holder, int position) {
        holder.btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityListFood activityListFood = (ActivityListFood) context;
                activityListFood.openFragmentUpdateFood(data.get(position));
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setMessage("Nhấn \"Xác nhận\" để xác nhận xóa món ăn");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Xác nhận", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                        Food food = data.get(position);
                        deleteFood(food);
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
    }

    private void deleteFood(Food food) {
        FoodApi.foodApi.deleteFood(food.get_id()).enqueue(new Callback<ResponseFoodById>() {
            @Override
            public void onResponse(Call<ResponseFoodById> call, Response<ResponseFoodById> response) {
                if (response.isSuccessful() && response.body().getStatus().equals("Success")) {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    data.remove(food);
                    notifyDataSetChanged();
                } else {
                    showErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseFoodById> call, Throwable t) {
                showFailedResponse();

            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvFoodName, tvFoodDesc, tvNewPrice, tvOldPrice, tvSoLuongTon, tvCurrencyLabelOldPrice, tvType;
        private ImageView foodImg;
        Button btnDelete, btnModify;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFoodName = itemView.findViewById(R.id.tv_foodname);
            tvFoodDesc = itemView.findViewById(R.id.tv_fooddesc);
            tvNewPrice = itemView.findViewById(R.id.tv_newprice);
            tvOldPrice = itemView.findViewById(R.id.tv_oldprice);
            tvType = itemView.findViewById(R.id.tvFoodType_itemfood_admin);
            tvSoLuongTon = itemView.findViewById(R.id.tv_soluongton);
            foodImg = itemView.findViewById(R.id.tv_foodimg);
            btnModify = itemView.findViewById(R.id.btn_modify_food);
            btnDelete = itemView.findViewById(R.id.btn_delete_food);
            tvCurrencyLabelOldPrice = itemView.findViewById(R.id.tv_currencylabel_item_listfood_admin);

        }
    }

    private void showErrorResponse(Response<?> response) {
        JsonParser parser = new JsonParser();
        JsonElement mJson = null;
        try {
            mJson = parser.parse(response.errorBody().string());
            Gson gson = new Gson();
            ApiError errorResponse = gson.fromJson(mJson, ApiError.class);

            Toast.makeText(context, errorResponse.getError(), Toast.LENGTH_SHORT).show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void showFailedResponse() {
        Toast.makeText(context, MyApplication.MESSAGE_TOAST_SERVER_NOTRESPONSE, Toast.LENGTH_SHORT).show();
    }


}
