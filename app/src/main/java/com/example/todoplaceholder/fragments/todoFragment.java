package com.example.todoplaceholder.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.todoplaceholder.MainActivity;
import com.example.todoplaceholder.R;
import com.example.todoplaceholder.viewmodels.MainViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class todoFragment extends Fragment {


    private Context context;
    private View topBackground;
    private RecyclerView calendarRV, categoryRV, todoRV;
    private TextInputLayout searchContainer;
    private TextInputEditText searchEditText;
    private int appColor;

    private MainViewModel mainViewModel;

    public todoFragment() {
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
        return inflater.inflate(R.layout.fragment_todo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        topBackground = v.findViewById(R.id.viewBackground);
        calendarRV = v.findViewById(R.id.recycler_view_calendar);
        categoryRV = v.findViewById(R.id.recycler_view_category);
        todoRV = v.findViewById(R.id.recycler_view_todo);
        searchContainer = v.findViewById(R.id.search_container);
        searchEditText = v.findViewById(R.id.search);

        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        mainViewModel.getBaseColor().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                appColor = integer;
                setUIColors();
            }
        });


    }

    private void setUIColors(){
        topBackground.setBackgroundColor(appColor);
        searchContainer.setBoxStrokeColor(appColor);
    }
}