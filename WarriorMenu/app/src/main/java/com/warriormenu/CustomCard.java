package com.warriormenu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.net.Uri;
import android.util.JsonWriter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cengalabs.flatui.FlatUI;
import com.cengalabs.flatui.views.FlatButton;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by vannguyen on 4/22/14.
 */
public class CustomCard extends Card {
    protected TextView resTitle;
    protected ImageView resImage;
    protected TextView resAddress;
    protected TextView resNumber;
    protected TextView warriorDollars;
    protected TextView openClose;
    protected TextView resHours;
    protected RatingBar ratingBar;
    protected TextView resDistance;
    protected RInfo info;
    protected Typeface typeface;
    protected Typeface typeface2;
    protected Location location;
    protected FlatButton button;


    public CustomCard(Context context, RInfo r, Typeface t, Typeface t2, Location lc){
        this(context, R.layout.card_thumbnail, r,t, t2, lc);
    }

    public CustomCard(Context context, int innerLayout, RInfo r, Typeface t, Typeface t2, Location lc){
        super(context, innerLayout);
        this.info = r;
        this.typeface = t;
        this.typeface2 = t2;
        this.location = lc;
    }

    public void setOpenClose(){
        Date date = new Date();
        Map<Integer, String> dayMapping = new HashMap<Integer, String>();

        String[] days = {"sunday", "monday","tuesday",
                "wednesday", "thursday", "friday", "saturday"};
        for (int i = 0; i < days.length; i++) {
            dayMapping.put(i, days[i]);
        }

        String stringDay = dayMapping.get(date.getDay());
        Day dayHours = info.days.get(stringDay);

        int currentHour = date.getHours()*100;
        if((dayHours.open < currentHour && dayHours.close > currentHour) || dayHours.is24Hour())
            info.open = true;
        else
            info.open = false;
        if(dayHours.isClosed())
            info.open = false;
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view){
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        Map<Integer, String> dayMapping = new HashMap<Integer, String>();

        String[] days = {"sunday", "monday","tuesday",
                "wednesday", "thursday", "friday", "saturday"};
        for (int i = 0; i < days.length; i++) {
            dayMapping.put(i, days[i]);
        }
        resTitle = (TextView) parent.findViewById(R.id.card_title);
        resImage = (ImageView) parent.findViewById(R.id.card_picture);
        //resAddress = (TextView) parent.findViewById(R.id.card_address);
        //resNumber = (TextView) parent.findViewById(R.id.card_number);
        warriorDollars = (TextView) parent.findViewById(R.id.card_dollar);
        //resHours = (TextView) parent.findViewById(R.id.card_hours);
        ratingBar = (RatingBar) parent.findViewById(R.id.card_stars);
        openClose = (TextView) parent.findViewById(R.id.card_open_close);
        button = (FlatButton) parent.findViewById(R.id.card_button);

        String stringDay = dayMapping.get(date.getDay());
        Day dayHours = info.days.get(stringDay);


        if(resTitle != null){
            resTitle.setText(info.name);
            resTitle.setTypeface(typeface2);
        }

        if(resImage != null) {
            resImage.setImageResource(info.idPicture);
        }

        /*if(resAddress != null){
            resAddress.setText(info.address);
            resAddress.setTypeface(typeface);
        }

        if(resNumber != null){
            resNumber.setText(info.number);
            resNumber.setTypeface(typeface);
        }*/

        if(warriorDollars != null){
            if(info.warriorD)
                warriorDollars.setText("Warrior Dollars: Yes");
            else
                warriorDollars.setText("Warrior Dollars: No");
            warriorDollars.setTypeface(typeface);
        }

        if(openClose != null){
            int currentHour = date.getHours()*100;
            if((dayHours.open < currentHour && dayHours.close > currentHour) || dayHours.is24Hour()){
                openClose.setText("Open Now!");
                openClose.setTextColor(Color.parseColor("#009900"));
                info.open = true;
            } else{
                openClose.setText("Closed.");
                openClose.setTextColor(Color.parseColor("#D11919"));
                info.open = false;
            }

            if(dayHours.isClosed()){
                openClose.setText("Closed.");
                openClose.setTextColor(Color.parseColor("#D11919"));
                info.open = false;
            }

        }

        /*if(resHours != null){
            String hours = new String();
            if(dayHours.isClosed()) {
                hours = "Closed Today.";
            } else if(dayHours.is24Hour()) {
                hours = "Open 24 hours.";
            } else {
                hours = "Hours: " + dayHours.openTimeString() + " - " + dayHours.closeTimeString();
            }

            resHours.setText(hours);
            resHours.setTypeface(typeface);
        }*/

        if(ratingBar != null){
            double temp = info.rating*10;
            for(int i = 0; i < info.comments.size(); i++){
                temp += info.comments.get(i).rating;
            }
            info.avgRating = temp/(10.0+info.comments.size());
            ratingBar.setNumStars(5);
            ratingBar.setRating((float)info.avgRating);
            LayerDrawable layer = (LayerDrawable) ratingBar.getProgressDrawable();
            layer.getDrawable(2).setColorFilter(Color.parseColor("#ffcc33"), PorterDuff.Mode.SRC_ATOP);
        }

        if(button != null){
            float[] results = new float[10];
            /*location.setLatitude(42.361525); //Mock Location
            location.setLongitude(-83.069586);*/

            Location.distanceBetween(location.getLatitude(), location.getLongitude(), info.latitude, info.longitude, results);
            float temp = results[0] * 0.0006213f;
            info.distance = results[0];
            //String str = String.format("%.2f", temp);
            button.setText("Directions");
            button.setTypeface(typeface);
            info.url = "http://maps.google.com/maps?saddr=" + location.getLatitude() + "," + location.getLongitude();
            info.url = info.url + "&daddr=" + info.name + "+"+info.address;
            //+ info.latitude + "," + info.longitude;
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse(info.url));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                }
            });



        }

    }
    public int imageID(Context context, String url){
        return context.getResources().getIdentifier("drawable/" + url, null, context.getPackageName());
    }

    private String mapsUrl(Location lc, RInfo rInfo){
        String origin = "origin=" + lc.getLatitude() + "," + lc.getLongitude();
        String dest = "destination=" + rInfo.latitude + "," + rInfo.longitude;
        String sensor = "sensor=false";
        String url = "https://maps.googleapis.com/maps/api/directions/json?" + origin + "&" + dest + "&" + sensor;
        return url;
    }

    private InputStream getStream(String strUrl){
        try{
            URL url = new URL(strUrl);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(1000);
            return  urlConnection.getInputStream();
        }catch(Exception e){
            return null;
        }
    }
}
