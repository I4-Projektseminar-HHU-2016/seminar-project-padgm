package com.example.paul.hashtagworldmap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by paul on 12.09.16.
 */
public class StartActivity extends Activity {


    private android.widget.Button signInButton;
    private android.widget.SeekBar seekBar;
    private android.widget.TextView textView;
    private android.widget.ImageButton exit;
    private android.widget.EditText cityText;
    private android.widget.Button checkButton;

    private double latitude;
    private double longitude;
    private static int distance;
    private DownloadTask data1;
    private ArrayList<Data> infoList = new ArrayList<>();
    private String city;
    private boolean checkButtonOk = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);

        checkButton = (android.widget.Button) findViewById(R.id.checkButton);
        cityText = (android.widget.EditText) findViewById(R.id.cityText);
        signInButton = (android.widget.Button) findViewById(R.id.button);
        textView = (android.widget.TextView) findViewById(R.id.textView);
        seekBar = (android.widget.SeekBar) findViewById(R.id.seekBar);
        seekBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);
        exit = (android.widget.ImageButton) findViewById(R.id.exit);

        cityText.setOnClickListener(new SearchView.OnClickListener() {
            @Override
            public void onClick(View v) {
                city = cityText.getText().toString();
            }
        });

        checkButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                search();
            }
        });


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
                startMap();
            }
        });
    }

    public void startLocActivity(){

        Intent locActivity = new Intent(this, LocationActivity.class);
        startActivity(locActivity);

    }

    public void startMap(){

        Intent mapsActivity = new Intent(this, MapsActivity.class);
        startActivity(mapsActivity);

    }

    public static int getDistance(){
        return distance;
    }


    public void search() {
            city = cityText.getText().toString();
        System.out.println("CITY: " + city);
            CurrentLocation.setCurLoc(city);
            startMap();
    }

}
