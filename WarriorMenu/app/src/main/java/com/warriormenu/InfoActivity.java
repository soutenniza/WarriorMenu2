package com.warriormenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class InfoActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        GlobalApp globalApp = (GlobalApp) getApplicationContext();

        TextView libView = (TextView) findViewById(R.id.lib_msg);
        TextView wlpView = (TextView) findViewById(R.id.wlp_msg);
        TextView title = (TextView) findViewById(R.id.info_title);
        title.setTypeface(globalApp.getTypeface());
        libView.setTypeface(globalApp.getTypeface());
        wlpView.setTypeface(globalApp.getTypeface());

    }


    public Bundle bundling(int c){
        Bundle b = new Bundle();
        b.putInt("c", c);
        return b;
    }
}
