package com.app.wptools.StoryDownloader;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.wptools.R;
import com.app.wptools.StoryDownloader.adapter.RecyclerViewMediaAdapter;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.ArrayList;

public class StoryDownloaderActivity extends AppCompatActivity {

    private static final String WHATSAPP_STATUSES_LOCATION = "/WhatsApp/Media/.Statuses";
    private RecyclerView mRecyclerViewMediaList;
    private LinearLayoutManager mLinearLayoutManager;
    public static final String TAG = "Home";
    private ArrayList<File> arrayListStatuses;
    private TextView message;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_downloader);

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
        title.setText(getString(R.string.stories_menu));

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        message = (TextView) findViewById(R.id.message);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             finish();
            }
        });

        mRecyclerViewMediaList = (RecyclerView) findViewById(R.id.recyclerViewMedia);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewMediaList.setLayoutManager(mLinearLayoutManager);
        System.out.println("......"+ Environment.getExternalStorageDirectory().toString());
        mRecyclerViewMediaList.addOnScrollListener(new CenterScrollListener());

        checkPermission();
    }


    /**
     * get all the files in specified directory
     *
     * @param parentDir
     * @return
     */
    private ArrayList<File> getListFiles(File parentDir) {
        arrayListStatuses.clear();
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files;
        files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {

                if (file.getName().endsWith(".jpg") ||
                        file.getName().endsWith(".gif") ||
                        file.getName().endsWith(".mp4")) {
                    if (!inFiles.contains(file))
                        inFiles.add(file);
                }
            }
        }
        return inFiles;
    }

    private void checkPermission() {
        TedPermission.with(this).setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            arrayListStatuses = new ArrayList<>();
            arrayListStatuses = getListFiles(new File(Environment.getExternalStorageDirectory().toString()+WHATSAPP_STATUSES_LOCATION));
            Log.e("Arraylist", "Path: " + Environment.getExternalStorageDirectory().toString()+WHATSAPP_STATUSES_LOCATION + "Data: " + arrayListStatuses.toString());
            if (arrayListStatuses!=null) {
                if (arrayListStatuses.size()>0) {
                    message.setVisibility(View.GONE);
                    mRecyclerViewMediaList.setVisibility(View.VISIBLE);
                    RecyclerViewMediaAdapter recyclerViewMediaAdapter = new RecyclerViewMediaAdapter(arrayListStatuses, StoryDownloaderActivity.this);
                    mRecyclerViewMediaList.setAdapter(recyclerViewMediaAdapter);
                } else {
                    message.setVisibility(View.VISIBLE);
                    mRecyclerViewMediaList.setVisibility(View.GONE);
                }
            }
//            Toast.makeText(StoryDownloaderActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
//            Toast.makeText(StoryDownloaderActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
        super.onBackPressed();
    }
}
