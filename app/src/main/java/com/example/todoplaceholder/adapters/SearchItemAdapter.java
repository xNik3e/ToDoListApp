package com.example.todoplaceholder.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoplaceholder.R;
import com.example.todoplaceholder.models.TaskModel;
import com.example.todoplaceholder.utils.view_services.AlwaysMarqueeTextView;

import java.util.List;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.ViewHolder> {

    private Context context;
    private List<TaskModel> modelList;

    public SearchItemAdapter(Context context, List<TaskModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaskModel task = modelList.get(position);

        if(task.getModel() == null){
            holder.container.setVisibility(View.INVISIBLE);
        }else{
            holder.container.setVisibility(View.VISIBLE);
            holder.circle.setBackgroundColor(task.getModel().getBaseColor());
        }
        holder.taskName.setText(task.getTaskName());
        if(!task.isActive()){
            holder.taskName.setTextColor(context.getResources().getColor(R.color.categoryColor));
            holder.taskName.setPaintFlags(holder.taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            holder.taskName.setTextColor(context.getResources().getColor(R.color.accentColor));
            holder.taskName.setPaintFlags(holder.taskName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

    }

    public List<TaskModel> getData(){
        return modelList;
    }

    public void removeItem(int position) {
        modelList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(TaskModel model, int position) {
        modelList.add(position, model);
        notifyItemInserted(position);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView container;
        View circle;
        AlwaysMarqueeTextView taskName;

        public ViewHolder(@NonNull View view) {
            super(view);
            container = view.findViewById(R.id.container);
            circle = view.findViewById(R.id.category_indicator);
            taskName = view.findViewById(R.id.category_title);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Start an edit activity with specific task id
                }
            });
        }
    }
}
