package com.warriormenu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cengalabs.flatui.views.FlatButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.CardView;

public class SingleActivity extends Activity{

    private Location myLocation;
    private TextView mainTitle;
    public int r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        myLocation = new Location("temp");
        r = unBundling(this.getIntent().getExtras());
        mainTitle = (TextView) findViewById(R.id.main_textView1);
        final GlobalApp globalApp = (GlobalApp) getApplicationContext();
        mainTitle.setTypeface(globalApp.getTypeface());
        final EditText editName = (EditText) findViewById(R.id.card_single_name);
        final EditText editComment = (EditText) findViewById(R.id.card_single_comment);
        final RatingBar ratingBar = (RatingBar) findViewById(R.id.card_single_stars);
        final Button button = (Button) findViewById(R.id.card_single_submit);
        LayerDrawable layer = (LayerDrawable) ratingBar.getProgressDrawable();
        layer.getDrawable(2).setColorFilter(Color.parseColor("#ffcc33"), PorterDuff.Mode.SRC_ATOP);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ArrayList<Card> cards = new ArrayList<Card>();
        final CustomCardExtended customCardExtended = new CustomCardExtended(getApplicationContext(), globalApp.getRestaurants().get(r), globalApp.getTypeface(), globalApp.getTypeface2(), globalApp.getLocation());
        CardView cardView = (CardView) findViewById(R.id.card_single);
        cardView.setCard(customCardExtended);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment comment = new Comment();
                comment.name = editName.getText().toString();
                comment.rating = ratingBar.getRating();
                comment.comment = editComment.getText().toString();
                globalApp.getRestaurants().get(r).comments.add(comment);
                URL url;
                URLConnection urlConn;
                try {

                    url = new URL(globalApp.getURL() + "/restaurants/" + Integer.toString(globalApp.getRestaurants().get(r).restID+1) + "/comments");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.connect();
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("name", comment.name);
                    jsonParam.put("rating", comment.rating);
                    jsonParam.put("comment", comment.comment);
                    DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
                    wr.writeBytes(jsonParam.toString());
                    wr.flush();
                    wr.close();
                    connection.disconnect();
                    String line;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    while ((line = reader.readLine()) != null) {
                    }
                    reader.close();
                } catch (IOException e){
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                CommentCard card = new CommentCard(getApplicationContext(), comment.name, comment.comment, comment.rating);
                card.setBackgroundResourceId(R.color.lightgreytan);
                customCardExtended.addNewComment(card);

                editName.setHint("Name");
                editComment.setHint("Your Review");
                button.setText("Submitted");
                button.setClickable(false);
            }
        });

    }


    public int unBundling(Bundle b){
        Double latitude = b.getDouble("lat");
        Double longitude = b.getDouble("long");
        myLocation.setLatitude(latitude);
        myLocation.setLongitude(longitude);
        return b.getInt("info");
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