package com.example.paul.hashtagworldmap;

/**
 * Created by paul on 20.08.16.
 */
public class Data {
    public Double latitude = 0.0;
    public Double longitude = 0.0;
    public String name = "";

    public void setLatitude(double latitude){ this.latitude=latitude; }

    public void setLongitude(double longitude){
        this.longitude=longitude;
    }

    public void setLocName(String  name){
        this.name=name;
    }


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