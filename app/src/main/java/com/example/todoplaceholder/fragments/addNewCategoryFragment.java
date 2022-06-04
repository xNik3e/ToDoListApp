package com.example.todoplaceholder.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todoplaceholder.MainActivity;
import com.example.todoplaceholder.R;
import com.example.todoplaceholder.adapters.ColorAdapter;
import com.example.todoplaceholder.models.CategoryModel;
import com.example.todoplaceholder.models.ColorModel;
import com.example.todoplaceholder.utils.Globals;
import com.example.todoplaceholder.viewmodels.MainViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.checkerframework.checker.units.qual.C;

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
    private List<ColorModel> colorModels = new ArrayList<>();
    private static List<CategoryModel> categoryModels = new ArrayList<>();
    private CategoryModel tempModel;
    private MainViewModel mainViewModel;
    private boolean isEditing;

    public addNewCategoryFragment(MainViewModel model) {
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
        return inflater.inflate(R.layout.fragment_add_new_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryModels = mainViewModel.getCategoryModels().getValue();
        colorRV = view.findViewById(R.id.recycler_view_color);
        titleLabel = view.findViewById(R.id.title_label);
        addButton = view.findViewById(R.id.addButton);
        nameContainer = view.findViewById(R.id.name_container);

        nameEditText = view.findViewById(R.id.name);
        deleteTrigger = view.findViewById(R.id.delete_trigger);

        mainViewModel.getBaseColor().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                int color = integer;
                setUIColors(color);
            }
        });

        colorAdapter = new ColorAdapter(context, colorModels);
        colorRV.setAdapter(colorAdapter);
        
        mainViewModel.getColorModelList().observe(getActivity(), new Observer<List<ColorModel>>() {
            @Override
            public void onChanged(List<ColorModel> models) {
                colorModels.clear();
                colorModels.addAll(models);
                int position = getPosition(colorModels);
                if(position == -1)
                    setUIColors(mainViewModel.getBaseColor().getValue());
                else
                    setUIColors(colorModels.get(position).getBaseColor());
                colorAdapter.notifyDataSetChanged();
            }
        });

        mainViewModel.getCategoryModels().observe(getActivity(), new Observer<List<CategoryModel>>() {
            @Override
            public void onChanged(List<CategoryModel> cModels) {
                categoryModels.clear();
                categoryModels.addAll(cModels);
            }
        });

        String tag =this.getTag();
        isEditing = !tag.equals("ADD");
        executeAction(isEditing);
    }


    private int getPosition(List<ColorModel> models){
        for(ColorModel m : models){
            if(m.isActive())
                return models.indexOf(m);
        }
        return -1;
    }

    private void setUIColors(int appColor) {
        addButton.setBackgroundTintList(ColorStateList.valueOf(appColor));
        nameContainer.setBoxStrokeColor(appColor);
        nameContainer.setBoxStrokeColorStateList(new ColorStateList(Globals.boxStates(), Globals.boxColors(appColor)));
        nameContainer.setDefaultHintTextColor(new ColorStateList(Globals.hintStates(), Globals.hintColors(appColor)));
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        mainViewModel.setColorList(ColorModel.populateColorList());
        nameContainer.getEditText().setText("");
    }

    public void executeAction(boolean isEditing) {
        if(isEditing){
            deleteTrigger.setVisibility(View.VISIBLE);
            titleLabel.setText("Edit category");
            addButton.setText("Apply");
            int index = getArguments().getInt("POSITION", 0);
            tempModel = mainViewModel.getCategoryModels().getValue().get(index);
            nameContainer.getEditText().setText(tempModel.getCategoryName());
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(getPosition(colorModels) == -1)
                        Toast.makeText(context, "Choose color", Toast.LENGTH_SHORT).show();
                    else{
                        if(nameEditText.getText().length() == -1)
                            Toast.makeText(context, "Name field should not be empty!", Toast.LENGTH_SHORT).show();
                        else{

                            tempModel.setColorId(getPosition(colorModels)+1);
                            tempModel.setCategoryName(nameEditText.getText().toString());
                            tempModel.createColorResources();

                            mainViewModel.updateCategory(tempModel);
                            dismiss();
                        }
                    }
                }
            });


        }else{
            deleteTrigger.setVisibility(View.GONE);
            titleLabel.setText("Add new category");
            addButton.setText("Add");
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(getPosition(colorModels) == -1)
                        Toast.makeText(context, "Choose color", Toast.LENGTH_SHORT).show();
                    else{
                        if(nameEditText.getText().length() == 0)
                            Toast.makeText(context, "Name field should not be empty!", Toast.LENGTH_SHORT).show();
                        else{
                            CategoryModel tempModel = new CategoryModel(nameEditText.getText().toString(), getPosition(colorModels)+1);
                            mainViewModel.insertCategory(tempModel);
                            dismiss();
                        }
                    }
                }
            });
        }
    }

    private void setColorPosition(int colorId) {
        colorModels = mainViewModel.getColorModelList().getValue();
        for(ColorModel model : colorModels){
            model.setActive(false);
        }
        colorModels.get(colorId-1).setActive(true);
        mainViewModel.setColorList(colorModels);
    }


}