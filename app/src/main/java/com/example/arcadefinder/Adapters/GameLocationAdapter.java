package com.example.arcadefinder.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcadefinder.Activities.RequestDetailsActivity;
import com.example.arcadefinder.Models.GameLocationModel;
import com.example.arcadefinder.R;
import com.example.arcadefinder.RoomGameLocation;

import org.parceler.Parcels;

import java.util.List;

public class GameLocationAdapter extends RecyclerView.Adapter<GameLocationAdapter.ViewHolder> {

    Context context;
    List<GameLocationModel> requests;

    public GameLocationAdapter(Context context, List<GameLocationModel> posts) {
        this.context = context;
        this.requests = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GameLocationModel post = requests.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    /**
     * For refreshing
     */
    public void clear() {
        requests.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvGameTitle;
        TextView tvAddress;
        TextView tvDescription;
        ImageView ivGameImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGameTitle = itemView.findViewById(R.id.tvGameTitle);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivGameImage = itemView.findViewById(R.id.ivGameImage);
            itemView.setOnClickListener(this);
        }

        public void bind(GameLocationModel request) {
            tvGameTitle.setText(request.getTitle());
            String addressText = request.getLocationName() + "\n" + request.getAddress();
            tvAddress.setText(addressText);
            tvDescription.setText(request.getDescription());
            Bitmap image = request.getImage();
            if (image != null) {
                ivGameImage.setImageBitmap(image);
            }
        }

        @Override
        public void onClick(View v) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the request at the position, this won't work if the class is static
                GameLocationModel request = requests.get(position);
                // create intent for the new activity
                Intent i = new Intent(context, RequestDetailsActivity.class);
                // serialize the request using parceler, use its short name as a key
                i.putExtra(GameLocationModel.class.getSimpleName(), Parcels.wrap(request));
                // show the activity
                context.startActivity(i);
            }
        }
    }
}

