package com.example.paul.hashtagworldmap;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by paul on 12.09.16.
 * Erste Activity, die sich aufzeigt
 */
public class StartActivity extends Activity {


    private android.widget.Button signInButton;
    private android.widget.SeekBar seekBar;
    private android.widget.TextView textView;
    private android.widget.TextView textViewCount;
    private android.widget.ImageButton exit;
    private android.widget.EditText cityText;
    private android.widget.Button checkButton;
    private android.widget.SeekBar seekBarCount;

    private double latitude;
    private double longitude;
    private static int distance;
    private DownloadTask data1;
    private ArrayList<Data> infoList = new ArrayList<>();
    private String city;
    private boolean checkButtonOk = false;
    public boolean toastShowed = true;
    public static int count;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);

        //Toast sollte nur angezeigt werden wenn es vorher noch nicht der Fall war
        if(toastShowed) {
            Toast.makeText(this, "Bitte aktiviere Internet und GPS, ansonsten wird die App abstürzen oder nicht korrekt ausgeführt.", Toast.LENGTH_LONG).show();
            toastShowed = false;
        }

        //  ------ XML ELMENTE -------

        //Initialisierung aller in start_activity.xml aufgeführten Elemente, die benutzt bzw. verändert werden müssen
        checkButton = (android.widget.Button) findViewById(R.id.checkButton);
        cityText = (android.widget.EditText) findViewById(R.id.cityText);
        signInButton = (android.widget.Button) findViewById(R.id.button);
        textView = (android.widget.TextView) findViewById(R.id.textView);
        textViewCount = (android.widget.TextView) findViewById(R.id.textViewCount);
        seekBar = (android.widget.SeekBar) findViewById(R.id.seekBar);
        seekBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);
        exit = (android.widget.ImageButton) findViewById(R.id.exit);
        seekBarCount = (android.widget.SeekBar) findViewById(R.id.seekBarCount);



        //  ------ APP BEENDEN -------

        //zum Beenden der gesamten App (vom Button der MapActivity)
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        //  ------ LISTENER -------

        //weißt eingegebenen Ort Instanzvariable city zu.
        cityText.setOnClickListener(new SearchView.OnClickListener() {
                @Override
                public void onClick(View v) {city = cityText.getText().toString();
                }
            });

        //prüft Instanzvariable und öffnet search();
        checkButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(cityText.getText().length() != 0) {
                    search();
                } else {
                    Toast toast = Toast.makeText(StartActivity.this, "Bitte gib einen Ort ein.", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        //nimmt den Radius entgegen
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int change = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                seekBar.setMax(750);
                this.change = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                distance = this.change;
                String value = String.valueOf(this.change);
                textView.setText(value + " m");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                distance = change;
                String value = String.valueOf(this.change);
                textView.setText(value + " m");

            }
        });


        //nimmt Anzahl entgegen
        seekBarCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int change = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                seekBarCount.setMax(50);
                this.change = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                count = this.change;
                String value = String.valueOf(this.change);
                textViewCount.setText(value);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                count = change;
                String value = String.valueOf(this.change);
                textViewCount.setText(value);

            }
        });



        //startet die LocationActivity
        signInButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                    startLocActivity();
            }
        });


        //übergeht startLocActivity(); und öffnet direkt die MapActivity und setzt die CurrentLocation auf Düsseldorf
        exit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentLocation.setCurLoc("Düsseldorf");
                startMap();
            }
        });
    }

    //  ------ WEITERE METHODEN -------
    //startet bei Aufruf LocationActivity zur Ermittlung des Standorts
    public void startLocActivity(){

        Intent locActivity = new Intent(this, LocationActivity.class);
        startActivity(locActivity);

    }


    //startet MapsActivity
    public void startMap(){

        Intent mapsActivity = new Intent(this, MapsActivity.class);
        startActivity(mapsActivity);

    }

    //gibt die Distance aus der seekBar zurück, die in der DownloadTask benötigt wird
    public static int getDistance(){
        return distance;
    }

    //gibt die Anzahl aus der seekBarCount zurück, die in der DownloadTask benötigt wird
    public static int getCount() { return count;}

    //startet die Map und setzt vorher die CurrentLocation auf den gesuchten Begriff von cityText
    public void search() {
            city = cityText.getText().toString();
            CurrentLocation.setCurLoc(city);
            startMap();
    }

}
