package com.microair.app.adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.microair.app.R;
import com.microair.app.model.SendCommand;
import com.microair.app.model.Song;
import com.microair.app.repository.SongRepository;


import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongHolder> {
    private List<Song> songs;
    private SongRepository songRepository;


    public SongsAdapter() {
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feature_song, parent, false);
        return new SongHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongHolder holder, int position) {
        holder.bind(songs.get(position));
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    class SongHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, description;
        ImageView menu, quick_play_pause;
        RoundedImageView imageView;


        public SongHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            menu = itemView.findViewById(R.id.menu_button);
            quick_play_pause = itemView.findViewById(R.id.quick_play_pause);
            imageView = itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "click", Toast.LENGTH_LONG).show();
//            notifyItemChanged(getAdapterPosition());
            EventBus.getDefault().post(new SendCommand(15, songs.get(getAdapterPosition()).getPath()));
            menu.setImageResource(R.drawable.ic_playlist_add_check);
            /*final Handler handler = new Handler();
            handler.postDelayed(() -> {
                Handler handler1 = new Handler();
                handler1.postDelayed(() -> {
                    notifyItemChanged(getAdapterPosition());
                }, 50);
            }, 100);*/
        }

        public void bind(Song song) {
            title.setText(song.getTitle());
            description.setText(song.artists.get(0).name);
        }


    }
}
