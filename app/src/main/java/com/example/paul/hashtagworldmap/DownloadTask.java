package com.example.paul.hashtagworldmap;

import android.os.Build;
import android.os.StrictMode;
import net.londatiga.android.instagram.InstagramRequest;
import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DownloadTask{
    public ArrayList<Data> infoList;

    public ArrayList<Data> getData(int call){      // getter für Daten der API
        getInfo(call);

       // System.out.println("INFOLIST: " + this.infoList);

        return this.infoList;
    }

    public void getInfo(int call){      // Zugriff auf Instagram API via Link

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        ArrayList<Data> infoList1 = new ArrayList<>();

        for (int i = 1; i<=call; i++) {


            String endpoint = "locations/" + i + "?access_token=2016498856.08ab859.910c92509e904a4cb1a02dfc71d54015";
            List<NameValuePair> params = new ArrayList<>();
            InstagramRequest request = new InstagramRequest();                                  // externes Modul AndroidInsta (hier musste über Import Modul ein externes Modul importiert werden, dass anschließend als Dependecy hinzugefügt wurde)


            try {
                String getRequestJSON = request.createRequest("GET", endpoint, params);


                JSONObject jsonObj = (JSONObject) new JSONTokener(getRequestJSON).nextValue();  // JSON-Format auslesen
                JSONObject jsonData = jsonObj.getJSONObject("data");                            // JSON-Format auslesen
                JSONObject meta = jsonObj.getJSONObject("meta");
                int code = meta.getInt("code");

                Data info = new Data();

                info.setLatitude(jsonData.getDouble("latitude"));
                info.setLongitude(jsonData.getDouble("longitude"));
                info.setLocName(jsonData.getString("name"));

                if (code != 200){
                    System.out.println("CODE: " + code);
                    if (code == 429){
                        System.out.println("Es werden mehrere AccessToken benötigt");
                    }
                }else {
                    infoList1.add(info);
                }

            } catch (JSONException e) {

            } catch (Exception e) {

            }
        }
        System.out.println("INFOLIST1: " + infoList1);
        this.infoList = infoList1;
    }
}
