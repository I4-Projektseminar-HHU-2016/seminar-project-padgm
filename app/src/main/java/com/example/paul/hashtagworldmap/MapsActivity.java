package com.example.paul.hashtagworldmap;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.SeekBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.londatiga.android.instagram.Instagram;
import net.londatiga.android.instagram.InstagramSession;

import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
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


    public ArrayList<Data> infoList = new ArrayList<>();


    private static final String CLIENT_ID = "08ab859c63e742688aab1fbd1c0a6d7f";
    private static final String CLIENT_SECRET = "94b68417b375459d97bdd5f5479449ed";
    private static final String REDIRECT_URI = "http://localhost";

    private double latitude;
    private double longitude;
    private DownloadTask data1;


    public int distance;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        menu = (android.widget.ImageButton) findViewById(R.id.menu);
        menuPoint1 = (android.widget.Button) findViewById(R.id.menuPoint1);
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

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


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
        //googleMap = this.mMap;
        LatLng standort = new LatLng(this.latitude, this.longitude);
        googleMap.addMarker(new MarkerOptions().position(standort).title("Standort"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(standort));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        System.out.println("ORT: " + latitude +"/ //// / "+ longitude);
        for (Data info : this.infoList) {
            LatLng neu = new LatLng(info.getLatitude(), info.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(neu).title(info.getLocName()));
        }
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

    public void restart() {
        frameLayout.setVisibility(View.VISIBLE);
        infoList = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            try {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
            }catch (ClassCastException e){
                e.printStackTrace();
            }
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Location services connected");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        }
        else {
            handleNewLocation(location);
        }
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
