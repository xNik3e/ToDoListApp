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

import com.example.todoplaceholder.MainActivity;
import com.example.todoplaceholder.R;
import com.example.todoplaceholder.adapters.CategoryBigPictureAdapter;
import com.example.todoplaceholder.interfaces.OnDismissInterface;
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
    private OnDismissInterface onDismissInterface;
    private addNewCategoryFragment newCategoryFragment;


    public CategoriesFragment(MainViewModel model) {
        this.mainViewModel = model;
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

        gridView = view.findViewById(R.id.grid_view);
        adapter = new CategoryBigPictureAdapter(context, categoryModelList);
        gridView.setAdapter(adapter);

        mainViewModel.getCategoryModels().observe(getActivity(), new Observer<List<CategoryModel>>() {
            @Override
            public void onChanged(List<CategoryModel> categoryModels) {
                if(categoryModels != null && !categoryModels.isEmpty()){
                    categoryModelList.clear();
                    categoryModelList.addAll(categoryModels);
                    newCategoryFragment = new addNewCategoryFragment(mainViewModel, categoryModelList, onDismissInterface);
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
                newCategoryFragment.show(getChildFragmentManager(), "EDIT");
            }
        });

        onDismissInterface = new OnDismissInterface() {
            @Override
            public void onDismissDialogAction() {
                newCategoryFragment = new addNewCategoryFragment(mainViewModel, categoryModelList);
            }
        };

    }
}