package com.example.quanlyquanan.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.quanlyquanan.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentUpdateFood#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentUpdateFood extends Fragment {
    private View mView;
    ImageView icBack;
    

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

        mView= inflater.inflate(R.layout.fragment_update_food, container, false);
        
        setControl();
        setEvent();
        
        return mView;
    }

    private void setEvent() {

        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void setControl() {

        icBack = mView.findViewById(R.id.ic_back_fragment_updatefood);


        
    }
}