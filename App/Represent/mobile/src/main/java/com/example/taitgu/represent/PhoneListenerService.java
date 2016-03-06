package com.example.taitgu.represent;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by TaitGu on 3/2/16.
 */
public class PhoneListenerService extends WearableListenerService {
    @Override
    public void onMessageReceived(MessageEvent messageEvent){
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        if(messageEvent.getPath().equalsIgnoreCase("/send_toast")){
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            Context context = getApplicationContext();

            //start activity
            Intent intent = new Intent(this, Detailed.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            super.onMessageReceived(messageEvent);
        }
    }

}
