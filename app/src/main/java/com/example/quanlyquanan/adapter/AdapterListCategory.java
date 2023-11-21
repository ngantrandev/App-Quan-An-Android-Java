package com.example.quanlyquanan.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyquanan.R;
import com.example.quanlyquanan.api.ApiError;
import com.example.quanlyquanan.api.CategoryApi;
import com.example.quanlyquanan.model.Category;
import com.example.quanlyquanan.response.ResponseCategoryById;
import com.example.quanlyquanan.setting.MyApplication;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterListCategory extends RecyclerView.Adapter<AdapterListCategory.ViewHolder> {
    private Context context;
    List<Category> categoryList;
    View mview;

    public AdapterListCategory(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mview = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(mview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(categoryList.get(position).getName().toString());
        holder.tvFoodNumber.setText(String.valueOf(categoryList.get(position).getFoodnumber()));

        setEvent(holder, position);
    }

    private void setEvent(ViewHolder holder, int position) {
        holder.btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetChangeName(categoryList.get(position));
            }
        });

        holder.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDeleteCategory(categoryList.get(position));
            }
        });

    }

    private void onClickDeleteCategory(Category category) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setMessage("Xác nhận xóa danh mục món\nĐồng thời xóa tất cả món ăn liên quan\nHành động này không thể hoàn tác");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Xác nhận", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();

                deleteCategory(category);
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

    private void deleteCategory(Category category) {
        CategoryApi.categoryApi.deleteById(category.get_id()).enqueue(new Callback<ResponseCategoryById>() {
            @Override
            public void onResponse(Call<ResponseCategoryById> call, Response<ResponseCategoryById> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("Success")) {
                        Toast.makeText(context, "Xóa danh mục thành công", Toast.LENGTH_LONG).show();
                        categoryList.remove(category);
                        notifyDataSetChanged();
                    }
                } else {
                    showErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseCategoryById> call, Throwable t) {
                Toast.makeText(context, MyApplication.MESSAGE_TOAST_SERVER_NOTRESPONSE, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openBottomSheetChangeName(Category tagetCategory) {

        View viewBottomSheet = LayoutInflater.from(context).inflate(R.layout.layout_bottomsheet_category, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(viewBottomSheet);
        Button btnXacNhan = bottomSheetDialog.findViewById(R.id.btn_submitname_bottomsheet_category);
        TextView tvTitle = bottomSheetDialog.findViewById(R.id.tv_title_bottomsheet_category);
        EditText edtName = bottomSheetDialog.findViewById(R.id.edt_name_bottomsheet_category);
        tvTitle.setText("Thay đổi tên danh mục");
        edtName.setText(tagetCategory.getName());

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setMessage("Xác nhận đổi tên\nHành động này không thể hoàn tác");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Xác nhận", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                        bottomSheetDialog.dismiss();

                        saveDataToServer(tagetCategory, edtName.getText().toString());
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
//        bottomSheetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void saveDataToServer(Category tagetCategory, String newName) {
        CategoryApi.categoryApi.changeCategoryName(
                        tagetCategory.get_id(),
                        new Category(tagetCategory.get_id(), newName, tagetCategory.getFoodnumber()))
                .enqueue(new Callback<ResponseCategoryById>() {
                    @Override
                    public void onResponse(Call<ResponseCategoryById> call, Response<ResponseCategoryById> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equals("Success")) {
                                Toast.makeText(context, "Đổi tên thành công", Toast.LENGTH_LONG).show();
                                tagetCategory.setName(response.body().getCategory().getName());
                                notifyDataSetChanged();
                            }
                        } else {
                            showErrorResponse(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseCategoryById> call, Throwable t) {
                        Toast.makeText(context, MyApplication.MESSAGE_TOAST_SERVER_NOTRESPONSE, Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public int getItemCount() {
        return categoryList == null ? 0 : categoryList.size();
    }


    protected class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvFoodNumber;
        Button btnXoa, btnSua;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_categoryname_item_category);
            tvFoodNumber = itemView.findViewById(R.id.tv_food_size_item_category);
            btnSua = itemView.findViewById(R.id.btn_modify_item_category);
            btnXoa = itemView.findViewById(R.id.btn_delete_item_category);
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


}
