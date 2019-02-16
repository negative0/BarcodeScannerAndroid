package com.example.negative0.drawover;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    Button createButton, notificationButton;
//    PermissionChecker checker = new PermissionChecker(this);
//    private boolean isShowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createButton = findViewById(R.id.btn_create);
        notificationButton = findViewById(R.id.check_devices);

        createButton.setOnClickListener(this);
        notificationButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_create:
//                if(checker.isRequiredPermissionGranted()) {
//                    if(isShowing){
//                        startHeadService();
//                    }else {
//                        stopHeadService();
//                    }
//                    isShowing = !isShowing;
//                }
//                else{
//                    startActivity(checker.createRequiredPermissionIntent());
//                }
                break;
                case R.id.check_devices:
                    getGameControllerIds();
                    break;
            default:
                Toast.makeText(this, "Which key pressed?", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void startHeadService() {
        Context context = this;
        context.startService(new Intent(context, HeadService.class));
    }

    private void stopHeadService() {
        Context context = this;
        context.stopService(new Intent(context, HeadService.class));
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "main";
            String description = "main_channel";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel("channel_1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public ArrayList<Integer> getGameControllerIds() {
        ArrayList<Integer> gameControllerDeviceIds = new ArrayList<Integer>();
        int[] deviceIds = InputDevice.getDeviceIds();
        for (int deviceId : deviceIds) {
            InputDevice dev = InputDevice.getDevice(deviceId);
            int sources = dev.getSources();

            Log.d("HeadService", dev.getName() + " " + dev.getDescriptor()+ " "+dev.getId());

//            // Verify that the device has gamepad buttons, control sticks, or both.
//            if (((sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD)
//                    || ((sources & InputDevice.SOURCE_JOYSTICK)
//                    == InputDevice.SOURCE_JOYSTICK)) {
//                // This device is a game controller. Store its device ID.
//                if (!gameControllerDeviceIds.contains(deviceId)) {
//                    gameControllerDeviceIds.add(deviceId);
//                }
//            }
        }
        return gameControllerDeviceIds;
    }


}
