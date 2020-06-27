package com.microair.app.repository;

import com.microair.app.dao.ArtistDao;
import com.microair.app.model.Artist;

import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.Sort;

public class ArtistRepository implements ArtistDao {
    private Realm mRealm;
    private static ArtistRepository INSTANCE = null;

    public ArtistRepository() {
        this.mRealm = Realm.getDefaultInstance();
    }

    public static ArtistRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ArtistRepository();
        }
        return INSTANCE;
    }

    @Override
    public List<Artist> getAllArtist() {
        return mRealm.where(Artist.class).findAll().sort("createdDate", Sort.DESCENDING);
    }

    @Override
    public Artist getArtistById(int id) {
        return mRealm.where(Artist.class).findFirst();
    }

    @Override
    public void saveArtist(Artist artist) {
        mRealm.executeTransactionAsync(realm -> {
            Number currentIdNum = realm.where(Artist.class).max("id");
            int nextId;
            if (currentIdNum == null) {
                nextId = 1;
            } else {
                nextId = currentIdNum.intValue() + 1;
            }
            artist.setId(nextId);
            realm.copyToRealmOrUpdate(artist);
        });
    }

    @Override
    public void updateArtist(Artist artist) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(artist);
            }
        });
    }

    @Override
    public void deleteArtist(Artist artist) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Objects.requireNonNull(realm.where(Artist.class).equalTo("id", artist.getId()).findFirst())
                        .deleteFromRealm();
            }
        });
    }
}
