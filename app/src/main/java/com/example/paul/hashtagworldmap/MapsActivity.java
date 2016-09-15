package com.example.paul.hashtagworldmap;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback{


    private GoogleMap mMap;
    private android.widget.SearchView search;
    private android.widget.FrameLayout menuOpen;
    private android.widget.ImageButton menu;
    private android.widget.ImageButton menuExit;
    private android.widget.Button menuPoint1;

    public ArrayList<Data> infoList = new ArrayList<>();


    private static final String CLIENT_ID = "08ab859c63e742688aab1fbd1c0a6d7f";
    private static final String CLIENT_SECRET = "94b68417b375459d97bdd5f5479449ed";
    private static final String REDIRECT_URI = "http://localhost";

    private double latitude;
    private double longitude;
    private DownloadTask data1;
    public CurrentLocation curLoc= null;
    public int distance;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);

        menu = (android.widget.ImageButton) findViewById(R.id.menu);
        menuExit = (android.widget.ImageButton) findViewById(R.id.menuExit);
        menuPoint1 = (android.widget.Button) findViewById(R.id.menuPoint1);
        search = (android.widget.SearchView) findViewById(R.id.searchView);
        menuOpen = (android.widget.FrameLayout) findViewById(R.id.menuOpen);

        getCurrentLocation();
        downloadData();

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
                    menu.setVisibility(View.INVISIBLE);
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
        menu.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenu();
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
        LatLng standort = new LatLng(this.latitude, this.longitude);

        googleMap.addMarker(new MarkerOptions().position(standort).title("Standort").icon(BitmapDescriptorFactory.fromResource(R.mipmap.standort)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(standort));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        System.out.println("ORT: " + latitude + " / //// / " + longitude);
        for (Data info : this.infoList) {
            LatLng neu = new LatLng(info.getLatitude(), info.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(neu).title(info.getLocName()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker1)));
        }

        SavedLocations getLoc = new SavedLocations();
        ArrayList<ArrayList<Data>> list = new ArrayList<>();
        ArrayList<Data> infoList1 = new ArrayList<>();


        try {
            list = getLoc.getSavedLocations();

            for(int i = 1; i<list.size(); i++) {
               System.out.println("TEST!!! " + list.get(i));
                infoList1 = list.get(i);

                for (Data info : infoList1) {
                    LatLng neu = new LatLng(info.getLatitude(), info.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(neu).title(info.getLocName()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker2)));
                }

            }
        }catch(NullPointerException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.mMap = googleMap;
    }



    public void startStartActivity(){

        Intent startScreen = new Intent(this, StartActivity.class);
        startActivity(startScreen);

    }

    public void getCurrentLocation(){
        try {
        LatLng currentLocation = CurrentLocation.getCurLoc();

            this.latitude = currentLocation.latitude;
            this.longitude = currentLocation.longitude;
        }catch(NullPointerException e){
            e.printStackTrace();
        }

    }

    public void downloadData(){

        data1 = new DownloadTask();
        data1.setLoc(this.latitude, this.longitude, StartActivity.getDistance());
        infoList = data1.getData();

    }

    private void openMenu() {
        menu.setVisibility(View.INVISIBLE);
        menuExit.setVisibility(View.VISIBLE);
        menuOpen.setVisibility(View.VISIBLE);
        menuPoint1.setVisibility(View.VISIBLE);


        menuExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuOpen.setVisibility(View.INVISIBLE);
                menu.setVisibility(View.VISIBLE);
            }
        });

        menuPoint1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restart();
                menuOpen.setVisibility(View.INVISIBLE);
                menu.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void restart() {
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

        infoList = null;
        mMap.clear();

        startStartActivity();
    }
}
