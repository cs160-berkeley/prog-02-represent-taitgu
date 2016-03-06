package com.example.taitgu.represent;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText zipcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //from http://stackoverflow.com/questions/8063439/android-edittext-finished-typing-event
        //when the zipcode is entered load it in
        ((EditText) findViewById(R.id.zipcode)).setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (!event.isShiftPressed()) {
                                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                                System.out.println(((EditText) findViewById(R.id.zipcode)).getText());
                                sendIntent.putExtra("ZIPCODE", ((EditText) findViewById(R.id.zipcode)).getText().toString());
                                startService(sendIntent);

                                Intent intent = new Intent(MainActivity.this, Congressional.class);
                                intent.putExtra("ZIPCODE", ((EditText) findViewById(R.id.zipcode)).getText().toString());
                                startActivity(intent);
                            }
                        }
                        return false; // pass on to other listeners.
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadCongress(View view)
    {
        //send intent to watch
        Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        sendIntent.putExtra("ZIPCODE", "94704");
        startService(sendIntent);

        //send intent to congressional class
        Intent intent = new Intent(MainActivity.this, Congressional.class);
        startActivity(intent);
    }
}
