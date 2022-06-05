package com.example.todoplaceholder.adapters;

import android.content.Context;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoplaceholder.R;
import com.example.todoplaceholder.models.DateModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class DateScreenAdapter extends RecyclerView.Adapter<DateScreenAdapter.ViewHolder> {

    private Context context;
    private List<DateModel> dateModels;

    public DateScreenAdapter(Context context, List<DateModel> dateModels) {
        this.context = context;
        this.dateModels = dateModels;
    }

    @NonNull
    @Override
    public DateScreenAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.calendar_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateScreenAdapter.ViewHolder holder, int position) {
        DateModel dateModel = dateModels.get(position);

        SimpleDateFormat onlyDay =new SimpleDateFormat("dd/MM");
        SimpleDateFormat dayString = new SimpleDateFormat("EEE");

        holder.date.setText(onlyDay.format(dateModel.getDate()));
        holder.day.setText(dayString.format(dateModel.getDate()));

        if(dateModel.isActive()){
            holder.container.setBackground(context.getResources().getDrawable(R.drawable.calendar_card_active));
        }else{
            holder.container.setBackground(context.getResources().getDrawable(R.drawable.calendar_card));
        }
    }

    @Override
    public int getItemCount() {
        return dateModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView date, day;
        ConstraintLayout container;

        public ViewHolder(@NonNull View v) {
            super(v);

            date = v.findViewById(R.id.date);
            day = v.findViewById(R.id.day);
            container = v.findViewById(R.id.container);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if(dateModels.get(position).isActive()){
                        dateModels.get(position).setActive(false);
                    }else{
                        for(DateModel m: dateModels){
                            m.setActive(false);
                        }
                        dateModels.get(position).setActive(true);
                    }
                    notifyDataSetChanged();
                }
            });

        }
    }
}
