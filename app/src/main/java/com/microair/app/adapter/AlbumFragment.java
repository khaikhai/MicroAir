package com.microair.app.adapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import com.microair.app.repository.AlbumRepository;

public class AlbumFragment extends Fragment {
    private AlbumAdapter adapter;
    private AlbumRepository repository;

    public AlbumFragment() {
        this.repository = AlbumRepository.getInstance();
    }

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.album_fragment, container, false);
        final FrameLayout frameLayout = view.findViewById(R.id.background);
        frameLayout.setBackgroundColor(R.color.black_color);
        RecyclerView recyclerView = view.findViewById(R.id.albumList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new AlbumAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setAlbums(repository.getAllAlbum());
        return view;
    }
}
