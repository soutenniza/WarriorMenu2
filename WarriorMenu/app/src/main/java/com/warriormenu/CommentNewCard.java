package com.warriormenu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cengalabs.flatui.views.FlatButton;
import com.cengalabs.flatui.views.FlatEditText;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by vannguyen on 4/22/14.
 */
public class CommentNewCard extends Card{
    public EditText nameInput;
    public FlatEditText reviewInput;
    public RatingBar ratingBar;
    public Button submitButton;
    protected Typeface typeface;
    protected Typeface typeface2;
    public String name;
    public String review;
    public float rating;
    public RInfo info;


    public CommentNewCard(Context context, RInfo rInfo){
        this(context, R.layout.card_comment_new, rInfo);
    }

    public CommentNewCard(Context context, int innerLayout, RInfo rInfo){
        super(context, innerLayout);
        this.info = rInfo;
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view){
        nameInput = (EditText) parent.findViewById(R.id.card_comment_name_new);
        reviewInput = (FlatEditText) parent.findViewById(R.id.card_comment_comment_new);
        ratingBar = (RatingBar) parent.findViewById(R.id.card_comment_stars_new);
        submitButton = (FlatButton) parent.findViewById(R.id.card_comment_submit);

        if(submitButton != null){
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Comment comment = new Comment();
                    comment.name = nameInput.getText().toString();
                    comment.rating = ratingBar.getRating();
                    comment.comment = reviewInput.getText().toString();
                    info.comments.add(comment);

                    //info.names.add(nameInput.getText().toString());
                    //info.reviews.add(reviewInput.getText().toString());
                    //info.ratings.add(ratingBar.getRating());
                }
            });
        }
        if(ratingBar != null){
            LayerDrawable layer = (LayerDrawable) ratingBar.getProgressDrawable();
            layer.getDrawable(2).setColorFilter(Color.parseColor("#0b4f45"), PorterDuff.Mode.SRC_ATOP);
        }
    }

}
