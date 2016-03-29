package com.ctg.ctvideo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.ctg.ctvideo.R;
import com.ctg.ctvideo.services.BitmapWorkerService;

public class VideoInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView img = (ImageView) findViewById(R.id.video_info_img);
        BitmapWorkerService.setImageView(getIntent().getStringExtra("videoImage"), img);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoActivity.intentTo(VideoInfoActivity.this, getIntent().getExtras());
            }
        });

        System.out.println(getIntent().getStringExtra("videoTitle"));
    }

}
