package com.app.wptools.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

/**
 * Created by Baljeet on 27-01-2018.
 */

public class FirebaseRegistation extends FirebaseMessagingService {

    private static final String TAG = "FirebaseApp";

    @Override
    public void onNewToken(@NonNull String s) {
        //Getting registration token of FCM
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on your logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        //send FCM token to web service
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server for your further implementation
        //Not required for current project
    }
}