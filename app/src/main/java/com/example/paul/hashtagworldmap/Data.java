package com.example.paul.hashtagworldmap;

/**
 * Created by paul on 20.08.16.
 * Data dient als Übertragung zwischen Activities
 */
public class Data {

    public Double latitude = 0.0;
    public Double longitude = 0.0;
    public String name = "";

    //  ------ SETTER -------

    // die setter setzen Name, Lägen- und Breitengrad
    public void setLatitude(double latitude){ this.latitude=latitude; }

    public void setLongitude(double longitude){
        this.longitude=longitude;
    }

    public void setLocName(String  name){
        this.name=name;
    }



    //  ------ GETTER -------

    // getter geben Name, Lägen- und Breitengrad zurück
    public Double getLatitude() {
        return this.latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public String getLocName() {
        return this.name;
    }
}