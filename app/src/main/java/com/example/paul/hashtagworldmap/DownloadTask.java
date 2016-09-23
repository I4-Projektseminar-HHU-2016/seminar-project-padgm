package com.example.paul.hashtagworldmap;


import android.os.Build;
import android.os.StrictMode;
import net.londatiga.android.instagram.InstagramRequest;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.util.ArrayList;
import java.util.List;

public class DownloadTask{
    public ArrayList<Data> infoList;
    public double latFromLoc;
    public double lonFromLoc;
    public int distance;
    public int count;


    public void setLoc(double latitude, double longitude, int distance, int count){
        this.latFromLoc = latitude;
        this.lonFromLoc = longitude;
        this.distance = distance;
        this.count = count;
    }

    // getter für Daten der API
    public ArrayList<Data> getData(){
        getInfo();

        return this.infoList;
    }


    // Zugriff auf Instagram API via Link
    public void getInfo() {

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        ArrayList<Data> infoList1 = new ArrayList<>();


        String endpoint = "locations/search?lat=" + latFromLoc + "&lng=" + lonFromLoc + "&distance=" + distance + "&count=" + count +"&access_token=2016498856.08ab859.910c92509e904a4cb1a02dfc71d54015";

        List<NameValuePair> params = new ArrayList<>();
        InstagramRequest request = new InstagramRequest();    // hier wird das externe Modul AndroidInsta verwendet(dabei musste über Import Modul ein externes Modul importiert werden, dass anschließend als Dependency hinzugefügt wurde)


        try {
            String getRequestJSON = request.createRequest("GET", endpoint, params);


            JSONObject jsonObj = (JSONObject) new JSONTokener(getRequestJSON).nextValue();  // JSON-Format auslesen
            JSONArray jsonData = jsonObj.getJSONArray("data");
            JSONObject meta = jsonObj.getJSONObject("meta");
            int code = meta.getInt("code");


            for (int i = 0; i < jsonData.length(); i++) {

                Data info = new Data();     //Data() als Zwischenspeicherung
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("INFOLIST1: " + String.valueOf(infoList1));
        this.infoList = infoList1;

    }

}
