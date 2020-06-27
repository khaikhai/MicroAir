package com.microair.app;

import android.app.Application;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.microair.app.model.SendCommand;
import com.microair.app.model.Song;
import com.microair.app.model.TestSendData;
import com.microair.app.service.FileDownloadClient;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.core.utils.SLog;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.action.SocketActionAdapter;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;

public class MicroAir extends Application {
    private File EXPORT_REALM_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    private String EXPORT_REALM_FILE_NAME = "update.realm";
    private String IMPORT_REALM_FILE_NAME = "song.realm"; // Eventually replace this if you're using a custom db name
    private Realm realm;
    private ConnectionInfo info;


    @Override
    public void onCreate() {
        super.onCreate();
        this.info = new ConnectionInfo("192.168.100.103", 8081);
        downloda();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("song.realm").build();
//        Realm.deleteRealm(config);

        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
        EventBus.getDefault().register(this);
/*        RealmAsyncTask realmAsyncTask = realm.executeTransactionAsync(realm -> {
            Number songId = realm.where(Song.class).max("id");
            Number artistId = realm.where(Artist.class).max("id");
            Number albumId = realm.where(Album.class).max("id");
            int nextSongId;
            int nextArtistId;
            int nextAlbumId;
            if (songId == null && artistId == null && albumId == null) {
                nextSongId = 1;
                nextArtistId = 1;
                nextAlbumId = 1;
            } else {
                nextSongId = songId.intValue() + 1;
                nextArtistId = artistId.intValue() + 1;
                nextAlbumId = albumId.intValue() + 1;
            }
            Artist artist = realm.createObject(Artist.class, nextArtistId);
            artist.setName("Myo Gyi");
            artist.setPlayCount(0);
            artist.setCreatedDate(new Date());

            Album album = realm.createObject(Album.class, nextAlbumId);
            album.setAlbumName("You Like");
            album.setPlayCount(0);
            album.setCreateDate(new Date());

            Song song = realm.createObject(Song.class, nextSongId);
            song.setAlbum(album);
            song.artists.add(artist);
            song.setTitle("Take It");
            song.setNumber(Formant.decimal(nextSongId));
            song.setPlayCount(0);
            song.setPath("asdlflaskdflkas");
            song.setCreatedDate(new Date());

        }, () -> Log.d("Song:", "song data insert Success"), error -> Log.d("Song:", "data insert error" + error.getMessage()));*/
//        fectchData();
        parePreClient();
    }

    public void fectchData() {
        final RealmResults<Song> realmResults = realm.where(Song.class).findAll();
        System.out.println(realmResults.asJSON());
        Log.i("Realm", realm.getPath());

    }

    private void downloda() {
        Log.d("Dowmload", "downloda: start download");
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("http://192.168.43.141:8080");
        Retrofit retrofit = builder.build();
        FileDownloadClient client = retrofit.create(FileDownloadClient.class);
        Call<ResponseBody> call = client.downloadFile();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                boolean succ = writeResponseBodyToDisk(response.body());
                if (succ) {
                    restore();
                }
                Log.d("download", "onResponse: write " + succ);
                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("fail", "onFailure: " + t.getMessage()
                );
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + File.separator + "update.realm");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    private String copyBundledRealmFile(String oldFilePath, String outFileName) {
        try {
            File file = new File(getApplicationContext().getFilesDir(), outFileName);

            FileOutputStream outputStream = new FileOutputStream(file);

            FileInputStream inputStream = new FileInputStream(new File(oldFilePath + outFileName));

            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            outputStream.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void restore() {
        //Restore
        String restoreFilePath = EXPORT_REALM_PATH + "/" + EXPORT_REALM_FILE_NAME;

        Log.d(TAG, "oldFilePath = " + restoreFilePath);

        copyBundledRealmFile(getExternalFilesDir(null) + File.separator, IMPORT_REALM_FILE_NAME);
        Log.d(TAG, "Data restore is done");
    }

    public void parePreClient() {
        SLog.setIsDebug(true);
        //Connection parameter Settings (IP, port number), which is also a unique identifier for a connection.
//        final ConnectionInfo info = new ConnectionInfo("192.168.43.141", 8081);
        //Call OkSocket open() the channel for this connection, and the physical connections will be connected.
        final IConnectionManager manager = OkSocket.open(info).send(new TestSendData(11, ""));
        manager.registerReceiver(new SocketActionAdapter() {
            @Override
            public void onSocketConnectionSuccess(ConnectionInfo info, String action) {
                Log.d(TAG, "onSocketConnectionSuccess: success ok socket");

            }

            @Override
            public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
                super.onSocketReadResponse(info, action, data);
                System.out.println("asdfa;sdf" + Arrays.toString(data.getBodyBytes()));
            }
        });
        manager.connect();

        OkSocket.open(info).send(new TestSendData(12, ""));


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendCommand(SendCommand command) {

        Log.d(TAG, "sendCommand: " + command.getData());
        OkSocket.open(info).send(new TestSendData(command.getCmd(), command.getData()));
    }

}
