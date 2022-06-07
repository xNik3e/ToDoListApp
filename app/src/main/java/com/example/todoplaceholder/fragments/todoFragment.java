package com.example.todoplaceholder.fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.todoplaceholder.R;
import com.example.todoplaceholder.adapters.CategoryAdapter;
import com.example.todoplaceholder.adapters.TaskAdapter;
import com.example.todoplaceholder.models.CategoryModel;
import com.example.todoplaceholder.models.DateModel;
import com.example.todoplaceholder.models.TaskModel;
import com.example.todoplaceholder.utils.Globals;
import com.example.todoplaceholder.utils.utils.SpaceItemDecoration;
import com.example.todoplaceholder.utils.view_services.App;
import com.example.todoplaceholder.utils.view_services.SwipeToDeleteCallback;
import com.example.todoplaceholder.viewmodels.MainViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private TextView nothingHere;
    private CoordinatorLayout coordinatorLayout;
    private Snackbar snackbar;
    private boolean isDeleted = false;
    private String nameToDelete;

    private List<CategoryModel> categoryModelList = new ArrayList<>();
    private List<TaskModel> taskModelList = new ArrayList<>();

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

        taskAdapter = new TaskAdapter(context, taskModelList, mainViewModel.getBaseColorNOW());

        mainViewModel.getBaseColor().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                appColor = integer;
                setUIColors();
                taskAdapter.notifyDataSetChanged();
            }
        });

        categoryAdapter = new CategoryAdapter(context, categoryModelList);
        categoryRV.setAdapter(categoryAdapter);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        todoRV.setLayoutManager(linearLayoutManager);
        todoRV.addItemDecoration(new SpaceItemDecoration(36));
        todoRV.setAdapter(taskAdapter);

        todoRV.setLeftBg(R.color.error);
        todoRV.setLeftImage(R.drawable.ic_delete_white);
        todoRV.setLeftText("DELETE");
        todoRV.setTextColor(App.getContext().getResources().getColor(R.color.accentColor));
        todoRV.setTextSize(48);

        todoRV.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {
                //nothing
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
                        if(isDeleted) {
                            mainViewModel.deleteTask(nameToDelete);
                            isDeleted = false;
                        }
                    }
                };

                Handler handler = new Handler();
                handler.postDelayed(r, 4000);
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
                List<DateModel> tempTestDates = DateModel.createDateList(taskModelList);
                taskAdapter.notifyDataSetChanged();
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


    }

    private void setUIColors() {
        topBackground.setBackgroundColor(appColor);
        nothingHere.setTextColor(appColor);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            searchEditText.setTextCursorDrawable(null);}*/
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(context) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
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
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(todoRV);



    }
}