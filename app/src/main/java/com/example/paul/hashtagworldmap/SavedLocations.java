package com.example.paul.hashtagworldmap;


import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by paul on 11.09.16.
 * dient als Speicherort für die bereits aufgerufenen Locations
 */
public class SavedLocations {

    ArrayList<Data> infoList = new ArrayList<>();
    ArrayList<ArrayList<Data>> list = new ArrayList<>();

    // ------ SETTER -------

    public void setNewLocations(ArrayList<Data> infoList) {
        System.out.println("SAVED!");

        this.infoList = infoList;
        list.add(this.infoList);

    }

    // ------ GETTER -------

    public ArrayList<ArrayList<Data>> getSavedLocations(){

        return list;

    }
}