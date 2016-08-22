package com.example.paul.hashtagworldmap;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.londatiga.android.instagram.Instagram;
import net.londatiga.android.instagram.InstagramSession;

import java.util.ArrayList;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private android.widget.SearchView search;
    private android.widget.TextView username;
    private android.widget.TextView passwort;
    private android.widget.ProgressBar progressBar;
    private android.widget.Button signInButton;
    private android.widget.FrameLayout  frameLayout;

    private InstagramSession mInstagramSession;
    private Instagram mInstagram;


    public ArrayList<Data> infoList = new ArrayList<>();



/*
    private static final String CLIENT_ID = "08ab859c63e742688aab1fbd1c0a6d7f";
    private static final String CLIENT_SECRET = "94b68417b375459d97bdd5f5479449ed";
    private static final String REDIRECT_URI = "http://localhost";
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DownloadTask data1 = new DownloadTask();
        this.infoList = data1.getData(0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        progressBar = (android.widget.ProgressBar) findViewById(R.id.progressBar);
        search = (android.widget.SearchView) findViewById(R.id.searchView);
        signInButton = (android.widget.Button) findViewById(R.id.button);
        frameLayout = (android.widget.FrameLayout) findViewById(R.id.frameLayout2);


        progressBar.setVisibility(View.INVISIBLE);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.GRAY, android.graphics.PorterDuff.Mode.MULTIPLY);
        try {
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    System.out.println(query);      //not necessary
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    System.out.println(newText);    //not necessary
                    return false;
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        username = (android.widget.TextView) findViewById(R.id.editText);
        passwort = (android.widget.TextView) findViewById(R.id.editText2);

        try{
            username.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    System.out.println(v.getText());
                    return false;
                }
            });
            passwort.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    System.out.println(v.getText());
                    return false;
                }
            });
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        signInButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                // Anmeldung bei Instagram
                frameLayout.setVisibility(View.INVISIBLE);
            }
        });
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Duesseldorf and move the camera
        LatLng duesseldorf = new LatLng(51.23442, 6.778618);
        mMap.addMarker(new MarkerOptions().position(duesseldorf).title("Düsseldorf"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(duesseldorf));


        for (Data info : this.infoList) {
            LatLng neu = new LatLng(info.getLatitude(),info.getLongitude());
            mMap.addMarker(new MarkerOptions().position(neu).title(info.getLocName()));
        }

    }
}
