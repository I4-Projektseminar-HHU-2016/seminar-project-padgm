package com.example.paul.hashtagworldmap;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by paul on 12.09.16.
 */
public class CurrentLocation {

    public static LatLng loc;

    public static void setCurLoc(LatLng loc1){
        loc = loc1;
    }


    public static LatLng getCurLoc(){
        return loc;
    }
}
