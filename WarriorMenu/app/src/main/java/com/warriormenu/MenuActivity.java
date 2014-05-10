package com.warriormenu;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Vector;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.CardView;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Vector<RInfo> restaurants = intRests();

        Typeface typeface = Typeface.createFromAsset(getAssets(), "Roboto-LightItalic.ttf");
        TextView mainTitle = (TextView) findViewById(R.id.main_textView1);
        mainTitle.setTypeface(typeface);
        ArrayList<Card> cards = new ArrayList<Card>();

        for(int i = 0; i < restaurants.size();i++){
           CustomCard card = new CustomCard(getBaseContext(), restaurants.get(i), typeface);
            card.setShadow(true);
            card.setSwipeable(true);
           cards.add(card);
        }

        CardArrayAdapter myAdapter = new CardArrayAdapter(getApplicationContext(),cards);

        CardListView listView = (CardListView) findViewById(R.id.myList);
        if (listView!=null){
            listView.setAdapter(myAdapter);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

    private String inputStreamToString(InputStream is) throws IOException {
        StringBuffer sBuffer = new StringBuffer();
        DataInputStream dataIO = new DataInputStream(is);
        String strLine = null;
        while((strLine = dataIO.readLine())  != null){
            sBuffer.append(strLine + "\n");
        }

        dataIO.close();
        return sBuffer.toString();
    }

    private Vector<RInfo> intRests(){
        Vector<RInfo> temp = new Vector<RInfo>();
        String array;
        String[] infoArray;
        String strFile = null;
        InputStream inFile = getResources().openRawResource(R.raw.restaurants_info);
        try{
            strFile = inputStreamToString(inFile);
        } catch (IOException e){
            e.printStackTrace();
        }
        array = strFile;
        infoArray = array.split("\n");
        int numRest = infoArray.length/21;
        for(int j = 0; j < numRest;j++){
            RInfo test = new RInfo();
            int i = 21*(j);
            test.name = infoArray[i];
            test.address = infoArray[i+1];
            test.lat = Double.parseDouble(infoArray[i+2]);
            test.longitude = Double.parseDouble(infoArray[i+3]);
            test.warriorD = Integer.parseInt(infoArray[i+4]);
            test.hour[0] = Integer.parseInt(infoArray[i+5]); //sundayOpen
            test.hour[1] = Integer.parseInt(infoArray[i+6]); //sundayClose
            test.hour[2] = Integer.parseInt(infoArray[i+7]); //monday
            test.hour[3] = Integer.parseInt(infoArray[i+8]);
            test.hour[4] = Integer.parseInt(infoArray[i+9]); //tuesday
            test.hour[5] = Integer.parseInt(infoArray[i+10]);
            test.hour[6] = Integer.parseInt(infoArray[i+11]); //wednesday
            test.hour[7] = Integer.parseInt(infoArray[i+12]);
            test.hour[8] = Integer.parseInt(infoArray[i+13]); //thursday
            test.hour[9] = Integer.parseInt(infoArray[i+14]);
            test.hour[10] = Integer.parseInt(infoArray[i+15]); //friday
            test.hour[11] = Integer.parseInt(infoArray[i+16]);
            test.hour[12] = Integer.parseInt(infoArray[i+17]); //saturday
            test.hour[13] = Integer.parseInt(infoArray[i+18]);
            test.rating = Float.parseFloat(infoArray[i + 19]);
            test.number = infoArray[i+20];
            temp.add(test);
        }
        return temp;
    }


}
