package com.warriormenu;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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
import com.cengalabs.flatui.views.FlatButton;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
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
    private Typeface typeface;
    private Typeface typeface2;
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

        restaurants = intRests();
        cards = new ArrayList<Card>();
        typeface = Typeface.createFromAsset(getAssets(), "Roboto-LightItalic.ttf");
        typeface2 = Typeface.createFromAsset(getAssets(), "Roboto-BoldCondensedItalic.ttf");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, Looper.myLooper());
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,0,this);
        TextView mainTitle = (TextView) findViewById(R.id.main_textView1);
        mainTitle.setTypeface(typeface);
        FlatUI.initDefaultValues(this);
        FlatUI.setDefaultTheme(FlatUI.GRASS);
        sortOptions = getResources().getStringArray(R.array.sorts);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.drawer_left);

        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_item, sortOptions));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());


        try{
            Thread.sleep(4000);
        }catch(Exception ex) {
            Log.d("waitcatch", "yes");
        }

        for(int i = 0; i < restaurants.size();i++){
           try {
               restaurants.get(i).id = getResources().getIdentifier(restaurants.get(i).photoloc, "drawable", getPackageName());
           }catch(Exception e){
               Log.d("Exception", "e");
           }

           CustomCard card = new CustomCard(this, restaurants.get(i), typeface, typeface2, myLocation);
           card.setId(card.info.address);
           card.setShadow(true);
           //card.resImage.setImageResource(imageID(this, url));
           //card.setSwipeable(true);
           cards.add(card);
        }

        cardsOriginal = new ArrayList<Card>(cards);
        myAdapter = new CardArrayAdapter(getApplicationContext(),cards);
        listView = (CardListView) findViewById(R.id.myList);
        if (listView!=null){
            listView.setAdapter(myAdapter);
        }

        //myPulltoRefresh = (PullToRefreshLayout) findViewById(R.id.refresh_layout);

        //ActionBarPullToRefresh.from(this).allChildrenArePullable().listener(this).setup(myPulltoRefresh);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return false;
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
                restaurant.photoloc = row.getString("photo_loc");
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
            sortCards(p);
        }

        private void sortCards(int p){
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


}


