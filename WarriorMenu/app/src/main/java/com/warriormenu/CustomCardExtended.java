package com.warriormenu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.net.Uri;
import android.provider.UserDictionary;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cengalabs.flatui.views.FlatButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by vannguyen on 4/22/14.
 */
public class CustomCardExtended extends CustomCard {
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
    protected TextView resCuisine;
    protected TextView resPrice;
    public ListView cardList;
    public ArrayList<Card> cards;
    public CommentNewCard commentNewCard;

    public CustomCardExtended(Context context, int innerLayout, RInfo r, Typeface t, Typeface t2, Location lc){
        super(context, innerLayout, r ,t , t2, lc);
        this.info = r;
        this.typeface = t;
        this.typeface2 = t2;
        this.location = lc;
    }

    public CustomCardExtended(Context context, RInfo r, Typeface t, Typeface t2, Location lc){
        this(context, R.layout.card_thumbnail_extended, r,t, t2, lc);
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view){
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        Map<Integer, String> dayMapping = new HashMap<Integer, String>();

        String[] days = {"Sunday", "Monday","Tuesday",
                "Wednesday", "Thursday", "Friday", "Saturday"};
        for (int i = 0; i < days.length; i++) {
            dayMapping.put(i, days[i]);
        }
        resTitle = (TextView) parent.findViewById(R.id.card_title_extended);
        resImage = (ImageView) parent.findViewById(R.id.card_picture_extended);
        resAddress = (TextView) parent.findViewById(R.id.card_address_extended);
        resNumber = (TextView) parent.findViewById(R.id.card_number_extended);
        warriorDollars = (TextView) parent.findViewById(R.id.card_dollar_extended);
        resHours = (TextView) parent.findViewById(R.id.card_hours_extended);
        ratingBar = (RatingBar) parent.findViewById(R.id.card_stars_extended);
        openClose = (TextView) parent.findViewById(R.id.card_open_close_extended);
        button = (FlatButton) parent.findViewById(R.id.card_button_extended);
        cardList = (CardListView) parent.findViewById(R.id.card_extended_listView);
        resCuisine = (TextView) parent.findViewById(R.id.card_cuisine_extended);
        resPrice = (TextView) parent.findViewById(R.id.card_price_extended);


        String stringDay = dayMapping.get(date.getDay());
        Day dayHours = info.days.get(stringDay.toLowerCase());


        if(resTitle != null){
            resTitle.setText(info.name);
            resTitle.setTypeface(typeface2);
        }

        if(resImage != null) {
            resImage.setImageResource(info.idPicture);
        }

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

        if(resHours != null){
            String hours = new String();
            hours += "Hours: ";
            /*
            if(dayHours.isClosed()) {
                hours = "Closed Today.";
            } else if(dayHours.is24Hour()) {
                hours = "Open 24 hours.";
            } else {
                hours = "Hours: " + dayHours.openTimeString() + " - " + dayHours.closeTimeString();
            }*/

            for(int i = 0; i < 7; i++){
                Day hour = info.days.get(days[i].toLowerCase());

                hours = hours +"\n"+ days[i] + ": ";
                if(hour.isClosed()) {
                    hours += "Closed";
                } else if(dayHours.is24Hour()) {
                    hours += "Open 24 hours.";
                } else {
                    hours = hours + hour.openTimeString() + " - " + hour.closeTimeString();
                }
            }

            resHours.setText(hours);
            resHours.setTypeface(typeface);
        }

        if(ratingBar != null){
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
            button.setText("Directions");
            button.setTypeface(typeface);
            info.url = "http://maps.google.com/maps?saddr=" + location.getLatitude() + "," + location.getLongitude();
            info.url = info.url + "&daddr=" + info.name + "+"+info.address;
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(Intent.ACTION_VIEW,  Uri.parse(info.url));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                }
            });
        }

        if(cardList != null){
            cards = new ArrayList<Card>();
            double temp = info.rating*10;
            for(int i = 0; i < info.comments.size(); i++){
                Comment comment = info.comments.get(i);
                temp += info.comments.get(i).rating;
                CommentCard card = new CommentCard(mContext, comment.name, comment.comment, comment.rating);
                card.setBackgroundResourceId(R.color.lightgreytan);
                cards.add(card);
            }
            info.avgRating = temp/(10.0+info.comments.size());
            CardArrayAdapter adapter = new CardArrayAdapter(mContext, cards);
            cardList.setAdapter(adapter);
        }

        if(resCuisine != null){
            resCuisine.setText("Cuisine: " + info.cuisine);
            resCuisine.setTypeface(typeface);
        }

        if(resPrice != null){
            String p = "Price: " + info.price;
            resPrice.setText(p);
            resPrice.setTypeface(typeface);
        }

    }
    public int imageID(Context context, String url){
        return context.getResources().getIdentifier("drawable/" + url, null, context.getPackageName());
    }

    public void addNewComment(Card card){
        cards.add(card);
        CardArrayAdapter adapter = new CardArrayAdapter(mContext, cards);
        cardList.setAdapter(adapter);
    }
}
