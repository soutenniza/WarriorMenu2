package com.warriormenu;

import android.widget.ImageView;

import java.util.Map;
import java.util.Vector;

/**
 * Created by vannguyen on 5/7/14.
 */
public class RInfo {
    public String name;
    public String address;
    public String number;
    public boolean warriorD;
    public double rating;
    public double latitude;
    public double longitude;
    Map<String, Day> days;
    public float distance;
    public String url;
    public String photoloc;
    public int id;
    public boolean open;
    public String cuisine;
    public String price;
    public Vector<Comment> comments;
}

