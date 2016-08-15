package com.example.paul.hashtagworldmap;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.SearchView;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private android.widget.SearchView search;
    private String contentJSON;
    private ArrayList<String> contentJSONArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        search = (android.widget.SearchView) findViewById(R.id.searchView);


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

        }
        for (int i = 0; i<20; i++) {
            new JSONFromURL().execute("https://api.instagram.com/v1/locations/" + i + "?access_token=2016498856.08ab859.910c92509e904a4cb1a02dfc71d54015");
        }
        JSONArray jsonFile = new JSONArray(contentJSONArray);
    }

public class JSONFromURL extends AsyncTask<String, String, String>{


    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();


                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

                contentJSONArray.add(buffer.toString());
                System.out.println(contentJSONArray);

            }catch(FileNotFoundException e){

            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (connection != null){
                connection.disconnect();
            }
            if(reader != null) {
                connection.disconnect();
            }

        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
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

    }
}
