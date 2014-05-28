package com.warriormenu;

import android.app.Application;

import java.util.Collections;
import java.util.Vector;

/**
 * Created by vannguyen on 5/27/14.
 */
public class GlobalApp extends Application {
    private Vector<RInfo> restaurants = new Vector<RInfo>();

    public GlobalApp(){
        restaurants = new Vector<RInfo>();
    }

    public Vector<RInfo> getRestaurants(){
        return restaurants;
    }

    public void setRestaurants(Vector<RInfo> r){
        restaurants.addAll(r);
    }

    public String getURL() {
        return "http://192.168.1.215:3000";
    }
}
