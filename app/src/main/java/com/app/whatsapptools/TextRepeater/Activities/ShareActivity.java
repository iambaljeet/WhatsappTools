package com.app.whatsapptools.TextRepeater.Activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.whatsapptools.R;
import com.app.whatsapptools.TextRepeater.Models.SqLiteText;
import com.app.whatsapptools.TextRepeater.helper.DatabaseHandler;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class ShareActivity extends AppCompatActivity {

    TextView text_repeat_preview;

    Button button_repeat, button_copy;

    CardView share_app;

    SqLiteText sqLiteText;

    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        final AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
            }

            @Override
            public void onAdOpened() {
            }

            @Override
            public void onAdLeftApplication() {
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
            }

            @Override
            public void onAdClosed() {
            }
        });

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.repeat_menu));

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        db = new DatabaseHandler(this);

        text_repeat_preview = (TextView) findViewById(R.id.text_repeat_preview);

        text_repeat_preview.setMovementMethod(new ScrollingMovementMethod());

        button_repeat = (Button) findViewById(R.id.button_repeat);
        button_copy = (Button) findViewById(R.id.button_copy);

        share_app = (CardView) findViewById(R.id.share_app);

        setPreviewScreen();

        button_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareRepeatedText();
            }
        });

        button_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SqLiteText sqLiteText = new SqLiteText();
                    sqLiteText = db.getText(1);
                    String repeated_text = sqLiteText.getRep_text();
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("text", repeated_text);
                    clipboard.setPrimaryClip(clip);

                Toast.makeText(ShareActivity.this, "Copied.", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            }
        });

        share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "\n" + "Hey, Flood your friend's Whatsapp, messenger, hike and more with messages. download this app now" +
                        "https://play.google.com/store/apps/details?id=com.freelance.textrepeaterpro");
                startActivity(shareIntent);
            }
                catch (Exception e) {
                    e.printStackTrace();
                }
        }
        });
    }

    private void shareRepeatedText() {
        try {
            SqLiteText sqLiteText = new SqLiteText();
            sqLiteText = db.getText(1);
            String repeated_text = sqLiteText.getRep_text();
            ShareCompat.IntentBuilder.from(ShareActivity.this).setText(repeated_text).setType("text/plain").setChooserTitle((CharSequence) "Have fun").startChooser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPreviewScreen() {
        SqLiteText sqLiteText = new SqLiteText();
        sqLiteText = db.getText(1);
        String repeated_text = "";
        try {
            if (sqLiteText!=null) {
                repeated_text = sqLiteText.getRep_text();
            }
//            repeated_text = Prefs.getString(Utils.PREFS_REPEATED_TEXT, "");
            text_repeat_preview.setText(repeated_text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
