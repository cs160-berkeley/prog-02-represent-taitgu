package com.example.taitgu.represent;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

public class Vote extends Activity {
    private TextView zipcode;
    private JSONArray data;
    private double obama;
    private double romney;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        String county = "";
        zipcode = (TextView) findViewById(R.id.textView);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            county = extras.getString("ZIPCODE");
            zipcode.setText(county);
        }


        try {
            InputStream stream = getAssets().open("election-county-2012.json");
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String jsonString = new String(buffer, "UTF-8");
            data = new JSONArray(jsonString);
            for (int i = 0; i< data.length(); i++){
                JSONObject c = (JSONObject)data.get(i);
                if (c.getString("county-name").equals(county)){
                    obama = c.getDouble("obama-percentage");
                    romney = c.getDouble("romney-percentage");
                }
            }
        } catch(Exception E){
            System.out.println("Couldn't load file");
        }


        ((TextView) findViewById(R.id.textView2)).setText("Obama: " + obama);
        ((TextView) findViewById(R.id.textView3)).setText("Romney: " + romney);

    }

}
