package com.bmtt.itunes.View;


import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmtt.itunes.R;
import com.bmtt.itunes.Model.Result;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlbumSearchAdapter extends RecyclerView.Adapter<AlbumSearchAdapter.AlbumSearchResultHolder> {
    private List<Result> results = new ArrayList<>();
    MainActivity main;

    @NonNull
    @Override
    public AlbumSearchResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_item, parent, false);

        return new AlbumSearchResultHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumSearchResultHolder holder, int position) {

        Result singleArtist = results.get(position);
        String artistName = "<b>" + "Artist Name:" + "</b> " + singleArtist.getArtistName();
        holder.item_artist.setText(Html.fromHtml(artistName));

        String trackName = "<b>" + "Track Name:" + "</b> " + singleArtist.getTrackName();
        holder.item_track.setText(Html.fromHtml(trackName));

        String collectName = "<b>" + "Colltn Name:" + "</b> " + singleArtist.getCollectionName();
        holder.item_collection.setText(Html.fromHtml(collectName));


        String collectPrice = "<b>" + "Colltn Price:" + "</b> " + main.getResources().getString(R.string.Rs) + String.valueOf(singleArtist.getCollectionPrice());

        holder.item_coll_price.setText(Html.fromHtml(collectPrice));

        String date_s = String.valueOf((singleArtist.getReleaseDate().substring(0, 10)));

        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dt.parse(date_s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // *** same for the format String below
        SimpleDateFormat dtf = new SimpleDateFormat("dd-MM-yyyy");


        String publishDate = "<b>" + "Release Dt :" + "</b> " + dtf.format(date);
        holder.item_publishedDate.setText(Html.fromHtml(publishDate));



        Glide.with(holder.itemView)
                .load(singleArtist.getArtworkUrl100())
                .into(holder.album_image_url);

        holder.select_album_cb.setOnCheckedChangeListener(null);
        holder.select_album_cb.setChecked(singleArtist.isChecked());

        holder.select_album_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                singleArtist.setChecked(isChecked);

            }
        });

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void setResults(List<Result> results, MainActivity main) {
        this.results = results;
        this.main = main;
        notifyDataSetChanged();
    }

    public class AlbumSearchResultHolder extends RecyclerView.ViewHolder {

        private TextView item_artist;
        private TextView item_track;
        private TextView item_collection, item_coll_price, item_publishedDate;
        private ImageView album_image_url;
        private CheckBox select_album_cb;

        public AlbumSearchResultHolder(@NonNull View itemView) {
            super(itemView);

            item_artist = itemView.findViewById(R.id.item_artist);
            item_track = itemView.findViewById(R.id.item_track);
            item_collection = itemView.findViewById(R.id.item_collection);
            item_coll_price = itemView.findViewById(R.id.item_coll_price);
            item_publishedDate = itemView.findViewById(R.id.item_publishedDate);
            album_image_url = itemView.findViewById(R.id.album_image_url);
            select_album_cb = itemView.findViewById(R.id.select_album_cb);
        }
    }

}
