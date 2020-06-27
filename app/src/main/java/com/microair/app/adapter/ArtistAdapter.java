package com.microair.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.microair.app.R;
import com.microair.app.model.Artist;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistHolder> {
    private List<Artist> artists;

    public void setArtist(List<Artist> artists) {
        this.artists = artists;

    }

    @NonNull
    @Override
    public ArtistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist_child, parent, false);
        return new ArtistHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistHolder holder, int position) {
        holder.bind(artists.get(position));
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public class ArtistHolder extends RecyclerView.ViewHolder {
        TextView mArtist;
        ImageView mImage;
        TextView mGenreOne;
        TextView mGenreTwo;
        View mPanel;
        View mPanelColor;
        View mRoot;
        TextView mCount;

        public ArtistHolder(@NonNull View itemView) {
            super(itemView);
            mArtist = itemView.findViewById(R.id.title);
            mCount = itemView.findViewById(R.id.count);
        }

        public void bind(Artist artist) {
            mArtist.setText(artist.getName());
            mCount.setText("20");
        }
    }
}
