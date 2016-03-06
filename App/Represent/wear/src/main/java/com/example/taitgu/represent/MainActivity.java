package com.example.taitgu.represent;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends WearableActivity {

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    private TextView zipcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setAmbientEnabled();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            setContentView(R.layout.activity_main);
            zipcode = (TextView) findViewById(R.id.zipcode);
            zipcode.setText(extras.getString("ZIPCODE"));
        }
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {

    }

    public void loadVote(View view)
    {
        Intent intent = new Intent(MainActivity.this, Vote.class);
        intent.putExtra("ZIPCODE",  zipcode.getText().toString());
        startActivity(intent);
    }

    public void loadDetailed(View view){
        System.out.println("loading detailed view");
        Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
        startService(sendIntent);
    }
}
