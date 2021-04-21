package com.example.cordova.plugin.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.example.cordova.plugin.service.LocationService;

public class AutoStart extends BroadcastReceiver {


    private LocationService mService = null;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent arg1) {
        Toast.makeText(context, "SERVICE Z AUTOSTARTU", Toast.LENGTH_SHORT).show();
        Intent serviceIntent = new Intent(context, LocationService.class);
        serviceIntent.putExtra("inputExtra", "Uruchom aplikacje, żeby wyłączyć.");
        ContextCompat.startForegroundService(context, serviceIntent);
    }
}
