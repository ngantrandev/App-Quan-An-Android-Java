package com.example.quanlyquanan.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quanlyquanan.R;
import com.example.quanlyquanan.adapter.AdapterSpinner;
import com.example.quanlyquanan.api.ApiError;
import com.example.quanlyquanan.api.FoodApi;
import com.example.quanlyquanan.filter.DecimalDigitsInputFilter;
import com.example.quanlyquanan.model.Category;
import com.example.quanlyquanan.model.Food;
import com.example.quanlyquanan.model.UpdateFood;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentUpdateFood#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentUpdateFood extends Fragment {
    private View mView;
    ImageView icBack, foodImg;

    Spinner spinnerCategory;
    EditText edtFoodName, edtSoluong, edtPrice, edtDiscount, edtNote;
    Button btnChangeData;

    Food food;
    List<Category> categoryList;

    AdapterSpinner adapterSpinner;


    public FragmentUpdateFood() {
        // Required empty public constructor
    }

    public static FragmentUpdateFood newInstance(String param1, String param2) {
        FragmentUpdateFood fragment = new FragmentUpdateFood();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_update_food, container, false);

        setControl();
        setEvent();

        return mView;
    }


    private void setControl() {

        icBack = mView.findViewById(R.id.ic_back_fragment_updatefood);
        foodImg = mView.findViewById(R.id.img_addfood_fragment_modifyfood);
        edtFoodName = mView.findViewById(R.id.edt_foodname_fragment_modifyfood);
        edtSoluong = mView.findViewById(R.id.edt_soluong_fragment_modify_food);
        edtPrice = mView.findViewById(R.id.edt_price_fragment_modifyfood);
        edtDiscount = mView.findViewById(R.id.edt_discount_fragment_modifyfood);
        spinnerCategory = mView.findViewById(R.id.sp_foodtype_fragment_modify_food);
        edtNote = mView.findViewById(R.id.edt_note_fragment_modifyfood);
        btnChangeData = mView.findViewById(R.id.btn_submit_fragment_modifyfood);
        edtDiscount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter()});
//
        food = (Food) getArguments().getSerializable("food");
        categoryList = (List<Category>) getArguments().get("category_list");

        adapterSpinner = new AdapterSpinner(getContext(), R.layout.item_spinner_selected, categoryList);
        spinnerCategory.setAdapter(adapterSpinner);

//
//        // load data lên view
//
        edtFoodName.setText(food.getName());
        edtPrice.setText(String.valueOf(food.getPrice()));
        edtSoluong.setText(String.valueOf(food.getSoLuongTon()));
        edtDiscount.setText(String.valueOf(food.getDiscount()));
        edtNote.setText(food.getDescription());
//
        try {
            String imageUri = food.getImgUrl();
            Picasso.get().load(imageUri).fit().centerCrop()
                    .placeholder(R.drawable.food_img_default)
                    .error(R.drawable.food_img_default)
                    .into(foodImg);
        } catch (Exception e) {
            foodImg.setImageResource(R.drawable.food_img_default);
        }


        for (int i = 0; i < categoryList.size(); i++) {
            if (food.getCategory().get_id().equals(categoryList.get(i).get_id())) {
                spinnerCategory.setSelection(i);
                break;
            }
        }


    }

    private void setEvent() {

        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        btnChangeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setMessage("Nhấn \"Xác nhận\" để xác nhận lưu thay đổi");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Xác nhận", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                        updateFood(food);
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

    private void updateFood(Food food) {
        int selectedItemCategory = spinnerCategory.getSelectedItemPosition(); // lay index item spinner
        Category category = categoryList.get(selectedItemCategory); // ánh xạ sang categoryList

        if (edtFoodName.getText().length() == 0) {
            Toast.makeText(getContext(), "Không được để trống tên món \nVui lòng điền tên món", Toast.LENGTH_LONG).show();
            return;
        }
        if (category == null) {

            Toast.makeText(getContext(), "Chưa chọn loại món\nTạo danh mục món nếu chưa có", Toast.LENGTH_LONG).show();
            return;
        }

//
//        FoodApi.foodApi.updateFood(
//                food.get_id(),
//                edtFoodName.getText().toString().trim(),
//                edtPrice.getText().toString().trim(),
//                edtDiscount.getText().toString(),
//                edtNote.getText().toString().trim(),
//                edtSoluong.getText().toString().trim(),
//                String.valueOf(food.getStatus()),
//                category.get_id()
//        )

        UpdateFood updateFood = new UpdateFood(
                edtFoodName.getText().toString().trim(),
                edtPrice.getText().toString().trim(),
                edtDiscount.getText().toString(),
                edtNote.getText().toString().trim(),
                edtSoluong.getText().toString().trim(),
                String.valueOf(food.getStatus()),
                category.get_id());

        FoodApi.foodApi.updateFood(food.get_id(), updateFood)
                .enqueue(new Callback<ResponseFoodById>() {
                    @Override
                    public void onResponse(Call<ResponseFoodById> call, Response<ResponseFoodById> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equals("Success")) {
                                Food newFood = response.body().getFood();
                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();

                                // Cập nhật biến food với giá trị của newFood
                                food.setName(newFood.getName());
                                food.setPrice(newFood.getPrice());
                                food.setDiscount(newFood.getDiscount());
                                food.setDescription(newFood.getDescription());
                                food.setSoLuongTon(newFood.getSoLuongTon());
                                food.setStatus(newFood.getStatus());
                                food.setCategory(newFood.getCategory());
                                food.setImgUrl(newFood.getImgUrl());

                            }

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

    private void showErrorResponse(Response<?> response) {
        JsonParser parser = new JsonParser();
        JsonElement mJson = null;
        try {
            mJson = parser.parse(response.errorBody().string());
            Gson gson = new Gson();
            ApiError errorResponse = gson.fromJson(mJson, ApiError.class);

            Toast.makeText(getContext(), errorResponse.getError(), Toast.LENGTH_SHORT).show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void showFailedResponse() {
        Toast.makeText(getContext(), MyApplication.MESSAGE_TOAST_SERVER_NOTRESPONSE, Toast.LENGTH_SHORT).show();
    }
}