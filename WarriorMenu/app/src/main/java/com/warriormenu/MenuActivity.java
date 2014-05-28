package com.warriormenu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;

import com.cengalabs.flatui.FlatUI;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Vector;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuActivity extends Activity implements LocationListener{
    protected Location myLocation = new Location("current");
    private PullToRefreshLayout myPulltoRefresh;
    private ArrayList<Card> cards;
    private ArrayList<Card> cardsOriginal;
    private Vector<RInfo> restaurants;
    private LocationManager locationManager;
    private String[] sortOptions;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private CardArrayAdapter myAdapter;
    private CardListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        GlobalApp globalApp = (GlobalApp) getApplicationContext();
        globalApp.setRestaurants(intRests());
        cardsOriginal = new ArrayList<Card>();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, Looper.myLooper());
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,0,this);
        final TextView mainTitle = (TextView) findViewById(R.id.main_textView1);
        mainTitle.setTypeface(globalApp.getTypeface());
        FlatUI.initDefaultValues(this);
        FlatUI.setDefaultTheme(FlatUI.GRASS);
        sortOptions = getResources().getStringArray(R.array.sorts);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.drawer_left);

        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_item, sortOptions));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());


        try{
            Thread.sleep(5000);
        }catch(Exception ex) {
            Log.d("waitcatch", "yes");
        }

        for(int i = 0; i < globalApp.getRestaurants().size();i++){
           try {
               globalApp.getRestaurants().get(i).idPicture = getResources().getIdentifier(globalApp.getRestaurants().get(i).photoloc, "drawable", getPackageName());
           }catch(Exception e){
               Log.d("Exception", "e");
           }

           CustomCard card = new CustomCard(this, globalApp.getRestaurants().get(i), globalApp.getTypeface(), globalApp.getTypeface2(), myLocation);
           card.info.restID = i;
           card.setShadow(true);
           card.setSwipeable(true);
           card.setOpenClose();
           card.setOnClickListener(new Card.OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    Intent intent = new Intent(MenuActivity.this, SingleActivity.class);
                    intent.putExtras(bundling(((CustomCard)card).info.restID));
                    startActivity(intent);
                }
            });
           //card.resImage.setImageResource(imageID(this, url));
           //card.setSwipeable(true);
           cardsOriginal.add(card);
        }

        cards = new ArrayList<Card>(cardsOriginal);
        int category = unBundling(this.getIntent().getExtras());
        listView = (CardListView) findViewById(R.id.myList);
        listView.setDividerHeight(10);
        categoryCards(category);

        //myPulltoRefresh = (PullToRefreshLayout) findViewById(R.id.refresh_layout);

        //ActionBarPullToRefresh.from(this).allChildrenArePullable().listener(this).setup(myPulltoRefresh);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent e){
        if(keyCode == KeyEvent.KEYCODE_MENU){
            if(!drawerLayout.isDrawerOpen(drawerList))
                drawerLayout.openDrawer(drawerList);
            return true;
        }
        return super.onKeyDown(keyCode, e);
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

    public class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View v, int p, long id){
            sortDrawerCards(p);
        }

        private void sortDrawerCards(int p){
            drawerList.setItemChecked(p, true);
            if(p == 0)
                sortAZNames();
            if(p == 1)
                sortZANames();
            if(p == 2)
                sortWarrior();
            if(p == 3)
                sortClosest();
            if(p == 4)
                sortOpened();
            if(p == 5)
                resetCards();
            myAdapter.notifyDataSetChanged();
            drawerLayout.closeDrawer(drawerList);
        }



        private void sortAZNames(){
            Collections.sort(cards, new Comparator<Card>() {
                @Override
                public int compare(Card card, Card card2) {
                    return ((CustomCard) card).info.name.compareTo(((CustomCard) card2).info.name);
                }
            });

        }

        private void sortZANames(){
            Collections.sort(cards, new Comparator<Card>() {
                @Override
                public int compare(Card card, Card card2) {
                    return ((CustomCard) card2).info.name.compareTo(((CustomCard) card).info.name);
                }
            });

        }

        private void sortWarrior(){
            ArrayList<Card> temp = new ArrayList<Card>();
            for(int i =0; i<cards.size();i++){
                CustomCard cc = (CustomCard) cards.get(i);
                if(cc.info.warriorD)
                    temp.add(cards.get(i));
            }

            cards.clear();
            cards.addAll(temp);
        }

        private void sortClosest(){
            Collections.sort(cards, new DistComparator());
        }

        private void sortOpened(){
            ArrayList<Card> temp = new ArrayList<Card>();
            for(int i =0; i<cards.size();i++){
                CustomCard cc = (CustomCard) cards.get(i);
                if(cc.info.open)
                    temp.add(cards.get(i));
            }

            cards.clear();
            cards.addAll(temp);
        }


        private void resetCards(){
            cards.clear();
            cards.addAll(cardsOriginal);
        }


    }

    public void categoryCards(int c){
        if(c == 1)
           sortCuisine("American");
        if(c == 2)
            sortCuisine("Italian");
        if(c == 3)
            sortCuisine("Asian");
        if(c == 4)
            sortCuisine("Mediterranean");
        if(c == 5)
            sortCuisine("Coffee/Bakery");
        if(c == 6)
            sortAffordable();
        if(c == 7)
            sortWarrior();
        if(c == 8)
            sortOpened();
        myAdapter = new CardArrayAdapter(getApplicationContext(), cards);
        if(listView != null)
            listView.setAdapter(myAdapter);

    }

    private void sortCuisine(String c){
        ArrayList<Card> temp = new ArrayList<Card>();
        for(int i =0; i<cardsOriginal.size();i++){
            CustomCard cc = (CustomCard) cardsOriginal.get(i);
            if((cc.info.cuisine.compareTo(c)) == 0)
                temp.add(cardsOriginal.get(i));
        }

        cards.clear();
        cards.addAll(temp);
    }

    private void sortAffordable(){
        ArrayList<Card> temp = new ArrayList<Card>();
        for(int i =0; i<cardsOriginal.size();i++){
            CustomCard cc = (CustomCard) cardsOriginal.get(i);
            if((cc.info.price.compareTo("$")) == 0)
                temp.add(cardsOriginal.get(i));
        }

        cards.clear();
        cards.addAll(temp);
    }

    private void sortOpened(){
        ArrayList<Card> temp = new ArrayList<Card>();
        for(int i =0; i<cards.size();i++){
            CustomCard cc = (CustomCard) cards.get(i);
            if(cc.info.open)
                temp.add(cards.get(i));
        }

        cards.clear();
        cards.addAll(temp);
    }

    private void sortWarrior(){
        ArrayList<Card> temp = new ArrayList<Card>();
        for(int i =0; i<cardsOriginal.size();i++){
            CustomCard cc = (CustomCard) cardsOriginal.get(i);
            if(cc.info.warriorD)
                temp.add(cardsOriginal.get(i));
        }

        cards.clear();
        cards.addAll(temp);
    }


    public Bundle bundling(int id){
        Bundle b = new Bundle();
        b.putInt("info", id);
        b.putDouble("lat", myLocation.getLatitude());
        b.putDouble("long", myLocation.getLongitude());
        return b;
    }

    public int unBundling(Bundle b){
        return b.getInt("c");
    }

}


