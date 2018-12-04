package com.app.wptools.NoNumberMessage.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.wptools.NoNumberMessage.Models.SqLiteText;
import com.app.wptools.NoNumberMessage.helper.DatabaseHandler;
import com.app.wptools.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hbb20.CountryCodePicker;

public class MainActivity extends AppCompatActivity {

    EditText text_number;
    CheckBox button_save_number;
    Button send_message_button;
    EditText text_message;
    CountryCodePicker country_code;
    Button saved_numbers_button;

    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nonumbermessage);

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

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.message_menu));

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        db = new DatabaseHandler(this);

        text_number = (EditText) findViewById(R.id.text_number);
        button_save_number = (CheckBox) findViewById(R.id.button_save_number);
        send_message_button = (Button) findViewById(R.id.send_message_button);
        text_message = (EditText) findViewById(R.id.text_message);
        country_code = (CountryCodePicker) findViewById(R.id.country_code);
        saved_numbers_button = (Button) findViewById(R.id.saved_numbers_button);

        send_message_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = country_code.getSelectedCountryCode();
                String number = text_number.getText().toString().trim();
                String message = text_message.getText().toString().trim();

                String complete_number = code+number;

                if (button_save_number.isChecked()) {
                    SqLiteText sqLiteText = new SqLiteText(1, number, code);
                    db.addNumber(sqLiteText);
                }
                
                if (!TextUtils.isEmpty(number)) {
                    if (TextUtils.isEmpty(message)) {
                        message = " ";
                    }
                    sendMessageOnWhatsApp(complete_number, message);
                } else {
                    text_number.setError("Please enter phone number first.");
                }
            }
        });

        saved_numbers_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SavedNumbersActivity.class);
                startActivity(intent);
            }
        });
    }

    private void sendMessageOnWhatsApp(String number, String message) {
        String toNumber = number; // contains spaces.
        toNumber = toNumber.replace("+", "").replace(" ", "");

        Intent sendIntent = new Intent("android.intent.action.MAIN");
        sendIntent.putExtra("jid", toNumber + "@s.whatsapp.net");
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setPackage("com.whatsapp");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    protected void onResume() {
        if (db.getNumbersCount()>0) {
            saved_numbers_button.setVisibility(View.VISIBLE);
        } else {
            saved_numbers_button.setVisibility(View.GONE);
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(saved_number,
                new IntentFilter("saved_number"));
        super.onResume();
    }

    private BroadcastReceiver saved_number = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String number = intent.getStringExtra("number");
            String code = intent.getStringExtra("code");

            text_number.setText(number);

            country_code.setCountryForPhoneCode(Integer.parseInt(code));
        }
    };


    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(saved_number);
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
        super.onBackPressed();
    }
}
