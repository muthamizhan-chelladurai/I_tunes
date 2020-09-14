package com.bmtt.itunes.View;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmtt.itunes.R;
import com.bmtt.itunes.Model.Result;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AlbumSearchAdapter extends RecyclerView.Adapter<AlbumSearchAdapter.AlbumSearchResultHolder> {
    private List<Result> results = new ArrayList<>();

    @NonNull
    @Override
    public AlbumSearchResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_item, parent, false);

        return new AlbumSearchResultHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumSearchResultHolder holder, int position) {

        holder.item_artist.setText(results.get(position).getArtistName());
        holder.item_track.setText(results.get(position).getTrackName());
        holder.item_collection.setText(results.get(position).getCollectionName());
        holder.item_coll_price.setText(String.valueOf(results.get(position).getCollectionPrice()));
        holder.item_publishedDate.setText(results.get(position).getReleaseDate().substring(0, 10));
        Glide.with(holder.itemView)
                .load(results.get(position).getArtworkUrl100())
                .into(holder.album_image_url);

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void setResults(List<Result> results) {
        this.results = results;
        notifyDataSetChanged();
    }

    public class AlbumSearchResultHolder extends RecyclerView.ViewHolder {

        private TextView item_artist;
        private TextView item_track;
        private TextView item_collection, item_coll_price, item_publishedDate;
        private ImageView album_image_url;

        public AlbumSearchResultHolder(@NonNull View itemView) {
            super(itemView);

            item_artist = itemView.findViewById(R.id.item_artist);
            item_track = itemView.findViewById(R.id.item_track);
            item_collection = itemView.findViewById(R.id.item_collection);
            item_coll_price = itemView.findViewById(R.id.item_coll_price);
            item_publishedDate = itemView.findViewById(R.id.item_publishedDate);
            album_image_url = itemView.findViewById(R.id.album_image_url);
        }
    }

}
