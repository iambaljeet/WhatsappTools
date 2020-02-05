package com.app.wptools.TextRepeater.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.wptools.R;
import com.app.wptools.TextRepeater.Models.SqLiteText;
import com.app.wptools.TextRepeater.helper.DatabaseHandler;
import com.app.wptools.TextRepeater.helper.RecentDatabaseHandler;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    EditText text_repeat, text_repeat_number;

    SeekBar seekbar_repeat;

    CheckBox button_newLine, button_addspace;

    Button button_repeat;

    String repeated_text;

    DatabaseHandler db;

//    private InterstitialAd mInterstitialAd;

    private RecentDatabaseHandler recentDatabaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeat);

        final AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("344E60227ECF057252A709CF0FCF1F32").build();
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

        new BgTasks().execute();

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.repeat_menu));

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId(getString(R.string.exit_interstitial));
//        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("344E60227ECF057252A709CF0FCF1F32").build(););

        text_repeat = (EditText) findViewById(R.id.text_repeat);
        text_repeat_number = (EditText) findViewById(R.id.text_repeat_number);

        seekbar_repeat = (SeekBar) findViewById(R.id.seekbar_repeat);

        button_newLine = (CheckBox) findViewById(R.id.button_newLine);
        button_addspace = (CheckBox) findViewById(R.id.button_addspace);

        button_repeat = (Button) findViewById(R.id.button_repeat);
        text_repeat_number.setText("1");

//        text_repeat_number.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                if (!TextUtils.isEmpty(text_repeat_number.getText().toString())) {
//                    if (Integer.parseInt(text_repeat_number.getText().toString()) <= 0) {
//                        text_repeat_number.setText("1");
//                    }
//                }
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (!TextUtils.isEmpty(text_repeat_number.getText().toString())) {
//                if (Integer.parseInt(text_repeat_number.getText().toString()) <= 0) {
//                    text_repeat_number.setText("1");
//                }
//            }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!TextUtils.isEmpty(text_repeat_number.getText().toString())) {
//                if (Integer.parseInt(text_repeat_number.getText().toString()) <= 0) {
//                    text_repeat_number.setText("1");
//                }
//            }
//            }
//        });

        seekbar_repeat.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                try {
                    if (progress == 0) {
                        text_repeat_number.setText(1 + "");
                    } else {
                        text_repeat_number.setText(progress + "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        button_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                if (!TextUtils.isEmpty(text_repeat.getText().toString().trim())) {
                    if (!TextUtils.isEmpty(text_repeat_number.getText().toString().trim())) {
                        if (Integer.parseInt(text_repeat_number.getText().toString()) > 0) {
                            new AsyncTaskRunner().execute();
                        }
                        else {
                            text_repeat_number.setError("Repeat limit should be 1 or more.");
                            text_repeat_number.requestFocus();
                        }
                    }
                    else {
                        text_repeat_number.setError("Please enter value.");
                        text_repeat_number.requestFocus();
                    }
                }
                else {
                    text_repeat.setError("Please enter text to repeat.");
                    text_repeat.requestFocus();
                }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

//    public void textRepeater() {
//        int no = Integer.parseInt(this.number.getText().toString());
//        ShareCompat.IntentBuilder.from(this).setText(new String(new char[no]).replace("\u0000", this.editText.getText().toString())).setType("text/plain").setChooserTitle((CharSequence) "Have fun").startChooser();
//    }
//
//    public void textRepeaterNewLine() {
//        ShareCompat.IntentBuilder.from(this).setText(new String(new char[Integer.parseInt(this.number.getText().toString())]).replace("\u0000", this.editText.getText().toString() + "\n")).setType("text/plain").setChooserTitle((CharSequence) "Have fun").startChooser();
//    }
//
//    public void textRepeaterCopy() {
//        int no = Integer.parseInt(this.number.getText().toString());
//        ((ClipboardManager) getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("copied", new String(new char[no]).replace("\u0000", this.editText.getText().toString())));
//        Toast.makeText(getApplicationContext(), "Copied", 0).show();
//    }
//
//    public void textRepeaterNewLineCopy() {
//        ((ClipboardManager) getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("copied", new String(new char[Integer.parseInt(this.number.getText().toString())]).replace("\u0000", this.editText.getText().toString() + "\n")));
//        Toast.makeText(getApplicationContext(), "Copied", 0).show();
//    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Repeating..."); // Calls onProgressUpdate()
            if (button_newLine.isChecked() && button_addspace.isChecked()) {
                try {
                    int no = Integer.parseInt(text_repeat_number.getText().toString());
                    repeated_text = new String(new char[no]).replace("\u0000", text_repeat.getText().toString()+ " " + "\n");
                    resp = "";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (!button_newLine.isChecked() && !button_addspace.isChecked()) {
                try {
                    int no = Integer.parseInt(text_repeat_number.getText().toString());
                    repeated_text = new String(new char[no]).replace("\u0000", text_repeat.getText().toString());
                    resp = "";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (button_newLine.isChecked() && !button_addspace.isChecked()) {
                try {
                    int no = Integer.parseInt(text_repeat_number.getText().toString());
                    repeated_text = new String(new char[no]).replace("\u0000", text_repeat.getText().toString()+ "\n");
                    resp = "";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!button_newLine.isChecked() && button_addspace.isChecked()) {
                try {
                    int no = Integer.parseInt(text_repeat_number.getText().toString());
                    repeated_text = new String(new char[no]).replace("\u0000", text_repeat.getText().toString()+ " ");
                    resp = "";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            try {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
                SqLiteText sqLiteText = new SqLiteText();
                sqLiteText.setRep_text(repeated_text);
                sqLiteText.setId(1);
            if (repeated_text!=null) {
                if (db.getTextCount() > 0) {
                    db.updateText(sqLiteText);
                } else {
                    db.addText(sqLiteText);
                }
                recentDatabaseHandler.addText(sqLiteText);
//                Prefs.putString(Utils.PREFS_REPEATED_TEXT, repeated_text);
                Intent intent = new Intent(MainActivity.this, ShareActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(MainActivity.this, "Some error occured please try again.", Toast.LENGTH_SHORT).show();
            }
            Log.e("Textrepeated", "SqLiteText");
        } catch (Exception e) {
            e.printStackTrace();
        }
        }


        @Override
        protected void onPreExecute() {
            try {
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "ProgressDialog",
                    "Repeating text");
        } catch (Exception e) {
            e.printStackTrace();
        }
        }


        @Override
        protected void onProgressUpdate(String... text) {
            Log.e("Textrepeated", "SqLiteText" + text[0]);
        }
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
//        if (mInterstitialAd.isLoaded()) {
//            mInterstitialAd.show();
//        } else {
//            Log.d("TAG", "The interstitial wasn't loaded yet.");
//        }
        super.onBackPressed();
    }

    public class BgTasks extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            db = new DatabaseHandler(MainActivity.this);
            recentDatabaseHandler = new RecentDatabaseHandler(MainActivity.this);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
        }
    }
}
