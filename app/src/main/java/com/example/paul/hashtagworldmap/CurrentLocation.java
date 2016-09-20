package com.example.paul.hashtagworldmap;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by paul on 12.09.16.
 * CurrentLocation dient als Übertragung zwischen den Activities
 */
public class CurrentLocation {

    public static LatLng loc = new LatLng(0, 0);
    public static String query;
    public static boolean entry;

    //  ------ SETTER -------

    //setzt die Location mit LatLng
    public static void setCurLoc(LatLng loc1){

        loc = loc1;

    }

    //setzt die Location mit String (damit auch die Eingabe in cityText berücksichtigt wird)
    public static void setCurLoc(String query1){

        query = query1;
        entry = true;

    }

    //  ------ GETTER -------

    //
    public static String getCurLocQuery(){

        return query;

    }

    public static LatLng getCurLoc(){

        return loc;

    }

    // ------ WEITERE METHODEN -------

    // gibt den entry Wert zurück (für if-Bedingungen)
    public static boolean entry(){

        return entry;

    }

    // setzt den entry Wert auf false
    public static void setEntryFalse(){

        entry = false;

    }


}
