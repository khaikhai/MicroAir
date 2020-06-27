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
import com.microair.app.repository.ArtistRepository;

public class ArtistFragment extends Fragment {
    private ArtistAdapter adapter;
    private ArtistRepository repository;

    public ArtistFragment() {
        this.repository = ArtistRepository.getInstance();
    }

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.artists_fragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.artistList);
        final FrameLayout frameLayout = view.findViewById(R.id.background);
        frameLayout.setBackgroundColor(R.color.black_color);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new ArtistAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setArtist(repository.getAllArtist());
        return view;
    }
}
