package com.app.wptools;

import android.app.Application;
import android.content.ContextWrapper;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.android.gms.ads.MobileAds;


/**
 * Created by Baljeet on 18-11-2017.
 */

public class MyApplication extends Application {

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();

        MobileAds.initialize(this, getString(R.string.add_app_id));

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }
}
