package com.microair.app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.microair.app.model.SendCommand;
import com.microair.app.model.TestSendData;
import com.microair.app.ui.TabAnimationActivity;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.core.utils.SLog;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.action.SocketActionAdapter;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button play, pause;
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout linearLayout;
    private ToggleButton tgbtn;
    private ListView listView;
    private TextView txtCantant, textView;
    private ContentLoadingProgressBar progressBar;
    private SeekBar progressSlider;
    private ImageButton playPauseButton, previousButton, nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        rellenarListView();

        tgbtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View view, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    tgbtn.setChecked(true);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    tgbtn.setChecked(false);
                }
            }

            @Override
            public void onSlide(View view, float v) {

            }
        });
    }

    private void init() {
        this.linearLayout = findViewById(R.id.bottomSheet);
        this.sheetBehavior = BottomSheetBehavior.from(linearLayout);
        this.tgbtn = findViewById(R.id.toggleButton);
        this.progressSlider = findViewById(R.id.progressSlider);
//        this.listView = findViewById(R.id.listView);
        this.txtCantant = findViewById(R.id.txtCantante);
        this.textView = findViewById(R.id.txtCancion);
        this.progressBar = findViewById(R.id.progbar);
        Intent intent = new Intent(MainActivity.this, TabAnimationActivity.class);
        startActivity(intent);
        playPauseButton = findViewById(R.id.playPauseButton);
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        mediaControl();
    }

    private void rellenarListView() {
        String[] nombre = {"50 Cent", "50 Cent", "50 Cent", "50 Cent", "50 Cent", "50 Cent", "50 Cent", "50 Cent"};
        String[] correo = {"Many Men", "Window Shopper",
                "Candy Shop", "Just a lil bit", "I'm the man", "P.I.M.P", "Wanksta",
                "Ayo technology"};


        ArrayList<Map<String, Object>> lista = new ArrayList<>();

        int nombresLen = nombre.length;

        for (int i = 0; i < nombresLen; i++) {
            Map<String, Object> listItem = new HashMap<>();
            listItem.put("Cantante", nombre[i]);
            listItem.put("Titulo", correo[i]);

            lista.add(listItem);
        }

//        this.listView.setAdapter(getAdapterListViewCT(lista));

//        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView txtCantanteLV = view.findViewById(android.R.id.text1);
//                TextView txtCancionLV = view.findViewById(android.R.id.text2);
//
//                txtCantant.setText(txtCantanteLV.getText());
//                textView.setText(txtCancionLV.getText());
//
//                progressBar.setProgress(getRandom());
//            }
//        });


    }

    private SimpleAdapter getAdapterListViewCT(ArrayList<Map<String, Object>> lista) {
        return new SimpleAdapter(this, lista,
                android.R.layout.simple_list_item_2, new String[]{"Cantante", "Titulo"},
                new int[]{android.R.id.text1, android.R.id.text2}) {

            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView txtNombre = view.findViewById(android.R.id.text1);
                txtNombre.setTypeface(Typeface.DEFAULT_BOLD);

                TextView txtCorreo = view.findViewById(android.R.id.text2);
                txtCorreo.setTextColor(Color.DKGRAY);

                return view;
            }

        };
    }

    private int getRandom() {
        return (int) Math.floor(Math.random() * 100);
    }

    public void parePreClient() {
        SLog.setIsDebug(true);
        //Connection parameter Settings (IP, port number), which is also a unique identifier for a connection.
        final ConnectionInfo info = new ConnectionInfo("192.168.43.141", 8080);
        //Call OkSocket open() the channel for this connection, and the physical connections will be connected.
        final IConnectionManager manager = OkSocket.open(info).send(new TestSendData(11, ""));
        manager.registerReceiver(new SocketActionAdapter() {
            @Override
            public void onSocketConnectionSuccess(ConnectionInfo info, String action) {


            }

            @Override
            public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
                super.onSocketReadResponse(info, action, data);
                System.out.println("asdfa;sdf" + Arrays.toString(data.getBodyBytes()));
            }
        });
        manager.connect();
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkSocket.open(info).send(new TestSendData(12, ""));
            }
        });

    }

    public void mediaControl() {
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Click", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new SendCommand(11, ""));
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Click", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new SendCommand(12, ""));
            }
        });
        previousButton.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Click", Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(new SendCommand(13, ""));
        });
        progressSlider.setMax(15);
        progressSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int lastSeekbarVal = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("TAG", "onProgressChanged: " + progress);
                if (progress > lastSeekbarVal) {
                    EventBus.getDefault().post(new SendCommand(16, String.valueOf(progress)));
                } else if (progress < lastSeekbarVal) {
                    EventBus.getDefault().post(new SendCommand(17, String.valueOf(progress)));
                }
                lastSeekbarVal = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
