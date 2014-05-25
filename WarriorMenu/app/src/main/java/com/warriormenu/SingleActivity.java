package com.warriormenu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.CardView;

public class SingleActivity extends Activity {

private Location myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        myLocation = new Location("temp");
        RInfo r = unBundling(this.getIntent().getExtras());
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Roboto-LightItalic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "Roboto-BoldCondensedItalic.ttf");
        final TextView mainTitle = (TextView) findViewById(R.id.main_textView1);
        mainTitle.setTypeface(typeface);

        ArrayList<Card> cards = new ArrayList<Card>();
        CustomCardExtended card = new CustomCardExtended(getApplicationContext(), r, typeface, typeface2, myLocation);
        cards.add(card);
        CardArrayAdapter myAdapter = new CardArrayAdapter(getApplicationContext(), cards);
        ListView listView = (CardListView) findViewById(R.id.myList_single);
        if(listView != null)
            listView.setAdapter(myAdapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public RInfo unBundling(Bundle b){
        String json = b.getString("info");
        Type type = new TypeToken<ArrayList<RInfo>>(){}.getType();
        ArrayList<RInfo> arrayList = new Gson().fromJson(json, type);
        Double latitude = b.getDouble("lat");
        Double longitude = b.getDouble("long");
        myLocation.setLatitude(latitude);
        myLocation.setLongitude(longitude);
        return arrayList.get(0);
    }

}
