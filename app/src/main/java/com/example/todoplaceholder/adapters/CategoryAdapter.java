package com.example.todoplaceholder.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoplaceholder.R;
import com.example.todoplaceholder.models.CategoryModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context context;
    private List<CategoryModel> categories;

    public CategoryAdapter(Context context, List<CategoryModel> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryModel model = categories.get(position);
        if(model.isActive()){
            Drawable drawable = context.getResources().getDrawable(R.drawable.background_transparent);
            Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(wrappedDrawable, model.getBaseColor());
            holder.category.setBackground(drawable);

            GradientDrawable gd = (GradientDrawable) holder.category.getBackground();
            gd.setStroke(2, model.getShadowColor());

            holder.category.setTextColor(context.getResources().getColor(R.color.accentColor));
        }else{
            holder.category.setBackground(context.getResources().getDrawable(R.drawable.background_transparent));
        }
        holder.category.setText(model.getCategoryName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            category = itemView.findViewById(R.id.category_name);
        }
    }
}
