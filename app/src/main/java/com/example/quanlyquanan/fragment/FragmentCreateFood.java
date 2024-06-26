package com.example.quanlyquanan.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.quanlyquanan.R;
import com.example.quanlyquanan.adapter.AdapterSpinner;
import com.example.quanlyquanan.api.ApiError;
import com.example.quanlyquanan.api.FoodApi;
import com.example.quanlyquanan.model.Category;
import com.example.quanlyquanan.model.Food;
import com.example.quanlyquanan.model.file.RealPathUtil;
import com.example.quanlyquanan.permission.Permission;
import com.example.quanlyquanan.response.ResponseFood;
import com.example.quanlyquanan.response.ResponseFoodById;
import com.example.quanlyquanan.setting.MyApplication;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentCreateFood extends Fragment {
    private static final int REQUEST_CODE_READ_EXTERNAL_STOREAGE = 100;
    private View mView;
    EditText edtFoodName, edtSoLuong, edtNote, edtPrice;
    ImageView foodImage, btnIncrease, btnDecrease;
    SpinnerAdapter arrayAdapterFoodType;
    Button btnSubmit;
    Spinner spinnerCategory;
    List<Category> categoryList;
    ImageView imgBack;
    List<Food> foodList;
    Permission permission;
    Uri mUri;

    private ProgressDialog progressDialog;

    public FragmentCreateFood() {
        // Required empty public constructor
    }

    public static FragmentCreateFood newInstance(String param1, String param2) {
        FragmentCreateFood fragment = new FragmentCreateFood();
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_create_food, container, false);

        setControl();
        setEvent();

        return mView;
    }

    private void setControl() {
        edtFoodName = mView.findViewById(R.id.edt_foodname_fragment_createfood);
        edtSoLuong = mView.findViewById(R.id.edt_soluong_create_food);
        edtPrice = mView.findViewById(R.id.edt_price_fragment_createfood);
        edtNote = mView.findViewById(R.id.edt_note_fragment_createfood);
        btnSubmit = mView.findViewById(R.id.btn_submit_fragment_createfood);
        spinnerCategory = mView.findViewById(R.id.sp_foodtype_fragment_create_food);
        imgBack = mView.findViewById(R.id.ic_back_fragment_list_food);
        foodImage = mView.findViewById(R.id.img_addfood_fragment_create_food);

        btnIncrease = mView.findViewById(R.id.btn_increase_fragment_createfood);
        btnDecrease = mView.findViewById(R.id.btn_decrease_fragment_createfood);


        categoryList = (List<Category>) getArguments().get("category_list");
        foodList = (List<Food>) getArguments().get("food_list");

        permission = new Permission(this);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Vui lòng đợi");

        initView();
    }

    private void setEvent() {

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUri != null) {
                    postNewFood();
                } else {
                    Toast.makeText(getContext(), "Vui lòng chọn ảnh", Toast.LENGTH_LONG).show();
                }
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        foodImage.setOnClickListener(new View.OnClickListener() {
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
                String temp = edtSoLuong.getText().toString().trim();

                int soluong = Integer.parseInt(temp);
                soluong++;
                edtSoLuong.setText(String.valueOf(soluong));

            }
        });

        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = edtSoLuong.getText().toString().trim();

                int soluong = Integer.parseInt(temp);

                if (soluong > 0)
                    soluong--;

                edtSoLuong.setText(String.valueOf(soluong));

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        if (requestCode == MyApplication.PERMISSION_REQ_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
                Log.d("CHECKPERMISSION", "onRequestPermissionsResult: " + "if");
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, MyApplication.PERMISSION_READ_EXTERNAL_STORAGE)) {
                Log.d("CHECKPERMISSION", "onRequestPermissionsResult: " + "else if");

                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setMessage("Chức năng yêu cầu quyền truy cập thư viện ảnh\\nHãy cấp quyền truy cập bộ nhớ!")
                        .setTitle("Yêu cầu quyền truy cập bộ nhớ")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                                intent.setData(uri);
                                activity.startActivity(intent);

                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
            } else {
                Log.d("CHECKPERMISSION", "onRequestPermissionsResult: " + "else");
                permission.requestRuntimePermission();

            }
        }
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
                foodImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void postNewFood() {
        String realPathImage = RealPathUtil.getRealPath(getContext(), mUri);
        File file = new File(realPathImage);
//        RequestBody  requestBodyImgFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody requestBodyImgFile = RequestBody.create(MediaType.parse(requireActivity().getContentResolver().getType(mUri)), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName(), requestBodyImgFile);


        if (mUri != null) {

            Log.d("MYMY", String.valueOf(file.length()));
            Log.d("MYMY", "realPath = null " + (realPathImage == null ? "YES" : "NO"));
            Log.d("MYMY", "imgFile exists " + ((file.exists()) ? "YES" : "NO"));
            Log.d("MYMY", "File Length: " + file.length());
            Log.d("MYMY", "File Exists: " + file.exists());
            Log.d("MYMY", "File name: " + file.getName());
            Log.d("MYMY", "uri:   " + mUri);
            Log.d("MYMY", "realpath:  " + realPathImage);
        }


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

        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setMessage(MyApplication.MESSAGE_DIALOG_CREATE_FOOD);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Xác nhận", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
                Log.d("TAG", "onClick00000000000000000000000: " + mUri);
                if (mUri != null) {
                    uploadFoodWithImg(multipartBody,
                            edtFoodName.getText().toString().trim(),
                            edtPrice.getText().toString().trim(),
                            edtNote.getText().toString().trim(),
                            edtSoLuong.getText().toString().trim(),
                            category.get_id());
                }
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

    private void uploadFoodWithImg(MultipartBody.Part multipartBody, String name, String price, String note, String soLuong, String categoryId) {
        FoodApi.foodApi.postFood(
                multipartBody,
                name,
                price,
                note,
                soLuong,
                categoryId
        ).enqueue(new Callback<ResponseFoodById>() {
            @Override
            public void onResponse(Call<ResponseFoodById> call, Response<ResponseFoodById> response) {

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("Success")) {
                        Toast.makeText(getContext(), MyApplication.MESSAGE_TOAST_CREATEFOOD_SUCCESS, Toast.LENGTH_LONG).show();
                        ResponseFoodById responseFood = response.body();

                        Food food = responseFood.getFood();
                        foodList.add(food);

                        // reset edittext
                        edtFoodName.setText("");
                        edtPrice.setText("0");
                        edtSoLuong.setText("0");
                        edtNote.setText("");
                        foodImage.setImageResource(R.drawable.food_img_default);
                    } else {
                        Toast.makeText(getContext(), MyApplication.MESSAGE_TOAST_CREATEFOOD_FAILED, Toast.LENGTH_LONG).show();
                    }

                } else {
                    showErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseFoodById> call, Throwable t) {
//                Toast.makeText(getContext(), MyApplication.MESSAGE_TOAST_SERVER_NOTRESPONSE, Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_LONG).show();
                Log.d("TAG", "" + t.toString());
            }
        });
    }


    private void initView() {

        arrayAdapterFoodType = new AdapterSpinner(this.getContext(), R.layout.item_spinner_selected, categoryList);
        spinnerCategory.setAdapter(arrayAdapterFoodType);

//        loadCategoryList();
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