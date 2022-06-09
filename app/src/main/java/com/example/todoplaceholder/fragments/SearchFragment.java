package com.example.todoplaceholder.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.todoplaceholder.EditActivity;
import com.example.todoplaceholder.R;
import com.example.todoplaceholder.adapters.SearchItemAdapter;
import com.example.todoplaceholder.interfaces.SearchToEditScreenInterface;
import com.example.todoplaceholder.models.TaskModel;
import com.example.todoplaceholder.utils.Globals;
import com.example.todoplaceholder.utils.view_services.App;
import com.example.todoplaceholder.viewmodels.MainViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.BoundExtractedResult;

public class SearchFragment extends Fragment implements TextWatcher {

    private Context context;
    private MainViewModel mainViewModel;
    private int appColor;
    private TextInputLayout searchContainer;
    private TextInputEditText search;
    private SwipeableRecyclerView searchItemRV;
    private TextView nothingHere;
    private List<TaskModel> taskModelList = new ArrayList<>();
    private List<TaskModel> backupTaskModelList = new ArrayList<>();
    private SearchItemAdapter searchItemAdapter;
    private String nameToDelete;
    private boolean isDeleted;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private SearchToEditScreenInterface anInterface;


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
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);

        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        search.addTextChangedListener(this);

        mainViewModel.getBaseColor().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                appColor = integer;
                setUIColors();
            }
        });


        anInterface = new SearchToEditScreenInterface() {
            @Override
            public void navigate(TaskModel model) {
                Intent intent = new Intent(context, EditActivity.class);
                intent.putExtra("FROM", "SEARCH");
                intent.putExtra("MODELID", model.getId());
                startActivity(intent);
                getActivity().finish();
            }
        };

        searchItemAdapter = new SearchItemAdapter(context, taskModelList, anInterface);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        searchItemRV.setLayoutManager(linearLayoutManager);
        searchItemRV.setAdapter(searchItemAdapter);


        searchItemRV.setLeftBg(R.color.error);
        searchItemRV.setLeftImage(R.drawable.ic_delete_white_small);
        searchItemRV.setLeftText("DELETE");
        searchItemRV.setTextColor(App.getContext().getResources().getColor(R.color.accentColor));
        searchItemRV.setTextSize(24);

        searchItemRV.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {
                //Nothing here
            }

            @Override
            public void onSwipedRight(int position) {
                final TaskModel model = searchItemAdapter.getData().get(position);

                nameToDelete = model.getTaskName();
                searchItemAdapter.removeItem(position);

                isDeleted = true;

                snackbar = Snackbar.make(coordinatorLayout, "Item was deleted", Snackbar.LENGTH_LONG);

                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        searchItemAdapter.restoreItem(model, position);
                        searchItemRV.scrollToPosition(position);
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
                handler.postDelayed(r, 4000);
            }

        });

        mainViewModel.getTaskModels().observe(getActivity(), new Observer<List<TaskModel>>() {
            @Override
            public void onChanged(List<TaskModel> taskModels) {
                if (taskModels.isEmpty()) {
                    coordinatorLayout.setVisibility(View.GONE);
                    nothingHere.setVisibility(View.VISIBLE);
                } else {
                    coordinatorLayout.setVisibility(View.VISIBLE);
                    nothingHere.setVisibility(View.GONE);
                }


                sortTasks(taskModels);
            }
        });

    }

    private void sortTasks(List<TaskModel> models) {

        List<TaskModel> activeTasks = new ArrayList<>();
        List<TaskModel> inActiveTasks = new ArrayList<>();

        models.stream()
                .filter(TaskModel::isActive)
                .forEach(activeTasks::add);

        models.stream()
                .filter(taskModel -> !taskModel.isActive())
                .forEach(inActiveTasks::add);

        activeTasks.sort(Comparator.comparing(TaskModel::getEndDate));
        inActiveTasks.sort(Comparator.comparing(TaskModel::getEndDate));

        taskModelList.clear();
        taskModelList.addAll(activeTasks);
        taskModelList.addAll(inActiveTasks);
        backupTaskModelList.clear();
        backupTaskModelList.addAll(taskModelList);
        searchItemAdapter.notifyDataSetChanged();
    }

    private void setUIColors() {

        searchContainer.setBoxStrokeColor(appColor);
        searchContainer.setBoxStrokeColorStateList(new ColorStateList(Globals.boxStates(), Globals.boxColors(appColor)));
        searchContainer.setDefaultHintTextColor(new ColorStateList(Globals.hintStates(), Globals.hintColors(appColor)));

        nothingHere.setTextColor(appColor);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
        if(s.length() >= 2){
            List<BoundExtractedResult<TaskModel>> match = FuzzySearch.extractAll(s.toString(), backupTaskModelList, TaskModel::getTaskName, 80);
            List<Integer> indexes = new ArrayList<>();
            match.stream().map(m -> m.getIndex()).forEach(indexes::add);

            List<TaskModel> searchedModels = new ArrayList<>();

            indexes.forEach(tempI -> {
                searchedModels.add(backupTaskModelList.get(tempI));
            });
            taskModelList.clear();
            taskModelList.addAll(searchedModels);
            if(taskModelList.isEmpty()){
                coordinatorLayout.setVisibility(View.GONE);
                nothingHere.setVisibility(View.VISIBLE);
            }else{
                coordinatorLayout.setVisibility(View.VISIBLE);
                nothingHere.setVisibility(View.GONE);
            }
            searchItemAdapter.notifyDataSetChanged();
        }else{
            coordinatorLayout.setVisibility(View.VISIBLE);
            nothingHere.setVisibility(View.GONE);
            taskModelList.clear();
            taskModelList.addAll(backupTaskModelList);
            searchItemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}