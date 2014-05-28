package com.warriormenu;

import android.app.Application;
import android.graphics.Typeface;

import java.util.Collections;
import java.util.Vector;

/**
 * Created by vannguyen on 5/27/14.
 */
public class GlobalApp extends Application {
    private Vector<RInfo> restaurants = new Vector<RInfo>();
    private Typeface typeface;
    private Typeface typeface2;

    public GlobalApp(){
        restaurants = new Vector<RInfo>();
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
}
