package com.example.paul.hashtagworldmap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.SeekBar;

import com.google.android.gms.common.api.GoogleApiClient;
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
    private android.widget.ProgressBar progressBar;
    private android.widget.Button signInButton;
    private android.widget.FrameLayout frameLayout;
    private android.widget.FrameLayout menuOpen;
    private android.widget.ImageView mImageView;
    private android.widget.ImageButton exit;
    private android.widget.SeekBar seekBar;
    private android.widget.TextView textView;
    private android.widget.ImageButton menu;
    private android.widget.Button menuPoint1;



    private InstagramSession mInstagramSession;
    private Instagram mInstagram;


    public ArrayList<Data> infoList = new ArrayList<>();


    private static final String CLIENT_ID = "08ab859c63e742688aab1fbd1c0a6d7f";
    private static final String CLIENT_SECRET = "94b68417b375459d97bdd5f5479449ed";
    private static final String REDIRECT_URI = "http://localhost";

    private double latitude;
    private double longitude;
    private DownloadTask data1;

    private LocationManager locationManager;
    private LocationListener listener;

    public int distance;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //lookUpForLoc();
        menu = (android.widget.ImageButton) findViewById(R.id.menu);
        menuPoint1 = (android.widget.Button)findViewById(R.id.menuPoint1);
        progressBar = (android.widget.ProgressBar) findViewById(R.id.progressBar);
        search = (android.widget.SearchView) findViewById(R.id.searchView);
        signInButton = (android.widget.Button) findViewById(R.id.button);
        frameLayout = (android.widget.FrameLayout) findViewById(R.id.frameLayout2);
        menuOpen = (android.widget.FrameLayout) findViewById(R.id.menuOpen);
        textView = (android.widget.TextView) findViewById(R.id.textView);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.GRAY, android.graphics.PorterDuff.Mode.MULTIPLY);
        exit = (android.widget.ImageButton) findViewById(R.id.exit);
        seekBar = (android.widget.SeekBar) findViewById(R.id.seekBar);
        search.setVisibility(View.INVISIBLE);
        menu.setVisibility(View.INVISIBLE);


        try {
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    menu.setVisibility(View.VISIBLE);
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

        search.setOnClickListener(new SearchView.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.setVisibility(View.INVISIBLE);
            }
        });
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                menu.setVisibility(View.VISIBLE);
                return false;
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int change = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
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
                progressBar.setVisibility(View.VISIBLE);

                data1 = new DownloadTask();
                data1.setLoc(latitude, longitude, distance);

                frameLayout.setVisibility(View.INVISIBLE);
                search.setVisibility(View.VISIBLE);
                menu.setVisibility(View.VISIBLE);
                infoList = data1.getData();
            }
        });

        exit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.setVisibility(View.INVISIBLE);
                search.setVisibility(View.VISIBLE);
                menu.setVisibility(View.VISIBLE);
            }
        });


        menu.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenu();
            }
        });


    }

    private void openMenu() {
        menuOpen.setVisibility(View.VISIBLE);
        menuPoint1.setVisibility(View.VISIBLE);
        menuPoint1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restart();
                menuOpen.setVisibility(View.INVISIBLE);
                menu.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void restart(){
        frameLayout.setVisibility(View.VISIBLE);
        infoList = null;
    }

/*
    public void lookUpForLoc() {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        configure_button();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.

        locationManager.requestLocationUpdates("gps", 5000, 0, listener);
    } */

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
        // Add a marker on current location and move the camera
        LatLng standort = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(standort).title("Standort"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(standort));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        SetMarker locationsAround = new SetMarker();
        locationsAround.setMarker(mMap, infoList);
    }

}
