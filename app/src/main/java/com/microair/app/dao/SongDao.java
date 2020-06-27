package com.microair.app.dao;

import com.microair.app.model.Song;

import java.util.List;

public interface SongDao {
    List<Song> getAllSong();

    Song getSongById(int id);

    Song getSongByArtistId(int artistId);

    Song getSongByAlbumId(int albumId);

    void saveSong(Song song);

    void updateSong(Song song);

    void deleteSong(Song song);
}
