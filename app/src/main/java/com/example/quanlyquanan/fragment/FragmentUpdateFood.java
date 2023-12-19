package com.example.quanlyquanan.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
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
import com.example.quanlyquanan.model.file.RealPathUtil;
import com.example.quanlyquanan.permission.Permission;
import com.example.quanlyquanan.response.ResponseFoodById;
import com.example.quanlyquanan.setting.MyApplication;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    ImageView icBack, foodImg, btnIncrease, btnDecrease;

    Spinner spinnerCategory;
    EditText edtFoodName, edtSoluong, edtPrice, edtDiscount, edtNote;
    Button btnChangeData;

    Food food;
    List<Category> categoryList;

    AdapterSpinner adapterSpinner;
    private Uri mUri;

    private Permission permission;


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
        permission = new Permission(this);

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

        btnIncrease = mView.findViewById(R.id.btn_increase_fragment_updatefood);
        btnDecrease = mView.findViewById(R.id.btn_decrease_fragment_updatefood);
//
//        food = (Food) getArguments().getSerializable("food");
//        categoryList = (List<Category>) getArguments().get("category_list");

        Bundle bundle = getArguments();
        if(bundle!=null){
            food = bundle.getParcelable("food");
            categoryList = bundle.getParcelableArrayList("category_list");
        }

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
//                        if(mUri == null) {
//                            Toast.makeText(getContext(), "Bạn phải chọn ảnh", Toast.LENGTH_LONG).show();
//                        }else {
                            updateFood(food);
//                        }
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

        foodImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CHECKPERMISSION", "onClick: ");
                if (!permission.isPermissionPickFileAccepted()) {
                    Log.d("CHECKPERMISSION", "click button : chua cap quyen");
                    permission.requestRuntimePermission();

                } else {
                    Log.d("CHECKPERMISSION", "click button : da cap quyen");
                    openGallery();
                }
            }
        });

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = edtSoluong.getText().toString().trim();

                int soluong = Integer.parseInt(temp);
                soluong++;
                edtSoluong.setText(String.valueOf(soluong));

            }
        });

        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = edtSoluong.getText().toString().trim();

                int soluong = Integer.parseInt(temp);

                if (soluong > 0)
                    soluong--;

                edtSoluong.setText(String.valueOf(soluong));

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

//        String realPathImage = RealPathUtil.getRealPath(getContext(), mUri);
//        File file = new File(realPathImage);
////        RequestBody  requestBodyImgFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        RequestBody requestBodyImgFile = RequestBody.create(MediaType.parse(requireActivity().getContentResolver().getType(mUri)), file);
//        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName(), requestBodyImgFile);

//        String foodId = food.get_id();
//        String foodName = edtFoodName.getText().toString().trim();
//        String price = edtPrice.getText().toString().trim();
//        String note = edtNote.getText().toString().trim();
//        String discount = edtDiscount.getText().toString().trim();
//        String soluong = edtSoluong.getText().toString().trim();
//        String categoryId = category.get_id();


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

//        FoodApi.foodApi.updateFood(food.get_id(),
//                        multipartBody,
//                        edtFoodName.getText().toString().trim(),
//                        edtPrice.getText().toString().trim(),
//                        edtDiscount.getText().toString().trim(),
//                        edtNote.getText().toString().trim(),
//                        edtSoluong.getText().toString().trim(),
//                        category.get_id())
//                .enqueue(new Callback<ResponseFoodById>() {
//                    @Override
//                    public void onResponse(Call<ResponseFoodById> call, Response<ResponseFoodById> response) {
//                        if (response.isSuccessful()) {
//                            if (response.body().getStatus().equals("Success")) {
//                                Food newFood = response.body().getFood();
//                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
//
//                                // Cập nhật biến food với giá trị của newFood
//                                food.setName(newFood.getName());
//                                food.setPrice(newFood.getPrice());
//                                food.setDiscount(newFood.getDiscount());
//                                food.setDescription(newFood.getDescription());
//                                food.setSoLuongTon(newFood.getSoLuongTon());
//                                food.setStatus(newFood.getStatus());
//                                food.setCategory(newFood.getCategory());
//                                food.setImgUrl(newFood.getImgUrl());
//
//                            }
//
//                        } else {
//                            showErrorResponse(response);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseFoodById> call, Throwable t) {
//                        showFailedResponse();
//                    }
//                });

    }

    private void openGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Choose image"), MyApplication.REQUEST_CODE_CHOOSE_FILE);
//        registerForActivityResult();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MyApplication.REQUEST_CODE_CHOOSE_FILE && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            mUri = selectedImage;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImage);
                foodImg.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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