package com.warriormenu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class MenuActivity extends Activity{
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
    private GlobalApp globalApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        SharedPreferences preferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        boolean firstStart = preferences.getBoolean("drawer", true);
        globalApp = (GlobalApp) getApplicationContext();
        final TextView mainTitle = (TextView) findViewById(R.id.main_textView1);
        mainTitle.setTypeface(globalApp.getTypeface());
        sortOptions = getResources().getStringArray(R.array.sorts);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.drawer_left);
        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_item, sortOptions));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        if(firstStart){
            drawerLayout.openDrawer(drawerList);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("drawer", false);
            editor.commit();
        }

        cardsOriginal = new ArrayList<Card>();
        intializeCards();
        cards = new ArrayList<Card>(cardsOriginal);
        globalApp.setOriginalCards(cardsOriginal);
        int category = unBundling(this.getIntent().getExtras());
        listView = (CardListView) findViewById(R.id.myList);
        listView.setDividerHeight(10);
        categoryCards(category);

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
        if(c == 9)
            sortCuisine("Latino");
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
        for(int i =0; i<cardsOriginal.size();i++){
            CustomCard cc = (CustomCard) cardsOriginal.get(i);
            if(cc.info.open)
                temp.add(cardsOriginal.get(i));
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

    public void intializeCards(){
            for(int i = 0; i < globalApp.getRestaurants().size();i++){
                try {
                    globalApp.getRestaurants().get(i).idPicture = getResources().getIdentifier(globalApp.getRestaurants().get(i).photoloc, "drawable", getPackageName());
                }catch(Exception e){
                    Log.d("Exception", "e");
                }

                CustomCard card = new CustomCard(this, globalApp.getRestaurants().get(i), globalApp.getTypeface(), globalApp.getTypeface2(), globalApp.getLocation());
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
    }
}


