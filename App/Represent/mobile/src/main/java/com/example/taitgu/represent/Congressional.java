package com.example.taitgu.represent;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
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

        zipcode = (TextView) findViewById(R.id.location);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        System.out.println("work yes?");
        if (extras != null) {
            System.out.println("WHY WORK NOT");
            zipcode.setText(extras.getString("ZIPCODE"));
        }

    }

    public void loadDetailed(View view)
    {
        Intent intent = new Intent(Congressional.this, Detailed.class);
        startActivity(intent);
    }

}
