package com.warriormenu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cengalabs.flatui.views.FlatButton;

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
public class CommentCard extends Card {
    protected TextView commentUser;
    protected TextView commentComment;
    protected RatingBar ratingBar;
    protected Typeface typeface;
    protected Typeface typeface2;


    public CommentCard(Context context){
        this(context, R.layout.card_comment);
    }

    public CommentCard(Context context, int innerLayout){
        super(context, innerLayout);
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view){
        commentUser = (TextView) parent.findViewById(R.id.card_comment_username);
        commentComment = (TextView) parent.findViewById(R.id.card_comment_comment);
        ratingBar = (RatingBar) parent.findViewById(R.id.card_comment_stars);

        if(commentUser != null){
            commentUser.setText("Van Nguyen");
        }

        if(commentComment != null){
            commentComment.setText("Very delicious pies! The service was top notch! I would go there everyday if I didn't gain half a pound everytime I go there.");
        }

        if(ratingBar != null){
            LayerDrawable layer = (LayerDrawable) ratingBar.getProgressDrawable();
            layer.getDrawable(2).setColorFilter(Color.parseColor("#0b4f45"), PorterDuff.Mode.SRC_ATOP);
        }

    }
}
