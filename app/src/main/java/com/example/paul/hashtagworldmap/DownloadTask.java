package com.example.paul.hashtagworldmap;

import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import net.londatiga.android.instagram.InstagramRequest;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DownloadTask{
    public ArrayList<Data> infoList;
    public double latFromLoc;
    public double lonFromLoc;
    public int distance;


    public void setLoc(double latitude, double longitude, int distance){
        this.latFromLoc = latitude;
        this.lonFromLoc = longitude;
        this.distance = distance;
    }

    public ArrayList<Data> getData(){      // getter für Daten der API
        getInfo();

        return this.infoList;
    }

    public void getInfo(){      // Zugriff auf Instagram API via Link

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        ArrayList<Data> infoList1 = new ArrayList<>();



            String endpoint = "locations/search?lat=" + latFromLoc + "&lng="+ lonFromLoc +"&distance=" + distance + "&access_token=2016498856.08ab859.910c92509e904a4cb1a02dfc71d54015";

            List<NameValuePair> params = new ArrayList<>();
            InstagramRequest request = new InstagramRequest();                                  // externes Modul AndroidInsta (hier musste über Import Modul ein externes Modul importiert werden, dass anschließend als Dependecy hinzugefügt wurde)


            try {
                String getRequestJSON = request.createRequest("GET", endpoint, params);


                JSONObject jsonObj = (JSONObject) new JSONTokener(getRequestJSON).nextValue();  // JSON-Format auslesen
                JSONArray jsonData = jsonObj.getJSONArray("data");                            // JSON-Format auslesen
                JSONObject meta = jsonObj.getJSONObject("meta");
                int code = meta.getInt("code");


                for(int i = 0; i<jsonData.length(); i++) {

                    Data info = new Data();
                    JSONObject k = jsonData.getJSONObject(i);

                    info.setLatitude(k.getDouble("latitude"));
                    info.setLongitude(k.getDouble("longitude"));
                    info.setLocName(k.getString("name"));

                    if (code == 200) {
                        infoList1.add(info);



                    } else {
                        System.out.println("Fehler: " + code);
                    }
                }

            } catch (JSONException e) {

            } catch (Exception e) {

            }

        System.out.println("INFOLIST1: " + String.valueOf(infoList1));
        this.infoList = infoList1;
        SavedLocations newLoc = new SavedLocations();
        newLoc.setNewLocations(infoList1);
    }
}
