package com.example.paul.hashtagworldmap;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
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
    private android.widget.FrameLayout frameLayout;
    private android.widget.ImageView mImageView;
    private android.widget.ImageButton exit;
    private android.widget.SeekBar seekBar;
    private android.widget.TextView textView;
    private android.widget.ImageButton imageButton2;


    private InstagramSession mInstagramSession;
    private Instagram mInstagram;


    public ArrayList<Data> infoList = new ArrayList<>();


    private static final String CLIENT_ID = "08ab859c63e742688aab1fbd1c0a6d7f";
    private static final String CLIENT_SECRET = "94b68417b375459d97bdd5f5479449ed";
    private static final String REDIRECT_URI = "http://localhost";


    LocationTracker locTracker;

    public int distance;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        final DownloadTask data1 = new DownloadTask();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        imageButton2 = (android.widget.ImageButton) findViewById(R.id.imageButton2);
        progressBar = (android.widget.ProgressBar) findViewById(R.id.progressBar);
        search = (android.widget.SearchView) findViewById(R.id.searchView);
        signInButton = (android.widget.Button) findViewById(R.id.button);
        frameLayout = (android.widget.FrameLayout) findViewById(R.id.frameLayout2);
        textView = (android.widget.TextView) findViewById(R.id.textView);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.GRAY, android.graphics.PorterDuff.Mode.MULTIPLY);
        exit = (android.widget.ImageButton) findViewById(R.id.exit);
        seekBar = (android.widget.SeekBar) findViewById(R.id.seekBar);
        search.setVisibility(View.INVISIBLE);
        imageButton2.setVisibility(View.INVISIBLE);



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

        signInButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                locTracker = new LocationTracker(MapsActivity.this, mMap);

                if(locTracker.isCanGetLocation()) {
                    double latitude = locTracker.getLatitude();
                    double longitude = locTracker.getLongitude();

                    Toast.makeText(getApplicationContext(), "Your Location is -\nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    data1.setLoc(51.23442, 6.778618, distance);

                }
                else {
                    locTracker.showSettingsAlert();
                }

                frameLayout.setVisibility(View.INVISIBLE);
                search.setVisibility(View.VISIBLE);
                imageButton2.setVisibility(View.VISIBLE);
            }
        });

        exit.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                frameLayout.setVisibility(View.INVISIBLE);
                search.setVisibility(View.VISIBLE);
                imageButton2.setVisibility(View.VISIBLE);
            }
        });


        imageButton2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenu();
            }
        });

        this.infoList = data1.getData();
    }

    private void openMenu() {

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
        LatLng duesseldorf = new LatLng(51.371556, 6.513465);
        mMap.addMarker(new MarkerOptions().position(duesseldorf).title("Düsseldorf"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(duesseldorf));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        for (Data info : this.infoList) {
            LatLng neu = new LatLng(info.getLatitude(),info.getLongitude());
            mMap.addMarker(new MarkerOptions().position(neu).title(info.getLocName()));
        }
    }

}
