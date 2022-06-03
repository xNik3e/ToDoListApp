package com.example.todoplaceholder.fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.todoplaceholder.R;
import com.example.todoplaceholder.utils.Globals;
import com.example.todoplaceholder.viewmodels.MainViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SearchFragment extends Fragment {

    private Context context;
    private MainViewModel mainViewModel;
    private int appColor;
    private TextInputLayout searchContainer;
    private TextInputEditText search;
    private RecyclerView searchItemRV;
    private TextView nothingHere;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchContainer = view.findViewById(R.id.search_container);
        search = view.findViewById(R.id.search);
        nothingHere = view.findViewById(R.id.text_nothing);
        searchItemRV = view.findViewById(R.id.recycler_view_search);


        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        mainViewModel.getBaseColor().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                appColor = integer;
                setUIColors();
            }
        });

    }

    private void setUIColors() {

        searchContainer.setBoxStrokeColor(appColor);
        searchContainer.setBoxStrokeColorStateList(new ColorStateList(Globals.boxStates(), Globals.boxColors(appColor)));
        searchContainer.setDefaultHintTextColor(new ColorStateList(Globals.hintStates(), Globals.hintColors(appColor)));

        nothingHere.setTextColor(appColor);
    }
}