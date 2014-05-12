package com.warriormenu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    protected LocationManager locationManager;

    public CustomCard(Context context, RInfo r, Typeface t, Typeface t2, LocationManager lc){
        this(context, R.layout.card_thumbnail, r,t, t2, lc);
    }

    public CustomCard(Context context, int innerLayout, RInfo r, Typeface t, Typeface t2, LocationManager lc){
        super(context, innerLayout);
        this.info = r;
        this.typeface = t;
        this.typeface2 = t2;
        this.locationManager = lc;
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
        resAddress = (TextView) parent.findViewById(R.id.card_address);
        resNumber = (TextView) parent.findViewById(R.id.card_number);
        warriorDollars = (TextView) parent.findViewById(R.id.card_dollar);
        resHours = (TextView) parent.findViewById(R.id.card_hours);
        ratingBar = (RatingBar) parent.findViewById(R.id.card_stars);
        openClose = (TextView) parent.findViewById(R.id.card_open_close);
        resDistance = (TextView) parent.findViewById(R.id.card_distance);

        String stringDay = dayMapping.get(date.getDay());
        Day dayHours = info.days.get(stringDay);


        if(resTitle != null){
            resTitle.setText(info.name);
            resTitle.setTypeface(typeface2);
        }

        if(resImage != null)
            resImage.setImageResource(R.drawable.jimmyjohns);

        if(resAddress != null){
            resAddress.setText(info.address);
            resAddress.setTypeface(typeface);
        }

        if(resNumber != null){
            resNumber.setText(info.number);
            resNumber.setTypeface(typeface);
        }

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
            } else{
                openClose.setText("Closed.");
                openClose.setTextColor(Color.parseColor("#D11919"));
            }

            if(dayHours.isClosed()){
                openClose.setText("Closed.");
                openClose.setTextColor(Color.parseColor("#D11919"));
            }

        }

        if(resHours != null){
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
        }

        if(ratingBar != null){
            ratingBar.setNumStars(5);
            ratingBar.setRating((float)info.rating);
        }

        if(resDistance != null){
            float[] results = new float[10];
            long l = 10000;
            final LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, l, 100000f, locationListener);
            Location location = new Location("");
            /*location.setLatitude(42.361525);
            location.setLongitude(-83.069586);*/
            Location.distanceBetween(location.getLatitude(), location.getLongitude(), info.latitude, info.longitude, results);
            float temp = results[0] * 0.0006213f;
            String str = String.format("%.3f", temp);
            resDistance.setText("Distance: " + str + " miles");
            resDistance.setTypeface(typeface);

            info.url = "http://maps.google.com/maps?saddr=" + location.getLatitude() + "," + location.getLongitude();
            info.url = info.url + "&daddr=" + info.latitude + "," + info.longitude;
            resDistance.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    resTitle.setText("Clicked!");
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse(info.url));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                }
            });

        }

    }
}
