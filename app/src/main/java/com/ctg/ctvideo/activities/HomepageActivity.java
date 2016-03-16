package com.ctg.ctvideo.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.GridView;

import com.ctg.ctvideo.R;
import com.ctg.ctvideo.adapter.HomepageAdapter;
import com.ctg.ctvideo.model.Video;

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

                    JSONObject json = new JSONObject(getTextWithJson("http://www.bilibili.com/index/ding.json"));
                    videos = new ArrayList<Video>();

                    String[] names = {"douga", "music", "game", "ent", "teleplay", "bangumi", "movie", "technology", "kichiku", "dance", "fashion"};
                    for (String name : names) {
                        JSONObject row = json.getJSONObject(name);
                        for (int i = 0; i < 10; i++) {
                            JSONObject item = row.getJSONObject(String.valueOf(i));
                            Video v = new Video();
                            v.url = "http://cttest.cachenow.net/dash.mp4";
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

    public static String getTextWithJson(String path) {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            InputStream in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int temp;
            while ((temp = in.read()) != -1) {
                out.write(temp);
            }
            in.close();

            return out.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 退出程序时结束所有的下载任务
        adapter.cancelAllTasks();
    }

}
