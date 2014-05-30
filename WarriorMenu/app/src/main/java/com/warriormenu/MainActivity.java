package com.warriormenu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Vector;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView mainText = (TextView) findViewById(R.id.title_main);
        TextView categoryText = (TextView) findViewById(R.id.categories_title);
        GlobalApp globalApp = (GlobalApp) getApplicationContext();
        mainText.setTypeface(globalApp.getTypeface());
        categoryText.setTypeface(globalApp.getTypeface2());
        Button button1 = (Button) findViewById(R.id.american_button);
        Button button2 = (Button) findViewById(R.id.italian_button);
        Button button3 = (Button) findViewById(R.id.asian_button);
        Button button4 = (Button) findViewById(R.id.med_button);
        Button button5 = (Button) findViewById(R.id.coffee_button);
        Button button6 = (Button) findViewById(R.id.affordable_button);
        Button button7 = (Button) findViewById(R.id.warriorDollar_button);
        Button button8 = (Button) findViewById(R.id.open_button);
        Button button9 = (Button) findViewById(R.id.latino_button);
        Button button10 = (Button) findViewById(R.id.all_button);
        button1.setTypeface(globalApp.getTypeface2());
        button2.setTypeface(globalApp.getTypeface2());
        button3.setTypeface(globalApp.getTypeface2());
        button4.setTypeface(globalApp.getTypeface2());
        button5.setTypeface(globalApp.getTypeface2());
        button6.setTypeface(globalApp.getTypeface2());
        button7.setTypeface(globalApp.getTypeface2());
        button8.setTypeface(globalApp.getTypeface2());
        button9.setTypeface(globalApp.getTypeface2());
        button10.setTypeface(globalApp.getTypeface2());
        button1.setTextColor(getResources().getColor(R.color.wayne_yellow));
        button3.setTextColor(getResources().getColor(R.color.wayne_yellow));
        button5.setTextColor(getResources().getColor(R.color.wayne_yellow));
        button7.setTextColor(getResources().getColor(R.color.wayne_yellow));
        button9.setTextColor(getResources().getColor(R.color.wayne_yellow));
        button10.setTextColor(getResources().getColor(R.color.wayne_yellow));

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        button10.setOnClickListener(this);

    }

    @Override
    public void onClick(View view){
        int categoryPicked = 0;
        switch (view.getId()){
            case R.id.american_button:
                categoryPicked = 1;
            break;
            case R.id.italian_button:
                categoryPicked = 2;
            break;
            case R.id.asian_button:
                categoryPicked = 3;
            break;
            case R.id.med_button:
                categoryPicked = 4;
            break;
            case R.id.coffee_button:
                categoryPicked = 5;
            break;
            case R.id.affordable_button:
                categoryPicked = 6;
            break;
            case R.id.warriorDollar_button:
                categoryPicked = 7;
            break;
            case R.id.open_button:
                categoryPicked = 8;
            break;
            case R.id.latino_button:
                categoryPicked = 9;
            break;
        }

        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        intent.putExtras(bundling(categoryPicked));
        startActivity(intent);
    }


    public Bundle bundling(int c){
        Bundle b = new Bundle();
        b.putInt("c", c);
        return b;
    }
}
