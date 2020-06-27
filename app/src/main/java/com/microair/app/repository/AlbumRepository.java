package com.microair.app.repository;

import com.microair.app.dao.AlbumDao;
import com.microair.app.model.Album;

import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.Sort;

public class AlbumRepository implements AlbumDao {
    private static AlbumRepository INSTANCE = null;
    private Realm mRealm;

    public AlbumRepository() {
        this.mRealm = Realm.getDefaultInstance();
    }

    public static AlbumRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AlbumRepository();
        }
        return INSTANCE;
    }

    @Override
    public List<Album> getAllAlbum() {
        return mRealm.where(Album.class).findAll().sort("createDate", Sort.DESCENDING);
    }

    @Override
    public Album getAlbumById(int id) {
        return mRealm.where(Album.class).equalTo("id", id).findFirst();
    }

    @Override
    public void saveAlbum(Album album) {
        mRealm.executeTransactionAsync(realm -> {
            Number currentIdNum = realm.where(Album.class).max("id");
            int nextId;
            if (currentIdNum == null) {
                nextId = 1;
            } else {
                nextId = currentIdNum.intValue() + 1;
            }
            album.setId(nextId);
            realm.copyToRealmOrUpdate(album);
        });
    }

    @Override
    public void updateAlbum(Album album) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(album);
            }
        });
    }

    @Override
    public void deleteAlbum(Album album) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Objects.requireNonNull(realm.where(Album.class).equalTo("id", album.getId())
                        .findFirst())
                        .deleteFromRealm();
            }
        });
    }
}
