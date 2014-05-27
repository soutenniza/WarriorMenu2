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

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by vannguyen on 4/22/14.
 */
public class CommentNewCard extends Card {
    public EditText nameInput;
    public EditText reviewInput;
    public RatingBar ratingBar;
    public Button submitButton;
    protected Typeface typeface;
    protected Typeface typeface2;
    public String name;
    public String review;
    public float rating;
    public View.OnClickListener onClickListener;


    public CommentNewCard(Context context, View.OnClickListener oc){
        this(context);
        this.onClickListener = oc;
    }

    public CommentNewCard(Context context){
        this(context, R.layout.card_comment_new);
    }

    public CommentNewCard(Context context, int innerLayout){
        super(context, innerLayout);
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view){
        nameInput = (EditText) parent.findViewById(R.id.card_comment_name_new);
        reviewInput = (EditText) parent.findViewById(R.id.card_comment_comment_new);
        ratingBar = (RatingBar) parent.findViewById(R.id.card_comment_stars_new);
        submitButton = (FlatButton) parent.findViewById(R.id.card_comment_submit);

        if(submitButton != null){
            submitButton.setOnClickListener(onClickListener);
        }

        if(ratingBar != null){
            LayerDrawable layer = (LayerDrawable) ratingBar.getProgressDrawable();
            layer.getDrawable(2).setColorFilter(Color.parseColor("#0b4f45"), PorterDuff.Mode.SRC_ATOP);
        }
    }

}