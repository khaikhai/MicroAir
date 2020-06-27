package com.microair.app.dao;

import com.microair.app.model.Artist;

import java.util.List;

public interface ArtistDao {
    List<Artist> getAllArtist();

    Artist getArtistById(int id);

    void saveArtist(Artist artist);

    void updateArtist(Artist artist);

    void deleteArtist(Artist artist);
}
