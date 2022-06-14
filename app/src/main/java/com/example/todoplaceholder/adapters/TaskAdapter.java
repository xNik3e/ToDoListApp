package com.example.todoplaceholder.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoplaceholder.R;
import com.example.todoplaceholder.interfaces.TaskDeActivatorInterface;
import com.example.todoplaceholder.models.TaskModel;
import com.example.todoplaceholder.utils.view_services.AlwaysMarqueeTextView;

import java.text.SimpleDateFormat;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private Context context;
    private List<TaskModel> taskModels;
    private int UIColor;
    private TaskDeActivatorInterface deActivatorInterface;

    public TaskAdapter(Context context, List<TaskModel> taskModels, int UIColor, TaskDeActivatorInterface deActivatorInterface) {
        this.context = context;
        this.taskModels = taskModels;
        this.UIColor = UIColor;
        this.deActivatorInterface = deActivatorInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.todo_item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TaskModel model = taskModels.get(position);
        holder.title.setText(model.getTaskName());
        holder.description.setText(model.getDescription());
        holder.title.setTextColor(UIColor);
        holder.categoryIndicator.setBackgroundTintList(ColorStateList.valueOf(UIColor));
        holder.buttonCheck.setCardBackgroundColor(UIColor);

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM dd");
        SimpleDateFormat esdf = new SimpleDateFormat("EEE, MMM dd HH:mm");

        holder.creationDate.setText("Created at: " + sdf.format(model.getCreatedAt()));
        holder.expirationDate.setText("Expiring at: " + esdf.format(model.getEndDate()));

        if (model.getNotificationTime() != null) {
            holder.alarmImage.setVisibility(View.VISIBLE);
        } else {
            holder.alarmImage.setVisibility(View.GONE);
        }

        if (model.getAttachedFileNames() != null && !model.getAttachedFileNames().isEmpty()) {
            holder.attachmentImage.setVisibility(View.VISIBLE);
        } else {
            holder.attachmentImage.setVisibility(View.GONE);
        }

        if (model.isActive()) {
            holder.buttonCheck.setVisibility(View.VISIBLE);
            holder.doneMessage.setVisibility(View.GONE);
        } else {
            holder.buttonCheck.setVisibility(View.GONE);
            holder.doneMessage.setVisibility(View.VISIBLE);
        }

        if (model.getModel() != null) {
            int color = model.getModel().getBaseColor();
            holder.title.setTextColor(color);
            holder.categoryIndicator.setBackgroundTintList(ColorStateList.valueOf(color));
            holder.buttonCheck.setCardBackgroundColor(color);
        }


    }

    public void removeItem(int position) {
        taskModels.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(TaskModel model, int position) {
        taskModels.add(position, model);
        notifyItemInserted(position);
    }

    public List<TaskModel> getData() {
        return taskModels;
    }

    @Override
    public int getItemCount() {
        return taskModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View categoryIndicator;
        TextView title, creationDate, doneMessage, expirationDate;
        ImageView alarmImage, attachmentImage;
        AlwaysMarqueeTextView description;
        CardView buttonCheck;

        public ViewHolder(@NonNull View view) {
            super(view);

            categoryIndicator = view.findViewById(R.id.category_indicator);
            title = view.findViewById(R.id.title);
            creationDate = view.findViewById(R.id.creationDate);
            doneMessage = view.findViewById(R.id.done_message);
            alarmImage = view.findViewById(R.id.alarm_image_view);
            description = view.findViewById(R.id.description);
            buttonCheck = view.findViewById(R.id.button_check);
            expirationDate = view.findViewById(R.id.expirationDate);
            attachmentImage = view.findViewById(R.id.attachments_image_view);

            buttonCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    taskModels.get(getAdapterPosition()).setActive(false);
                    notifyItemChanged(getAdapterPosition());
                    deActivatorInterface.finishTask(taskModels.get(getAdapterPosition()));
                }
            });

        }
    }
}
