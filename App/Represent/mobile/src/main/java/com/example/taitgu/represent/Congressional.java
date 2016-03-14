package com.example.taitgu.represent;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Congressional extends AppCompatActivity {
    private TextView zipcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        zipcode = (TextView) findViewById(R.id.zipcode);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {

            zipcode.setText("Results:");

            //person1
            String[] person1 = extras.getStringArray("PERSON1");
            if (person1[0]==null){
                LinearLayout p1 = (LinearLayout)findViewById(R.id.person1);
                p1.setVisibility(View.INVISIBLE);
            } else {
                ((TextView)findViewById(R.id.name1)).setText(person1[0]);
                ((TextView)findViewById(R.id.party1)).setText(person1[1]);
                ((TextView)findViewById(R.id.email1)).setText(person1[2]);
                ((TextView)findViewById(R.id.website1)).setText(person1[3]);
            }

            //person2
            String[] person2 = extras.getStringArray("PERSON2");
            if (person2[0]==null){
                LinearLayout p2 = (LinearLayout)findViewById(R.id.person2);
                p2.setVisibility(View.INVISIBLE);
            } else {
                ((TextView)findViewById(R.id.name2)).setText(person2[0]);
                ((TextView)findViewById(R.id.party2)).setText(person2[1]);
                ((TextView)findViewById(R.id.email2)).setText(person2[2]);
                ((TextView)findViewById(R.id.website2)).setText(person2[3]);
            }

            //person3
            String[] person3 = extras.getStringArray("PERSON3");
            if (person3[0]==null){
                LinearLayout p3 = (LinearLayout)findViewById(R.id.person3);
                p3.setVisibility(View.INVISIBLE);
            } else {
                ((TextView)findViewById(R.id.name3)).setText(person3[0]);
                ((TextView)findViewById(R.id.party3)).setText(person3[1]);
                ((TextView)findViewById(R.id.email3)).setText(person3[2]);
                ((TextView)findViewById(R.id.website3)).setText(person3[3]);
            }

            //person4
            String[] person4 = extras.getStringArray("PERSON4");
            if (person4[0]==null){
                LinearLayout p4 = (LinearLayout)findViewById(R.id.person4);
                p4.setVisibility(View.INVISIBLE);
            } else {
                ((TextView)findViewById(R.id.name4)).setText(person4[0]);
                ((TextView)findViewById(R.id.party4)).setText(person4[1]);
                ((TextView)findViewById(R.id.email4)).setText(person4[2]);
                ((TextView)findViewById(R.id.website4)).setText(person4[3]);
            }

            //person5
            String[] person5 = extras.getStringArray("PERSON5");
            if (person5[0]==null){
                LinearLayout p5 = (LinearLayout)findViewById(R.id.person5);
                p5.setVisibility(View.INVISIBLE);
            } else {
                ((TextView)findViewById(R.id.name5)).setText(person5[0]);
                ((TextView)findViewById(R.id.party5)).setText(person5[1]);
                ((TextView)findViewById(R.id.email5)).setText(person5[2]);
                ((TextView)findViewById(R.id.website5)).setText(person5[3]);
            }
        }

    }

    public void loadDetailed(View view)
    {
        Intent intent = new Intent(Congressional.this, Detailed.class);
        startActivity(intent);
    }

}
