package com.example.paul.hashtagworldmap;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by paul on 12.09.16.
 */
public class LocationActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    public static final String TAG = LocationActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private double latitude;
    private double longitude;
    private boolean startedMap = false;


    private android.widget.TextView latInfo;
    private android.widget.TextView lonInfo;
    private android.widget.ProgressBar progressBar;

    //------------ LocationActivity ----------------

    @Override
    protected void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_activity);


        progressBar = (android.widget.ProgressBar) findViewById(R.id.progressBar);
        latInfo = (android.widget.TextView) findViewById(R.id.latInfo);
        lonInfo = (android.widget.TextView) findViewById(R.id.lonInfo);




        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1000); // 1 second, in milliseconds

        progressBar.setProgress(20);
        progressBar.setProgress(30);
        progressBar.setProgress(35);


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

            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, new com.google.android.gms.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                }
            });
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

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest,
                    new com.google.android.gms.location.LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {

                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    });
            setCurrentLocation();
        }
        else {
            progressBar.setProgress(40);
            progressBar.setProgress(50);
            progressBar.setProgress(60);
            handleNewLocation(location);
        }
        progressBar.setProgress(40);
        progressBar.setProgress(50);
        progressBar.setProgress(60);

    }

    public void handleNewLocation(Location location) {

        try {
            Log.d(TAG, location.toString());

            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();

        }catch(NullPointerException e){
            e.printStackTrace();
        }

        setCurrentLocation();

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
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

    public void setCurrentLocation(){

        System.out.println("LOCATIONACTIVITY SET CURRENT LOCATION: LAT = " + this.latitude + " LNG = " + this.longitude);
        LatLng curLoc = new LatLng(this.latitude, this.longitude);
        CurrentLocation.setCurLoc(curLoc);

        latInfo.setText(String.valueOf(latitude));
        lonInfo.setText(String.valueOf(longitude));



        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(80);
                        progressBar.setProgress(90);
                        progressBar.setProgress(100);


                        if(!startedMap && progressBar.getProgress() == 100) {
                            startedMap = true;
                            startMap();
                        }
                    }
                });
            }
        };
        thread.start();


    }


    public void startMap(){

        Intent mapsActivity = new Intent(this, MapsActivity.class);
        startActivity(mapsActivity);

    }
}

