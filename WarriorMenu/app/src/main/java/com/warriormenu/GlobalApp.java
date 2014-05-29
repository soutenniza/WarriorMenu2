package com.warriormenu;

import android.app.Application;
import android.graphics.Typeface;
import android.location.Location;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by vannguyen on 5/27/14.
 */
public class GlobalApp extends Application {
    private Vector<RInfo> restaurants = new Vector<RInfo>();
    private Typeface typeface;
    private Typeface typeface2;
    private ArrayList<Card> originalCards;
    private Location myLocation;
    private boolean cardsInitialized;

    public GlobalApp(){
        restaurants = new Vector<RInfo>();
        originalCards = new ArrayList<Card>();
        myLocation = new Location("me!");
        cardsInitialized = false;
    }

    public void setTypefaces(Typeface t, Typeface t2){
        this.typeface = t;
        this.typeface2 = t2;
    }

    public Typeface getTypeface(){
        return typeface;
    }

    public Typeface getTypeface2(){
        return typeface2;
    }
    public Vector<RInfo> getRestaurants(){
        return restaurants;
    }

    public void setRestaurants(Vector<RInfo> r){
        restaurants.addAll(r);
    }

    public String getURL() {
        return "http://warrior-dev.cfapps.io";
    }

    public ArrayList<Card> getOriginalCards(){
        return  originalCards;
    }

    public void setOriginalCards(ArrayList<Card> d){
        originalCards = new ArrayList<Card>();
        originalCards.addAll(d);
    }

    public void addCard(Card card){
        this.originalCards.add(card);
    }

    public void setLocation(double lat, double lon){
        myLocation.setLatitude(lat);
        myLocation.setLongitude(lon);
    }

    public Location getLocation(){
        return myLocation;
    }

    public void setCardsInitialized(Boolean b){
        cardsInitialized = b;
    }

    public boolean getCardsInitialized(){
        return cardsInitialized;
    }
}
