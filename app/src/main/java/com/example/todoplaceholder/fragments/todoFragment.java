package com.example.todoplaceholder.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.todoplaceholder.EditActivity;
import com.example.todoplaceholder.R;
import com.example.todoplaceholder.adapters.CategoryAdapter;
import com.example.todoplaceholder.adapters.DateScreenAdapter;
import com.example.todoplaceholder.adapters.TaskAdapter;
import com.example.todoplaceholder.interfaces.CategoryAdapterNotifier;
import com.example.todoplaceholder.interfaces.OnCalendarDateChange;
import com.example.todoplaceholder.interfaces.TaskDeActivatorInterface;
import com.example.todoplaceholder.models.CategoryModel;
import com.example.todoplaceholder.models.DateModel;
import com.example.todoplaceholder.models.TaskModel;
import com.example.todoplaceholder.utils.utils.SpaceItemDecoration;
import com.example.todoplaceholder.utils.view_services.App;
import com.example.todoplaceholder.viewmodels.MainViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class todoFragment extends Fragment {

    private Context context;
    private View topBackground;
    private RecyclerView calendarRV, categoryRV;
    private SwipeableRecyclerView todoRV;
    private TextInputLayout searchContainer;
    private TextInputEditText searchEditText;
    private int appColor;
    private CategoryAdapter categoryAdapter;
    private TaskAdapter taskAdapter;
    private DateScreenAdapter dateAdapter;
    private TextView nothingHere;
    private CoordinatorLayout coordinatorLayout;
    private Snackbar snackbar;
    private boolean isDeleted = false;
    private String nameToDelete;
    private OnCalendarDateChange onCalendarDateChangeInterface;
    private CategoryAdapterNotifier categoryAdapterNotifier;
    private TaskDeActivatorInterface deActivatorInterface;

    private List<CategoryModel> categoryModelList = new ArrayList<>();
    private List<TaskModel> taskModelList = new ArrayList<>();
    private List<TaskModel> sortedTaskModelList = new ArrayList<>();
    private List<TaskModel> previousSortedTaskModelList = new ArrayList<>();
    private List<DateModel> dateModelList = new ArrayList<>();
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<Boolean>();

    private MainViewModel mainViewModel;

    public todoFragment() {

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
        nothingHere = v.findViewById(R.id.text_nothing);
        coordinatorLayout = v.findViewById(R.id.coordinatorLayout);

        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        onCalendarDateChangeInterface = new OnCalendarDateChange() {
            @Override
            public void forceTaskAdapterChange() {
                isUpdating.setValue(true);
                List<DateModel> tempDateModelList = new ArrayList<>();
                tempDateModelList.addAll(dateAdapter.getDateModels());
                dateModelList.clear();
                dateModelList.addAll(tempDateModelList);
                dateAdapter.notifyDataSetChanged();
                isUpdating.setValue(false);
            }
        };

        categoryAdapterNotifier = new CategoryAdapterNotifier() {
            @Override
            public void notifyAboutChange(int position, boolean isActive) {
                if (isActive) {
                    if (previousSortedTaskModelList.size() != 0) {
                        sortedTaskModelList.clear();
                        sortedTaskModelList.addAll(previousSortedTaskModelList);
                    }
                    updateTaskByCategory();
                } else {
                    sortedTaskModelList.clear();
                    sortedTaskModelList.addAll(previousSortedTaskModelList);
                    updateUIWhenCategoryChanges();
                }
            }
        };

        deActivatorInterface = new TaskDeActivatorInterface() {
            @Override
            public void finishTask(TaskModel tempTaskModel) {
                tempTaskModel.setActive(false);
                mainViewModel.updateTask(tempTaskModel);
            }
        };

        taskAdapter = new TaskAdapter(context, sortedTaskModelList, mainViewModel.getBaseColorNOW(), deActivatorInterface);
        categoryAdapter = new CategoryAdapter(context, categoryModelList, categoryAdapterNotifier);
        dateAdapter = new DateScreenAdapter(context, dateModelList, onCalendarDateChangeInterface);

        mainViewModel.getBaseColor().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                appColor = integer;
                setUIColors();
                taskAdapter.notifyDataSetChanged();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);

        categoryRV.setAdapter(categoryAdapter);

        calendarRV.setLayoutManager(linearLayoutManager1);
        calendarRV.setAdapter(dateAdapter);

        todoRV.setLayoutManager(linearLayoutManager);
        todoRV.addItemDecoration(new SpaceItemDecoration(36));
        todoRV.setAdapter(taskAdapter);


        todoRV.setLeftBg(R.color.error);
        todoRV.setRightBg(R.color.confirmation);
        todoRV.setLeftImage(R.drawable.ic_delete_white);
        todoRV.setRightImage(R.drawable.ic_edit);
        todoRV.setLeftText("DELETE");
        todoRV.setRightText("EDIT");
        todoRV.setTextColor(App.getContext().getResources().getColor(R.color.accentColor));
        todoRV.setTextSize(48);


        todoRV.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {
                //EDIT
                final TaskModel model = taskAdapter.getData().get(position);
                Intent intent = new Intent(getActivity(), EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("MODEL", model);
                bundle.putSerializable("FROM", "FRAGMENT");
                intent.putExtras(bundle);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onSwipedRight(int position) {
                final TaskModel model = taskAdapter.getData().get(position);

                nameToDelete = model.getTaskName();
                taskAdapter.removeItem(position);

                isDeleted = true;

                snackbar = Snackbar.make(coordinatorLayout, "Item was deleted", Snackbar.LENGTH_LONG);

                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        taskAdapter.restoreItem(model, position);
                        todoRV.scrollToPosition(position);
                        isDeleted = false;
                    }
                });
                snackbar.setActionTextColor(appColor);
                snackbar.show();

                final Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        if (isDeleted) {
                            mainViewModel.deleteTask(nameToDelete);
                            isDeleted = false;
                        }
                    }
                };

                Handler handler = new Handler();
                handler.postDelayed(r, 2800);
            }
        });

        mainViewModel.getTaskModels().observe(getActivity(), new Observer<List<TaskModel>>() {
            @Override
            public void onChanged(List<TaskModel> taskModels) {
                taskModelList.clear();
                taskModelList.addAll(taskModels);

                if (taskModels.isEmpty()) {
                    coordinatorLayout.setVisibility(View.GONE);
                    nothingHere.setVisibility(View.VISIBLE);
                } else {
                    coordinatorLayout.setVisibility(View.VISIBLE);
                    nothingHere.setVisibility(View.GONE);
                }

                dateModelList.clear();
                List<DateModel> tempDataModelList = DateModel.createDateList(taskModelList);
                dateModelList.addAll(tempDataModelList);
                dateAdapter.notifyDataSetChanged();

                updateTasksWithDate();
            }
        });

        mainViewModel.getCategoryModels().observe(getActivity(), new Observer<List<CategoryModel>>() {
            @Override
            public void onChanged(List<CategoryModel> categoryModels) {
                if (categoryModels != null && !categoryModels.isEmpty()) {
                    categoryModelList.clear();
                    categoryModelList.addAll(categoryModels);
                    categoryAdapter.notifyDataSetChanged();
                }
            }
        });

        isUpdating.observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isUpdating) {
                if (!isUpdating)
                    updateTasksWithDate();
            }
        });
    }

    private void updateTasksWithDate() {
        previousSortedTaskModelList.clear();
        if (dateModelList.isEmpty()) {
            coordinatorLayout.setVisibility(View.GONE);
            nothingHere.setVisibility(View.VISIBLE);
        } else {
            if (coordinatorLayout.getVisibility() == View.VISIBLE) {
                //
            } else {
                coordinatorLayout.setVisibility(View.VISIBLE);
                nothingHere.setVisibility(View.GONE);
            }
            Date activeDate = dateModelList.stream()
                    .filter(DateModel::isActive)
                    .collect(Collectors.toList()).get(0).getDate();

            List<TaskModel> tempTaskModels = taskModelList.stream()
                    .filter(task -> task.getSimpleDate().getTime() == activeDate.getTime())
                    .collect(Collectors.toList());

            Collections.sort(tempTaskModels, new SortByDate());


            sortedTaskModelList.clear();
            sortedTaskModelList.addAll(tempTaskModels);
            updateTaskByCategory();
        }
    }

    private void updateTaskByCategory() {

        boolean isActive = categoryModelList.stream()
                .anyMatch(CategoryModel::isActive);

        if (isActive) {
            String categoryName = categoryModelList.stream()
                    .filter(CategoryModel::isActive)
                    .findAny()
                    .get().getCategoryName();

            List<TaskModel> tempTaskModels = new ArrayList<>();

            previousSortedTaskModelList.clear();
            previousSortedTaskModelList.addAll(sortedTaskModelList);

            sortedTaskModelList.stream()
                    .filter(task -> {
                        if (task.getModel() != null)
                            return task.getModel().getCategoryName().equals(categoryName);
                        else
                            return false;
                    })
                    .forEach(tempTaskModels::add);

            sortedTaskModelList.clear();
            sortedTaskModelList.addAll(tempTaskModels);
        }
        updateUIWhenCategoryChanges();
    }

    private void setUIColors() {
        topBackground.setBackgroundColor(appColor);
        nothingHere.setTextColor(appColor);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            searchEditText.setTextCursorDrawable(null);}*/
    }

    class SortByDate implements Comparator<TaskModel> {

        @Override
        public int compare(TaskModel taskModel, TaskModel t1) {
            return taskModel.getEndDate().compareTo(t1.getEndDate());
        }
    }

    private void updateUIWhenCategoryChanges() {

        if (sortedTaskModelList.size() == 0) {
            coordinatorLayout.setVisibility(View.GONE);
            nothingHere.setVisibility(View.VISIBLE);
        } else {
            coordinatorLayout.setVisibility(View.VISIBLE);
            nothingHere.setVisibility(View.GONE);
        }
        taskAdapter.notifyDataSetChanged();
    }


}