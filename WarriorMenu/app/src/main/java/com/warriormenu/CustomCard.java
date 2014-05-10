package com.warriormenu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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
            String stringDay = dayMapping.get(date.getDay());
            Day dayHours = info.days.get(stringDay);
            int currentHour = date.getHours()*100;
            if((dayHours.open < currentHour && dayHours.close > currentHour) || dayHours.open == 0000 ){
                openClose.setText("Open Now!");
                openClose.setTextColor(Color.parseColor("#009900"));
            } else{
                openClose.setText("Closed.");
                openClose.setTextColor(Color.parseColor("#D11919"));
            }

            if(dayHours.open == 9999){
                openClose.setText("Closed.");
                openClose.setTextColor(Color.parseColor("#D11919"));
            }

        }

        if(resHours != null){
            String stringDay = dayMapping.get(date.getDay());
            Day dayHours = info.days.get(stringDay);
            String hours = "Hours: ";
            String temp;
            if(dayHours.open > 999)
                if(dayHours.open > 1200){
                    int i = dayHours.open - 1200;
                    temp = Integer.toString(i);
                    hours = hours + temp.charAt(0) + ":" + temp.charAt(1) + temp.charAt(2) + "PM";
                }else if(dayHours.open == 9999)
                hours = "Closed Today";
                else {
                    int i = dayHours.open;
                    temp = Integer.toString(i);
                    hours = hours + temp.charAt(0) + temp.charAt(1)+ ":" + temp.charAt(2) + temp.charAt(3) + "AM";
                }
            else if (dayHours.open == 0000){
                hours = "Open 24 Hours";
            }else{
                temp = Integer.toString(dayHours.open);
                hours = hours + temp.charAt(0) + ":" + temp.charAt(1) + temp.charAt(2) + "AM";
            }
            if(dayHours.close == 9999)
                hours += "";
            else if(dayHours.close == 0000)
                hours += "";
            else if(dayHours.close > 999){
                hours += " - ";
                if(dayHours.close > 1200){
                    if(dayHours.close > 2400){
                        int i = dayHours.close - 2400;
                        temp = Integer.toString(i);
                        hours = hours + temp.charAt(0) + ":" + temp.charAt(1) + temp.charAt(2) + "AM";
                    }else{
                      int i = dayHours.close - 1200;
                      temp = Integer.toString(i);
                      hours = hours + temp.charAt(0) + ":" + temp.charAt(1) + temp.charAt(2) + "PM";
                    }
                } else {
                    int i = dayHours.close;
                    temp = Integer.toString(i);
                    hours = hours + temp.charAt(0) + temp.charAt(1)+ ":" + temp.charAt(2) + temp.charAt(3) + "AM";
                }
            }else{
                hours += " - ";
                temp = Integer.toString(dayHours.close);
                hours = hours + temp.charAt(0) + ":" + temp.charAt(1) + temp.charAt(2) + "AM";
            }

            //hours = hours + " - " + temp.charAt(0) + temp.charAt(1) + ":" + temp.charAt(2) + temp.charAt(3);
            resHours.setText(hours);
            resHours.setTypeface(typeface);
        }


        if(ratingBar != null){
            ratingBar.setNumStars(5);
            ratingBar.setRating((float)info.rating);
        }

    }
}
