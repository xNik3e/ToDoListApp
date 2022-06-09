package com.example.todoplaceholder.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.todoplaceholder.FullImageActivity;
import com.example.todoplaceholder.R;
import com.example.todoplaceholder.interfaces.PhotoDeletionInterface;
import com.example.todoplaceholder.models.PhotoModels;

import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    private List<PhotoModels> photoModels;
    private Context context;
    private PhotoDeletionInterface photoDeletionInterface;

    public PhotosAdapter(Context context, List<PhotoModels> photoModels, PhotoDeletionInterface mInterface) {
        this.photoModels = photoModels;
        this.context = context;
        this.photoDeletionInterface = mInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.attach_photo_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(photoModels.get(position).getmBitmap())
                .centerCrop()
                .into(holder.attachedPhoto);
    }

    @Override
    public int getItemCount() {
        return photoModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView deletePhoto, attachedPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            deletePhoto = itemView.findViewById(R.id.delete_photo);
            attachedPhoto = itemView.findViewById(R.id.attached_photo);
            attachedPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, FullImageActivity.class);
                    intent.putExtra("FILENAME", photoModels.get(getAdapterPosition()).getFilename());
                    context.startActivity(intent);
                }
            });

            deletePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    photoDeletionInterface.deletePhoto(photoModels.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }
    }
}
