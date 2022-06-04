package com.example.todoplaceholder.adapters;



import android.content.Context;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoplaceholder.R;
import com.example.todoplaceholder.models.ColorModel;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {

    private Context context;
    private List<ColorModel> modelList;
    private ChangeSelectionInterface selectionInterface;

    public ColorAdapter(Context context, List<ColorModel> modelList) {
        this.context = context;
        this.modelList = modelList;
        selectionInterface = (ChangeSelectionInterface) context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.color_circle_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ColorModel model = modelList.get(position);
        holder.colorCircle.setBackgroundColor(model.getBaseColor());
        if(model.isActive())
            holder.selectionIndicator.setVisibility(View.VISIBLE);
        else
            holder.selectionIndicator.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class
    ViewHolder extends RecyclerView.ViewHolder {

        CardView selectionIndicator;
        View colorCircle;

        public ViewHolder(@NonNull View v) {
            super(v);
            selectionIndicator = v.findViewById(R.id.selection_indicator);
            colorCircle = v.findViewById(R.id.color_circle);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectionInterface.invertSelection(getAdapterPosition());
                }
            });
        }
    }

    public interface ChangeSelectionInterface{
        void invertSelection(int position);
    }
}
