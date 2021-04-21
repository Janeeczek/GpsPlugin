package com.example.cordova.plugin;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import com.example.cordova.plugin.service.LocationService;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;


public class GpsPlugin extends CordovaPlugin {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private Context applicationContext;
    private static String TAG = "GpsPlugin";
    public static CordovaWebView gWebView;
    private static final String ACTION_REQUEST_START = "start";
    private static final String ACTION_REQUEST_STOP = "stop";
	private static final String ACTION_REQUEST_TEST = "test";
    private static final String[] BACKGROUND_PERMISSION = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
    public GpsPlugin() {
    }

    public GpsPlugin(Context applicationContext) {
        this.applicationContext = applicationContext;
    }
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        gWebView = webView;
        System.out.println(TAG + "==>  initialize");
    }
    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        System.out.println(TAG + "==> execute");
		if (ACTION_REQUEST_TEST.equals(action)) {
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    System.out.println(TAG + "==> TEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEST");
                    checkPermission(BACKGROUND_PERMISSION);
                    Intent serviceIntent = new Intent(cordova.getContext(), LocationService.class);
                    cordova.getContext().stopService(serviceIntent);
                    callbackContext.success();
                }
            });
            return true;
        }
        else if (ACTION_REQUEST_START.equals(action)) {

            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    System.out.println(TAG + "==> STAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAART");
                    Intent serviceIntent = new Intent(cordova.getContext(), LocationService.class);
                    serviceIntent.putExtra("inputExtra", "Uruchom aplikacje, żeby wyłączyć.");
                    ContextCompat.startForegroundService(cordova.getContext(), serviceIntent);
                    callbackContext.success();
                }
            });
            return true;
        }
        else if (ACTION_REQUEST_STOP.equals(action)) {

            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    Intent serviceIntent = new Intent(cordova.getContext(), LocationService.class);
                    cordova.getContext().stopService(serviceIntent);
                    callbackContext.success();
                }
            });
            return true;
        }
        return false;
    }
    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }
    }
    private void checkPermission(String[] permission) {
        if(hasAllPermissions(permission)) {
            System.out.println("MA WSZYSTKIE PERMISJE");
        } else {
            System.out.println("PROSZE O PERMISJE");
            cordova.requestPermissions((CordovaPlugin)this,MY_PERMISSIONS_REQUEST_LOCATION, permission);
        }
    }
    private boolean hasAllPermissions(String[] permissions) {

        for (String permission : permissions) {
            System.out.println(TAG + " =====> "+permission+" : " + cordova.hasPermission(permission));
            if(!cordova.hasPermission(permission)) {
                return false;
            }
        }
        return true;
    }
}
