package com.example.negative0.drawover;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Foreground service. Creates a head view.
 * The pending intent allows to go back to the settings activity.
 */
public class HeadService extends AccessibilityService {

    private final static int FOREGROUND_ID = 999;

    private HeadLayer mHeadLayer;

    private int currentDeviceID = 0;

    private String readData = "";
    private int readCount = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        logServiceStarted();

        //initHeadLayer();

       // PendingIntent pendingIntent = createPendingIntent();
      //  Notification notification = createNotification(pendingIntent);

       // startForeground(FOREGROUND_ID, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        destroyHeadLayer();
       // stopForeground(true);

        logServiceEnded();
    }

    private void initHeadLayer() {
        mHeadLayer = new HeadLayer(this);
    }

    private void destroyHeadLayer() {
        mHeadLayer.destroy();
        mHeadLayer = null;
    }

    private PendingIntent createPendingIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        return PendingIntent.getActivity(this, 0, intent, 0);
    }

    private Notification createNotification(PendingIntent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new NotificationCompat.Builder(this, "channel_1")
                    .setContentTitle("Draw Over")
                    .setContentText("Testing")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(intent)
                    .build();
        }
        return new Notification.Builder(this)
                .setContentTitle("Draw Over")
                .setContentText("Testing")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(intent)
                .build();
    }

    private void logServiceStarted() {
        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
    }

    private void logServiceEnded() {
        Toast.makeText(this, "Service ended", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
//        Log.d("AccessEvent", "Event occoured");
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        //Log.d("Access", "Service connected");
        super.onServiceConnected();
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        boolean isHandled = false;
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (currentDeviceID == 0) {
                int[] deviceIds = InputDevice.getDeviceIds();
                for (int deviceId : deviceIds) {
                    InputDevice dev = InputDevice.getDevice(deviceId);
                    if (dev.getName().equals("Bluetooth Scanner")) {
                        this.currentDeviceID = dev.getId();
                    }

                }

            }

            if (currentDeviceID == 0) {
                Log.d("HeadService", "Bluetooth scanner not found");
            }
            Log.d("HeadService", "Source:" + event.getDeviceId()+"Current:"+currentDeviceID);
            if (event.getDeviceId() == currentDeviceID) {
                if (readCount == 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            displayData(readData);
                            readCount = 0;
                            readData = "";
                        }
                    }, 1000);
                }
                Log.d("Character:", "" + event.getDisplayLabel());
                readData += event.getDisplayLabel();
                isHandled = true;
                readCount++;

            }
        }
        if (isHandled) return true;
        return super.onKeyEvent(event);
    }

    private void displayData(String data) {
        Toast.makeText(this, "Read:" + data, Toast.LENGTH_LONG).show();
    }


}