package com.example.todoplaceholder.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todoplaceholder.R;
import com.example.todoplaceholder.adapters.ColorAdapter;
import com.example.todoplaceholder.interfaces.ColorAdapterChangeColorInterface;
import com.example.todoplaceholder.interfaces.OnDismissInterface;
import com.example.todoplaceholder.models.CategoryModel;
import com.example.todoplaceholder.models.ColorModel;
import com.example.todoplaceholder.utils.Globals;
import com.example.todoplaceholder.utils.utils.DatabaseValidator;
import com.example.todoplaceholder.viewmodels.MainViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class addNewCategoryFragment extends BottomSheetDialogFragment {

    private Context context;
    private RecyclerView colorRV;
    private TextView titleLabel, addButton;
    private TextInputLayout nameContainer;
    private TextInputEditText nameEditText;
    private ImageView deleteTrigger;
    private ColorAdapter colorAdapter;
    private List<ColorModel> colorModelList = ColorModel.populateColorList();
    private List<CategoryModel> categoryModels = new ArrayList<>();
    private MainViewModel mainViewModel;
    private boolean isEditing;
    private int index;
    private ColorAdapterChangeColorInterface colorAdapterChangeColorInterface;
    private String name;
    private OnDismissInterface dismissInterface;

    public addNewCategoryFragment(MainViewModel model, List<CategoryModel> modelList, OnDismissInterface onDismissInterface) {
        this.mainViewModel = model;
        this.categoryModels.addAll(modelList);
        this.dismissInterface = onDismissInterface;
    }

    public addNewCategoryFragment(MainViewModel model, List<CategoryModel> modelList) {
        this.mainViewModel = model;
        this.categoryModels.addAll(modelList);
        this.dismissInterface = null;
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
        return inflater.inflate(R.layout.fragment_add_new_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String tag = this.getTag();
        isEditing = !tag.equals("ADD");

        Bundle bundle = getArguments();
        if (bundle != null) {
            index = bundle.getInt("POSITION", 0);
            name = categoryModels.get(index).getCategoryName();
        }

        colorRV = view.findViewById(R.id.recycler_view_color);
        titleLabel = view.findViewById(R.id.title_label);
        addButton = view.findViewById(R.id.addButton);
        nameContainer = view.findViewById(R.id.name_container);
        nameEditText = view.findViewById(R.id.name);
        deleteTrigger = view.findViewById(R.id.delete_trigger);

        colorAdapterChangeColorInterface = new ColorAdapterChangeColorInterface() {
            @Override
            public void invertSelectedColor(int position) {
                invertSelection(position);
            }
        };

        mainViewModel.getCategoryModels().observe(getActivity(), new Observer<List<CategoryModel>>() {
            @Override
            public void onChanged(List<CategoryModel> modelList) {
                if (categoryModels.size() < modelList.size()) {
                    categoryModels.clear();
                    categoryModels.addAll(modelList);
                    name = categoryModels.get(index).getCategoryName();
                }
            }
        });

        colorAdapter = new ColorAdapter(context, colorModelList, colorAdapterChangeColorInterface);
        colorRV.setAdapter(colorAdapter);

        if (isEditing) {
            deleteTrigger.setVisibility(View.VISIBLE);
            titleLabel.setText("Edit category");
            addButton.setText("Apply");
            invertSelection(categoryModels.get(index).getColorId() - 1);
            nameContainer.getEditText().setText("");
            nameEditText.setText(name);
        } else {
            nameContainer.getEditText().setText("");
            deleteTrigger.setVisibility(View.GONE);
            titleLabel.setText("Add new category");
            addButton.setText("Add");
            mainViewModel.getBaseColor().observe(getActivity(), new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    setUIColors(integer);
                }
            });
        }

        deleteTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, categoryModels.get(index).getCategoryName() + " was deleted!", Toast.LENGTH_SHORT).show();
                CategoryModel temp = categoryModels.get(index);

                DatabaseValidator.validate(temp, mainViewModel, true);
                DatabaseValidator.isValidationCompleted().observe(getActivity(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean){
                            mainViewModel.deleteCategory(categoryModels.get(index).getCategoryName());
                            dismiss();
                        }
                    }
                });
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditing) {
                    if (nameContainer.getEditText().getText().length() == 0) {
                        Toast.makeText(context, "Name field should not be empty!", Toast.LENGTH_SHORT).show();
                    } else {
                        categoryModels.get(index).setCategoryName(nameEditText.getText().toString());
                        categoryModels.get(index).setColorId(getActiveColor());
                        categoryModels.get(index).createColorResources();

                        CategoryModel temp = categoryModels.get(index);

                        DatabaseValidator.validate(temp, mainViewModel, false);

                        DatabaseValidator.isValidationCompleted().observe(getActivity(), new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean aBoolean) {
                                if (aBoolean) {
                                    mainViewModel.updateCategory(categoryModels.get(index));
                                    dismiss();
                                }
                            }
                        });

                    }
                } else {
                    if (!isAnythingSelected()) {
                        Toast.makeText(context, "Please, select color!", Toast.LENGTH_SHORT).show();
                    } else if (nameContainer.getEditText().getText().length() == 0) {
                        Toast.makeText(context, "Name field should not be empty!", Toast.LENGTH_SHORT).show();
                    } else {
                        CategoryModel tempModel = new CategoryModel(nameEditText.getText().toString(), getActiveColor());
                        mainViewModel.insertCategory(tempModel);
                        dismiss();
                    }
                }
            }
        });
    }

    private void setUIColors(int appColor) {
        addButton.setBackgroundTintList(ColorStateList.valueOf(appColor));
        nameContainer.setBoxStrokeColor(appColor);
        nameContainer.setBoxStrokeColorStateList(new ColorStateList(Globals.boxStates(), Globals.boxColors(appColor)));
        nameContainer.setDefaultHintTextColor(new ColorStateList(Globals.hintStates(), Globals.hintColors(appColor)));
    }


    private void invertSelection(int position) {
        for (ColorModel model : colorModelList) {
            model.setActive(false);
        }
        colorModelList.get(position).setActive(true);
        setUIColors(colorModelList.get(position).getBaseColor());
        colorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        colorModelList = ColorModel.populateColorList();
        nameContainer.getEditText().setText("");
        if (dismissInterface != null)
            dismissInterface.onDismissDialogAction();
    }

    private boolean isAnythingSelected() {
        for (ColorModel m : colorModelList) {
            if (m.isActive())
                return true;
        }
        return false;
    }

    private int getActiveColor() {
        int i = 1;
        for (ColorModel m : colorModelList) {
            if (m.isActive())
                return i;
            i++;
        }
        return 0;
    }

}