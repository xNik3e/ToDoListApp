package com.example.todoplaceholder.utils.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.todoplaceholder.models.CategoryModel;
import com.example.todoplaceholder.models.TaskModel;
import com.example.todoplaceholder.viewmodels.MainViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DatabaseValidator {

    private static MutableLiveData<Boolean> validationProcessCompletion = new MutableLiveData<>();


    public static LiveData<Boolean> isValidationCompleted(){return validationProcessCompletion;}

    public static void validate(CategoryModel categoryModel, MainViewModel viewModel, boolean wasDeleted) {
        validationProcessCompletion.setValue(false);

        List<TaskModel> resultTasks = new ArrayList<>();
        List<Integer> indexes = new ArrayList<>();
        List<TaskModel> tasks = viewModel.getTaskModels().getValue();

        IntStream.range(0, tasks.size())
                .filter(i -> {
                    if(tasks.get(i).getModel() == null)
                        return false;
                    else{
                        return tasks.get(i).getModel().getId() == categoryModel.getId();
                    }
                }).forEach(indexes::add);

        if(wasDeleted){
            indexes.forEach(i ->{
                tasks.get(i).setModel(null);
            });
        }else{
            indexes.forEach(i ->{
                tasks.get(i).getModel().setCategoryName(categoryModel.getCategoryName());
                tasks.get(i).getModel().setColorId(categoryModel.getColorId());
                tasks.get(i).getModel().createColorResources();
            });
        }
        resultTasks.addAll(tasks);
        viewModel.insertAllTasks(resultTasks);

        validationProcessCompletion.setValue(true);
    }
}
