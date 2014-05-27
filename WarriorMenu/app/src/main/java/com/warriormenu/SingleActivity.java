package com.warriormenu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cengalabs.flatui.views.FlatButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.CardView;

public class SingleActivity extends Activity{

    private Location myLocation;
    private CustomCardExtended customCardExtended;
    private TextView mainTitle;
    public RInfo r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        myLocation = new Location("temp");
        r = unBundling(this.getIntent().getExtras());
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Roboto-LightItalic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "Roboto-BoldCondensedItalic.ttf");
        mainTitle = (TextView) findViewById(R.id.main_textView1);
        mainTitle.setTypeface(typeface);

        r.names.add("Van");
        r.reviews.add("I love to eat pies all day even though I cant move any part of of my body. How did I type this?");
        r.ratings.add(1.f);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ArrayList<Card> cards = new ArrayList<Card>();
        customCardExtended = new CustomCardExtended(getApplicationContext(), r, typeface, typeface2, myLocation);
        CardView cardView = (CardView) findViewById(R.id.card_single);
        cardView.setCard(customCardExtended);

        CommentNewCard commentNewCard = new CommentNewCard(getApplicationContext(), r);
        commentNewCard.setBackgroundResourceId(R.color.lightgreytan);
        customCardExtended.addNewComment(commentNewCard);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(SingleActivity.this, MenuActivity.class);
        intent.putExtras(bundling(r));
        startActivity(intent);
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

    public Bundle bundling(RInfo rInfo){
        Bundle b = new Bundle();
        Gson gson = new Gson();
        ArrayList<RInfo> arrayList = new ArrayList<RInfo>();
        arrayList.add(rInfo);
        String json = gson.toJson(arrayList);
        b.putString("info", json);
        b.putDouble("lat", myLocation.getLatitude());
        b.putDouble("long", myLocation.getLongitude());
        return b;
    }
}