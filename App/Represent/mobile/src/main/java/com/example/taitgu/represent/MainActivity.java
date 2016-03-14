package com.example.taitgu.represent;

import android.content.Intent;
import android.hardware.SensorManager;
import android.location.Location;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private EditText zipcode;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private String latitude;
    private String longitude;
    private String genLat;
    private String genlong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

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
                                String zip = ((EditText) findViewById(R.id.zipcode)).getText().toString();
                                findGeocode(zip);
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
        final String googlelink = "https://maps.googleapis.com/maps/api/geocode/json?latlng="
                +latitude + "," + longitude
                +"&key=AIzaSyB64SFX7NazuI8M9w6k9THgeYGqWZ22-9g";
        String link = "https://congress.api.sunlightfoundation.com/legislators/locate?"+
                "latitude=" + latitude +
                "&longitude=" + longitude +
                "&apikey=05b337727dd142f4bf97be8994e6df46";


        Ion.with(MainActivity.this)
                .load(link)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(result != null) {

                            Ion.with(MainActivity.this)
                                    .load(googlelink)
                                    .asJsonObject()
                                    .setCallback(new FutureCallback<JsonObject>() {
                                        @Override
                                        public void onCompleted(Exception e, JsonObject result2) {
                                            // do stuff with the result or error
                                            String county = "";

                                            JsonArray r = (JsonArray)result2.get("results");
                                            JsonArray addrcomp = (JsonArray)((JsonObject)r.get(0)).get("address_components");
                                            for(int i = 0; i < addrcomp.size();i++){
                                                JsonObject obj = (JsonObject)addrcomp.get(i);
                                                JsonArray types = (JsonArray)obj.get("types");

                                                if (types.get(0).getAsString().equals("administrative_area_level_2")){
                                                    county = obj.get("long_name").getAsString();
                                                }
                                            }

                                            if(county.length()>7 && county.substring(county.length()-7,county.length()).equals(" County")){
                                                county = county.substring(0,county.length()-7);
                                            }
                                            Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                                            sendIntent.putExtra("ZIPCODE", county);
                                            startService(sendIntent);
                                        }
                                    });

                            String[][] toSend = new String[5][4];
                            JsonArray r = (JsonArray)result.get("results");
                            for (int i = 0; i<r.size() && i<5; i++){
                                JsonObject person = (JsonObject)r.get(i);

                                //get name
                                String name = "";
                                JsonPrimitive chamber = (JsonPrimitive)person.get("chamber");
                                if(chamber.getAsString().equals("house")){
                                    name += "Rep. ";
                                } else {
                                    name += "Senator ";
                                }
                                name += person.get("first_name").getAsString();
                                name += " ";
                                name += person.get("last_name").getAsString();
                                toSend[i][0] = name;

                                //get party
                                String party = person.get("party").getAsString();
                                if(party.equals("D")){
                                    toSend[i][1] = "Democratic Party";
                                } else if (party.equals("R")){
                                    toSend[i][1] = "Republican Party";
                                } else {
                                    toSend[i][1] = "Independent Party";
                                }

                                //get email
                                toSend[i][2] = person.get("oc_email").getAsString();

                                //get website
                                toSend[i][3] = person.get("website").getAsString();
                            }


                            Bundle b = new Bundle();
                            b.putStringArray("PERSON1", toSend[0]);
                            b.putStringArray("PERSON2", toSend[1]);
                            b.putStringArray("PERSON3", toSend[2]);
                            b.putStringArray("PERSON4", toSend[3]);
                            b.putStringArray("PERSON5", toSend[4]);

                            Intent intent = new Intent(MainActivity.this, Congressional.class);
                            intent.putExtras(b);
                            startActivity(intent);

                        } else {
                            System.out.println("Failed to get Geocode data");
                        }
                    }
                });
    }



    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    //from http://stackoverflow.com/questions/31059369/error-with-google-places-api-addconnectioncallbacks
    //from http://developer.android.com/intl/zh-tw/reference/android/location/Location.html
    //implements ConnectionCallbacks and OnFailedListeners
    @Override
    public void onConnected(Bundle connectionHint) {
        // We are now connected!
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            latitude = String.valueOf(mLastLocation.getLatitude());
            longitude = String.valueOf(mLastLocation.getLongitude());
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // We are not connected anymore!
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // We tried to connect but failed!
    }

    private void findGeocode(final String zipcode){
        String link = "https://congress.api.sunlightfoundation.com/legislators/locate?zip=" +
                zipcode +
                "&apikey=05b337727dd142f4bf97be8994e6df46";


        final String googlelink = "https://maps.googleapis.com/maps/api/geocode/json?address="
                    + zipcode
                    + "&key=AIzaSyB64SFX7NazuI8M9w6k9THgeYGqWZ22-9g";

        Ion.with(MainActivity.this)
                .load(link)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(result != null) {
                            // do stuff with the result or error

                            Ion.with(MainActivity.this)
                                    .load(googlelink)
                                    .asJsonObject()
                                    .setCallback(new FutureCallback<JsonObject>() {
                                        @Override
                                        public void onCompleted(Exception e, JsonObject result2) {
                                            // do stuff with the result or error
                                            String county = zipcode;

                                            JsonArray r = (JsonArray)result2.get("results");
                                            JsonArray addrcomp = (JsonArray)((JsonObject)r.get(0)).get("address_components");
                                            for(int i = 0; i < addrcomp.size();i++){
                                                JsonObject obj = (JsonObject)addrcomp.get(i);
                                                JsonArray types = (JsonArray)obj.get("types");

                                                if (types.get(0).getAsString().equals("administrative_area_level_2")){
                                                    county = obj.get("long_name").getAsString();
                                                }
                                            }

                                            if(county.length()>7 && county.substring(county.length()-7,county.length()).equals(" County")){
                                                county = county.substring(0,county.length()-7);
                                            }
                                            Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                                            sendIntent.putExtra("ZIPCODE", county);
                                            startService(sendIntent);
                                        }
                                    });


                            String[][] toSend = new String[5][4];
                            JsonArray r = (JsonArray)result.get("results");
                            for (int i = 0; i<r.size() && i<5; i++){
                                JsonObject person = (JsonObject)r.get(i);

                                //get name
                                String name = "";
                                JsonPrimitive chamber = (JsonPrimitive)person.get("chamber");
                                if(chamber.getAsString().equals("house")){
                                    name += "Rep. ";
                                } else {
                                    name += "Senator ";
                                }
                                name += person.get("first_name").getAsString();
                                name += " ";
                                name += person.get("last_name").getAsString();
                                toSend[i][0] = name;

                                //get party
                                String party = person.get("party").getAsString();
                                if(party.equals("D")){
                                    toSend[i][1] = "Democratic Party";
                                } else if (party.equals("R")){
                                    toSend[i][1] = "Republican Party";
                                } else {
                                    toSend[i][1] = "Independent Party";
                                }

                                //get email
                                toSend[i][2] = person.get("oc_email").getAsString();

                                //get website
                                toSend[i][3] = person.get("website").getAsString();
                            }


                            Bundle b = new Bundle();
                            b.putStringArray("PERSON1", toSend[0]);
                            b.putStringArray("PERSON2", toSend[1]);
                            b.putStringArray("PERSON3", toSend[2]);
                            b.putStringArray("PERSON4", toSend[3]);
                            b.putStringArray("PERSON5", toSend[4]);

                            Intent intent = new Intent(MainActivity.this, Congressional.class);
                            intent.putExtras(b);
                            intent.putExtra("ZIPCODE", zipcode);
                            startActivity(intent);
                        } else {
                            System.out.println("Failed to get Geocode data");
                        }
                    }
                });
    }
}
