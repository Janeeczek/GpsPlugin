package com.example.cordova.plugin.service;


import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
/*
import package_apki.MainActivity;

*/
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

@RequiresApi(api = Build.VERSION_CODES.O)
public class LocationService extends Service {
    private static String TAG = "ForegroundService";
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private NotificationManager mNotificationManager;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private Notification notification;
    private LocationCallback locationCallback;
    private Intent notificationIntent;
    private PendingIntent pendingIntent;

    @Override
    public void onCreate() {
        super.onCreate();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();
        //deklaracja odbiornika lokalizacji
        //onLocationResult wykonuje sie za kazdym razem gdy zostanie pobrana lokalizacja
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    System.out.println(TAG + "==> LoocationResult == null");
                    String input = "Location:  null null " ;
                    notification = buildForegroundNotification(input,pendingIntent);
                    mNotificationManager.notify(1, notification);
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    System.out.println(TAG + "==> for (Location location");

                    Log.i("LocationService", "Location: " + location.getLatitude() + " " + location.getLongitude());
                    String input = "Location: " + location.getLatitude() + " " + location.getLongitude();
                    notification = buildForegroundNotification(input,pendingIntent);
                    mNotificationManager.notify(1, notification);
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        notificationIntent = new Intent(this, MainActivity.class);
        pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        notification = buildForegroundNotification(input,pendingIntent);
        startForeground(1, notification);


        //LOKALIZACJA
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(locationSettingsResponse -> {
            System.out.println(TAG + "==> startLocationUpdates START");
            startLocationUpdates();
        });
        //~LOKALIZACJA
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private Notification buildForegroundNotification(String input, PendingIntent pendingIntent) {
        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Aktywne pobieranie lokalizacji w tle")
                .setContentText(input)
                .setSmallIcon(getApplicationInfo().icon)/////////////////////////////////////////////////////////TUTAJ MOZNA ZMIENIC IKONE
                .setContentIntent(pendingIntent)
                .setSound(null)
                .build();
        return notification;
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
        System.out.println(TAG + "==> createNotificationChannel");
    }

    private int startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //BRAK UPRAWNIEN
            return 0;
        }
        System.out.println(TAG + "==> startLocationUpdates");
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
        return 1;
    }
    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        System.out.println(TAG + "==> createLocationRequest");
    }
    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}
