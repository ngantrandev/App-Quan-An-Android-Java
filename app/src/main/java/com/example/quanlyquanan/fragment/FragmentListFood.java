package com.example.quanlyquanan.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.widget.SearchView;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quanlyquanan.R;
import com.example.quanlyquanan.adapter.AdapterListFood;
import com.example.quanlyquanan.adapter.AdapterSpinner;
import com.example.quanlyquanan.model.Category;
import com.example.quanlyquanan.model.Food;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListFood#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListFood extends Fragment {

    private View mView;
    private ImageButton btnAddFood;
    Spinner spinnerFoodType;
    SearchView searchView;
    List<Category> categoryList, categoryListLabel;
    List<Food> foodList,tempFoodList;

    AdapterSpinner spinnerAdapter;
    AdapterListFood adapterListFood;
    RecyclerView recyclerView;
    ImageView imgBack;


    public FragmentListFood() {
        // Required empty public constructor
    }

    public static FragmentListFood newInstance(String param1, String param2) {
        FragmentListFood fragment = new FragmentListFood();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_list_food, container, false);

        setControl();
        setEvent();


        Log.d("FragmentListFood", "onCreateView: ");

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setEvent() {
        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragmentCreateFood();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().finish();
            }
        });

        spinnerFoodType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tempFoodList.clear();
                Category category = categoryListLabel.get(position);
                Toast.makeText(getContext(), "" + category.getName(), Toast.LENGTH_SHORT).show();

                for(int i = 0;i<foodList.size();i++) {
                    Food food = foodList.get(i);
                    if (food.getCategory().getName().equals(category.getName()) || category.getName().equals("Tất cả")) {
                        tempFoodList.add(foodList.get(i));
                    }
                }

                adapterListFood.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }

    private void setControl() {

        btnAddFood = mView.findViewById(R.id.btn_add_food);
        spinnerFoodType = mView.findViewById(R.id.sp_fragment_listfood);
        searchView = mView.findViewById(R.id.sv_fragment_listfood);
        recyclerView = mView.findViewById(R.id.rv_fragment_list_food);
        imgBack = mView.findViewById(R.id.ic_back_fragment_list_food);

        tempFoodList = new ArrayList<>();
//        foodList = new ArrayList<>();

        categoryListLabel = new ArrayList<>();
        categoryList = (List<Category>) getArguments().get("category_list");
        foodList = (List<Food>) getArguments().get("food_list");



        spinnerAdapter = new AdapterSpinner(getContext(), R.layout.item_spinner_dropdown, categoryListLabel);
        spinnerFoodType.setAdapter(spinnerAdapter);

        adapterListFood = new AdapterListFood(getContext(), tempFoodList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterListFood);
    }


    private void openFragmentCreateFood() {

        FragmentCreateFood fragmentCreateFood = new FragmentCreateFood();
        if (categoryList != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("category_list", (Serializable) categoryList);
            bundle.putSerializable("food_list", (Serializable) foodList);

            fragmentCreateFood.setArguments(bundle);
        }

        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.layout_content_activity_listfood, fragmentCreateFood);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void notifyDataLoaded() {
        categoryListLabel.clear();
        categoryListLabel.add(new Category("0", "Tất cả", 0));
        categoryListLabel.addAll(categoryList);

        tempFoodList.addAll(foodList);

        spinnerAdapter.notifyDataSetChanged();
        adapterListFood.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();

        notifyDataLoaded();
    }
}