package com.example.todoplaceholder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.todoplaceholder.R;
import com.example.todoplaceholder.models.CategoryModel;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    Context context;
    List<CategoryModel> models;

    public CategoryAdapter(Context context, List<CategoryModel> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int i) {
        return models.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CategoryModel model = models.get(i);
        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.category_item_view_bigpicture, viewGroup, false);
            holder.inner_circle = view.findViewById(R.id.inner_circle);
            holder.outer_circle = view.findViewById(R.id.outer_circle);
            holder.name = view.findViewById(R.id.category_name);

            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.name.setText(model.getCategoryName());
        holder.outer_circle.setBackgroundColor(model.getShadowColor());
        holder.inner_circle.setBackgroundColor(model.getBaseColor());

        return view;
    }

    private static class ViewHolder{
        View outer_circle, inner_circle;
        TextView name;
    }
}
