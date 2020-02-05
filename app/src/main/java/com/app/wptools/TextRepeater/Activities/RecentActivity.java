package com.app.wptools.TextRepeater.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wptools.R;
import com.app.wptools.TextRepeater.Models.SqLiteText;
import com.app.wptools.TextRepeater.helper.RecentDatabaseHandler;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class RecentActivity extends AppCompatActivity {

    RecentDatabaseHandler recentDatabaseHandler;
    RecyclerView recyclerView_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);

        final AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("344E60227ECF057252A709CF0FCF1F32").build();;
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                AdRequest adRequest = new AdRequest.Builder().addTestDevice("344E60227ECF057252A709CF0FCF1F32").build();;
                mAdView.loadAd(adRequest);
            }

            @Override
            public void onAdOpened() {
            }

            @Override
            public void onAdLeftApplication() {
                AdRequest adRequest = new AdRequest.Builder().addTestDevice("344E60227ECF057252A709CF0FCF1F32").build();;
                mAdView.loadAd(adRequest);
            }

            @Override
            public void onAdClosed() {
            }
        });

        recentDatabaseHandler = new RecentDatabaseHandler(this);

        recyclerView_history = (RecyclerView) findViewById(R.id.recyclerView_history);

        if (recentDatabaseHandler.getTextCount() > 0) {
        } else {
        }

        SqLiteText sqLiteText = new SqLiteText();
        sqLiteText = recentDatabaseHandler.getText(1);
        String repeated_text = sqLiteText.getRep_text();
    }
}
