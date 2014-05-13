package com.warriormenu;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Vector;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuActivity extends Activity implements LocationListener{
    protected Location myLocation = new Location("current");
    private PullToRefreshLayout myPulltoRefresh;
    private ArrayList<Card> cards;
    Vector<RInfo> restaurants;
    Typeface typeface;
    Typeface typeface2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        restaurants = intRests();
        cards = new ArrayList<Card>();
        typeface = Typeface.createFromAsset(getAssets(), "Roboto-LightItalic.ttf");
        typeface2 = Typeface.createFromAsset(getAssets(), "Roboto-BoldCondensedItalic.ttf");
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, Looper.myLooper());
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,0,this);

        TextView mainTitle = (TextView) findViewById(R.id.main_textView1);
        mainTitle.setTypeface(typeface);

        for(int i = 0; i < restaurants.size();i++){
           CustomCard card = new CustomCard(getBaseContext(), restaurants.get(i), typeface, typeface2, myLocation);
           card.setShadow(true);
           card.setSwipeable(true);
           cards.add(card);
        }

        CardArrayAdapter myAdapter = new CardArrayAdapter(getApplicationContext(),cards);

        CardListView listView = (CardListView) findViewById(R.id.myList);
        if (listView!=null){
            listView.setAdapter(myAdapter);
        }

        //myPulltoRefresh = (PullToRefreshLayout) findViewById(R.id.refresh_layout);

        //ActionBarPullToRefresh.from(this).allChildrenArePullable().listener(this).setup(myPulltoRefresh);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private String inputStreamToString(InputStream is) throws IOException {
        StringBuffer sBuffer = new StringBuffer();
        DataInputStream dataIO = new DataInputStream(is);
        String strLine = null;
        while((strLine = dataIO.readLine())  != null){
            sBuffer.append(strLine + "\n");
        }

        dataIO.close();
        return sBuffer.toString();
    }

    private Vector<RInfo> intRests(){
        Vector<RInfo> restaurantArray = new Vector<RInfo>();
        String array;
        String[] infoArray;
        String strFile = null;
        InputStream inFile = getResources().openRawResource(R.raw.restaurants);
        try{
            strFile = inputStreamToString(inFile);
            JSONArray restaurants = new JSONArray(strFile);
            for (int i = 0; i < restaurants.length(); i++) {
                JSONObject row = restaurants.getJSONObject(i);
                RInfo restaurant = new RInfo();
                restaurant.days = new HashMap<String, Day>();
                JSONObject hours = row.getJSONObject("hours");
                String[] days = {"sunday", "monday","tuesday",
                        "wednesday", "thursday", "friday", "saturday"};

                restaurant.name = row.getString("name");
                restaurant.address = row.getString("address");
                restaurant.latitude = row.getDouble("latitude");
                restaurant.longitude = row.getDouble("longitude");
                restaurant.warriorD = row.getBoolean("warrior_bucks");
                restaurant.rating = row.getDouble("rating");
                restaurant.number = row.getString("number");
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
        myLocation.setLatitude(location.getLatitude());
        myLocation.setLongitude(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extra){
        Log.d("Lat", "Status");
    }


    /*@Override Placeholder for refresh
    public void onRefreshStarted(View v){
        for(int i = 0; i < restaurants.size();i++){
            CustomCard card = new CustomCard(getBaseContext(), restaurants.get(i), typeface, typeface2, myLocation);
            card.setShadow(true);
            card.setSwipeable(true);
            cards.add(card);
        }
    }*/

}
