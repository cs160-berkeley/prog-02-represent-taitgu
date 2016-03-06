package com.example.taitgu.represent;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;

public class Vote extends Activity {
    private TextView zipcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        zipcode = (TextView) findViewById(R.id.textView);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            zipcode.setText(extras.getString("ZIPCODE"));
        }

        if (!zipcode.getText().toString().equals("94704")){
            ((TextView) findViewById(R.id.textView2)).setText("Obama: 53%");
            ((TextView) findViewById(R.id.textView3)).setText("Romney: 47%");
        }
    }

}
