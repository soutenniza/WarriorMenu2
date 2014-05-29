package com.warriormenu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cengalabs.flatui.FlatUI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import it.gmariotti.cardslib.library.internal.Card;


public class SplashActivity extends Activity implements LocationListener {

    private GlobalApp globalApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        globalApp = (GlobalApp) getApplicationContext();
        globalApp.setTypefaces(Typeface.createFromAsset(getAssets(), "Roboto-LightItalic.ttf"),Typeface.createFromAsset(getAssets(), "Roboto-BoldCondensedItalic.ttf"));
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ImageView imageView = (ImageView) findViewById(R.id.splash_icon);
        imageView.setImageResource(R.drawable.icon_wm);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,0,this);

        final TextView mainTitle = (TextView) findViewById(R.id.splash_msg);


        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                globalApp.setRestaurants(intRests());
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 2000);

    }

    private void finishActivity(){
        this.finish();
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer sBuffer = new StringBuffer();
        String strLine = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sBuffer.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sBuffer.toString();
    }

    private Vector<RInfo> intRests(){
        final GlobalApp globalApp = (GlobalApp) getApplicationContext();
        Vector<RInfo> restaurantArray = new Vector<RInfo>();
        String array;
        String[] infoArray;
        String strFile = null;
        InputStream inFile = getResources().openRawResource(R.raw.restaurants);
        try{
            URL url = new URL(globalApp.getURL() + "/restaurants");
            HttpURLConnection con = (HttpURLConnection) url
                    .openConnection();
            con.connect();
            strFile = readStream(con.getInputStream());

            JSONArray restaurants = new JSONArray(strFile);
            for (int i = 0; i < restaurants.length(); i++) {
                JSONObject row = restaurants.getJSONObject(i);
                RInfo restaurant = new RInfo();
                restaurant.days = new HashMap<String, Day>();
                JSONObject hours = row.getJSONObject("hours");
                String[] days = {"sunday", "monday","tuesday",
                        "wednesday", "thursday", "friday", "saturday"};
                restaurant.restID = row.getInt("id");
                restaurant.name = row.getString("name");
                restaurant.address = row.getString("address");
                restaurant.latitude = row.getDouble("latitude");
                restaurant.longitude = row.getDouble("longitude");
                restaurant.warriorD = row.getBoolean("warrior_bucks");
                restaurant.rating = row.getDouble("rating");
                restaurant.number = row.getString("number");
                restaurant.photoloc = row.getString("photo_loc");
                restaurant.cuisine = row.getString("cuisine");
                restaurant.price = row.getString("price");
                restaurant.comments = new Vector<Comment>();
                JSONArray comments = row.getJSONArray("comments");
                for (int j = 0; j < comments.length(); j++) {
                    JSONObject jsonComment = comments.getJSONObject(j);
                    Comment comment = new Comment();
                    comment.restaurant_id = jsonComment.getInt("restaurant_id");
                    comment.comment = jsonComment.getString("comment");
                    comment.name = jsonComment.getString("name");
                    comment.rating = jsonComment.getDouble("rating");
                    restaurant.comments.add(comment);
                }
                for (int j = 0; j < days.length; j++) {
                    Day day = new Day();
                    JSONObject dayObj = hours.getJSONObject(days[j]);
                    day.open = dayObj.getInt("open");
                    day.close = dayObj.getInt("close");

                    restaurant.days.put(days[j],day);
                }
                restaurantArray.add(restaurant);

            }
        } catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return restaurantArray;
    }

    @Override
    public void onProviderEnabled(String provider){
        Log.d("provider:", "enabled");
    }

    @Override
    public void onProviderDisabled(String provider){
        Log.d("provider:", "disabled");
    }

    @Override
    public void onLocationChanged(Location location){
        globalApp.setLocation(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extra){
        Log.d("Lat", "Status");
    }
}
