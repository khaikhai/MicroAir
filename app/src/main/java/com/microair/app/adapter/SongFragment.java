package com.microair.app.adapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.microair.app.R;
import com.microair.app.model.VersionModel;
import com.microair.app.repository.SongRepository;

import java.util.Arrays;
import java.util.List;

public class SongFragment extends Fragment {
    int color;
    SongsAdapter adapter;
    private SongRepository songRepository;

    public SongFragment() {
        this.songRepository = SongRepository.getInstance();
    }


    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.songs_fragment, container, false);

        final FrameLayout frameLayout = view.findViewById(R.id.background);
        frameLayout.setBackgroundColor(R.color.black_color);
        RecyclerView recyclerView = view.findViewById(R.id.songsList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        List<String> list = Arrays.asList(VersionModel.data);
        Log.d("Song Fragment", "onCreateView: " + songRepository.getAllSong().get(0));
        adapter = new SongsAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setSongs(songRepository.getAllSong());
        return view;
    }
}
