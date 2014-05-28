package com.warriormenu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Timer;
import java.util.TimerTask;


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        GlobalApp globalApp = (GlobalApp) getApplicationContext();
        globalApp.setTypefaces(Typeface.createFromAsset(getAssets(), "Roboto-LightItalic.ttf"),Typeface.createFromAsset(getAssets(), "Roboto-BoldCondensedItalic.ttf"));

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 5000);

    }

    private void finishActivity(){
        this.finish();
    }

}
