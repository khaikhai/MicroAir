package com.microair.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.microair.app.R;
import com.microair.app.model.Album;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumHolder> {
    private List<Album> albums;

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    @NonNull
    @Override
    public AlbumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
        return new AlbumHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumHolder holder, int position) {
        holder.bind(albums.get(position));
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public class AlbumHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        ImageView menu, quick_play_pause;
        RoundedImageView imageView;

        public AlbumHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            menu = itemView.findViewById(R.id.menu_button);
            quick_play_pause = itemView.findViewById(R.id.quick_play_pause);
            imageView = itemView.findViewById(R.id.image);
        }

        public void bind(Album album) {
            title.setText(album.getAlbumName());
            description.setText("asl;dkf");
        }
    }
}
