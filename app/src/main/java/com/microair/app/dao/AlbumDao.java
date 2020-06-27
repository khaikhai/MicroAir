package com.microair.app.dao;

import com.microair.app.model.Album;

import java.util.List;

public interface AlbumDao {
    List<Album> getAllAlbum();

    Album getAlbumById(int id);

    void saveAlbum(Album album);

    void updateAlbum(Album album);

    void deleteAlbum(Album album);
}
