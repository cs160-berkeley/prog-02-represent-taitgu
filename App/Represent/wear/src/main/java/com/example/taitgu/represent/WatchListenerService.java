package com.example.taitgu.represent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by TaitGu on 3/4/16.
 */
public class WatchListenerService extends WearableListenerService {
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
        Log.d("T", "in WatchListenerService, got: " + messageEvent.getPath());
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("ZIPCODE", value);
        startActivity(intent);
    }
}

