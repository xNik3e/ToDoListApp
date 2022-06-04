package com.example.todoplaceholder.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.todoplaceholder.R;
import com.example.todoplaceholder.adapters.CategoryBigPictureAdapter;
import com.example.todoplaceholder.models.CategoryModel;
import com.example.todoplaceholder.utils.view_services.ExpandableHeightGridView;
import com.example.todoplaceholder.viewmodels.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class CategoriesFragment extends Fragment {

    private Context context;
    private ExpandableHeightGridView gridView;
    private MainViewModel mainViewModel;
    private List<CategoryModel> categoryModelList = new ArrayList<>();
    private CategoryBigPictureAdapter adapter;

    private addNewCategoryFragment newCategoryFragment;


    public CategoriesFragment() {
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
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        gridView = view.findViewById(R.id.grid_view);
        adapter = new CategoryBigPictureAdapter(context, categoryModelList);
        gridView.setAdapter(adapter);

        newCategoryFragment = new addNewCategoryFragment();

        mainViewModel.getCategoryModels().observe(getActivity(), new Observer<List<CategoryModel>>() {
            @Override
            public void onChanged(List<CategoryModel> categoryModels) {
                if(categoryModels != null && !categoryModels.isEmpty()){
                    categoryModelList.clear();
                    categoryModelList.addAll(categoryModels);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt("POSITION", position);
                newCategoryFragment.setArguments(bundle);
                newCategoryFragment.show(getParentFragmentManager(), "EDIT");
            }
        });

    }
}