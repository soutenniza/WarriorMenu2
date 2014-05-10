package com.warriormenu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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
    protected RInfo info;
    protected Typeface typeface;

    public CustomCard(Context context, RInfo r, Typeface t){
        this(context, R.layout.card_thumbnail, r,t);
    }

    public CustomCard(Context context, int innerLayout, RInfo r, Typeface t){
        super(context, innerLayout);
        this.info = r;
        this.typeface = t;
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view){
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        Map<Integer, String> dayMapping = new HashMap<Integer, String>();

        String[] days = {"sunday", "monday","tuesday",
                "wednesday", "thursday", "friday", "saturday"};
        for (int i = 0; i < days.length; i++) {
            dayMapping.put(i+1, days[i]);
        }
        resTitle = (TextView) parent.findViewById(R.id.card_title);
        resImage = (ImageView) parent.findViewById(R.id.card_picture);
        resAddress = (TextView) parent.findViewById(R.id.card_address);
        resNumber = (TextView) parent.findViewById(R.id.card_number);
        warriorDollars = (TextView) parent.findViewById(R.id.card_dollar);
        resHours = (TextView) parent.findViewById(R.id.card_hours);
        ratingBar = (RatingBar) parent.findViewById(R.id.card_stars);
        openClose = (TextView) parent.findViewById(R.id.card_open_close);

        String stringDay = dayMapping.get(date.getDay());
        Day dayHours = info.days.get(stringDay);

        if(resTitle != null){
            resTitle.setText(info.name);
            resTitle.setTypeface(typeface);
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

    }
}
