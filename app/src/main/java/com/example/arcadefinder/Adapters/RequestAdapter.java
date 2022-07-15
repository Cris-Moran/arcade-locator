package com.example.arcadefinder.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.arcadefinder.Activities.RequestDetailsActivity;
import com.example.arcadefinder.ParseGameLocation;
import com.example.arcadefinder.R;
import com.parse.ParseFile;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    Context context;
    List<ParseGameLocation> requests;

    public RequestAdapter(Context context, List<ParseGameLocation> posts) {
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
        ParseGameLocation post = requests.get(position);
        // TODO: Not sure what this does..
//        holder.ivGameImage.layout(0, 0, 0, 0);
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

        public void bind(ParseGameLocation request) {
            tvGameTitle.setText(request.getTitle());
            String addressText = request.getLocationName() + "\n" + request.getAddress();
            tvAddress.setText(addressText);
            tvDescription.setText(request.getDescription());
            ParseFile image = request.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivGameImage);
                ivGameImage.setMinimumHeight(ivGameImage.getMeasuredHeight());
            }
        }

        @Override
        public void onClick(View v) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the request at the position, this won't work if the class is static
                ParseGameLocation request = requests.get(position);
                // create intent for the new activity
                Intent i = new Intent(context, RequestDetailsActivity.class);
                // serialize the request using parceler, use its short name as a key
                i.putExtra("PARSE_OBJECT_EXTRA", request);
                // show the activity
                context.startActivity(i);
            }
        }
    }
}
