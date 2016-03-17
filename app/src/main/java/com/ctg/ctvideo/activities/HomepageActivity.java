package com.ctg.ctvideo.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.LruCache;
import android.widget.GridView;

import com.ctg.ctvideo.R;
import com.ctg.ctvideo.adapter.HomepageAdapter;
import com.ctg.ctvideo.model.Video;
import com.ctg.ctvideo.services.NetworkService;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomepageActivity extends AppCompatActivity {
    private LruCache<String, Bitmap> mMemoryCache;
    private List<Video> videos;

    /**
     * 用于展示照片墙的GridView
     */
    private GridView mPhotoWall;

    /**
     * GridView的适配器
     */
    private HomepageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new Thread() {
            public void run() {
                try {

                    JSONObject json = NetworkService.getJsonByUrl("http://www.bilibili.com/index/ding.json");
                    videos = new ArrayList<Video>();

                    String[] names = {"douga", "music", "game", "ent", "teleplay", "bangumi", "movie", "technology", "kichiku", "dance", "fashion"};
                    for (String name : names) {
                        JSONObject row = json.getJSONObject(name);
                        for (int i = 0; i < 10; i++) {
                            JSONObject item = row.getJSONObject(String.valueOf(i));
                            Video v = new Video();
                            v.url = "http://cttest.cachenow.net/dash.mp4";
//                            v.url = "http://59.120.43.180:17355";
                            v.title = item.getString("title");
                            v.pic = item.getString("pic");
                            videos.add(v);
                        }
                    }

                    imageHandler.sendEmptyMessage(0);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private Handler imageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mPhotoWall = (GridView) findViewById(R.id.homepage_images_wall);
            adapter = new HomepageAdapter(HomepageActivity.this, 0, videos, mPhotoWall);
            mPhotoWall.setAdapter(adapter);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 退出程序时结束所有的下载任务
        adapter.cancelAllTasks();
    }

}
