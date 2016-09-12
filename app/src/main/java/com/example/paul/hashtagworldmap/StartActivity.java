package com.example.paul.hashtagworldmap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import java.util.ArrayList;

/**
 * Created by paul on 12.09.16.
 */
public class StartActivity extends Activity {


    private android.widget.ProgressBar progressBar;
    private android.widget.Button signInButton;
    private android.widget.SeekBar seekBar;
    private android.widget.TextView textView;
    private android.widget.ImageButton exit;

    private double latitude;
    private double longitude;
    private int distance;
    private DownloadTask data1;
    private ArrayList<Data> infoList = new ArrayList<>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);


        progressBar = (android.widget.ProgressBar) findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.GRAY, android.graphics.PorterDuff.Mode.MULTIPLY);
        signInButton = (android.widget.Button) findViewById(R.id.button);
        textView = (android.widget.TextView) findViewById(R.id.textView);
        seekBar = (android.widget.SeekBar) findViewById(R.id.seekBar);
        exit = (android.widget.ImageButton) findViewById(R.id.exit);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int change = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText("Radius");
                seekBar.setMax(750);
                this.change = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                String value = String.valueOf(this.change);
                textView.setText(value + " m");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String value = String.valueOf(this.change);
                textView.setText(value + " m");
                distance = change;
            }
        });

        signInButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                startLocActivity();
            }
        });


        exit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void startLocActivity(){

        Intent locActivity = new Intent(this, LocationActivity.class);
        startActivity(locActivity);

    }
}
