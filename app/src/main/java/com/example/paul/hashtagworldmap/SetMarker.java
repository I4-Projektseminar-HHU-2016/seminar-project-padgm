package com.example.paul.hashtagworldmap;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by paul on 06.09.16.
 */


public class SetMarker {

    private GoogleMap googleMap;
    private ArrayList<Data> infoList;

    public void setMarker(GoogleMap googleMap, ArrayList<Data> infoList){

        this.googleMap = googleMap;
        this.infoList = infoList;

        System.out.println("angekommen");

        for (Data info : this.infoList) {
            LatLng neu = new LatLng(info.getLatitude(), info.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(neu).title(info.getLocName()));
        }

    }
}
