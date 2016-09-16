package com.example.paul.hashtagworldmap;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by paul on 12.09.16.
 */
public class CurrentLocation {

    public static LatLng loc = new LatLng(0, 0);
    public static String query;
    public static boolean entry;

    // SETTER

    public static void setCurLoc(LatLng loc1){

        loc = loc1;

    }

    public static void setCurLoc(String query1){

        query = query1;
        entry = true;

    }

    // GETTER

    public static String getCurLocQuery(){

        return query;

    }

    public static LatLng getCurLoc(){

        return loc;

    }

    // ________

    public static boolean entry(){

        return entry;

    }

    public static void setEntryFalse(){

        entry = false;

    }


}
