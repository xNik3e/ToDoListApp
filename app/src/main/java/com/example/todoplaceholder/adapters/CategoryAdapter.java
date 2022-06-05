package com.example.todoplaceholder.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
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
            GradientDrawable drawable = new GradientDrawable();
            drawable.setStroke(5, model.getShadowColor());
            drawable.setCornerRadius(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()));
            drawable.setColor(model.getBaseColor());

            holder.category.setBackground(drawable);
            holder.category.setTextColor(context.getResources().getColor(R.color.accentColor));
        }else{
            holder.category.setBackground(context.getResources().getDrawable(R.drawable.background_transparent));
            holder.category.setTextColor(context.getResources().getColor(R.color.categoryColor));
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(categories.get(position).isActive()){
                        categories.get(position).setActive(false);
                    }else{
                        for(CategoryModel m : categories){
                            m.setActive(false);
                        }
                        categories.get(position).setActive(true);

                    }
                    notifyDataSetChanged();
                }
            });
        }
    }
}
