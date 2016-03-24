package com.ctg.ctvideo.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.ctg.ctvideo.R;
import com.ctg.ctvideo.adapter.HomepageAdapter;
import com.ctg.ctvideo.model.Video;
import com.ctg.ctvideo.services.CtVideoService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecommendFragment extends Fragment {
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread() {
            public void run() {
                try {
                    CtVideoService.login("admin", "gsta123");
                    JSONObject json = CtVideoService.getVodList();

                    videos = new ArrayList<Video>();
                    JSONArray vodList = json.optJSONObject("Response").optJSONArray("vod_list");
                    for (int i = 0; i < vodList.length(); i++) {
                        JSONObject item = vodList.optJSONObject(i);
                        Video v = new Video();
                        v.url =  "http://cttest.cachenow.net/dash.mp4";
                        v.title = item.optString("title");
                        v.pic = item.optString("img");
                        videos.add(v);
                    }
                    imageHandler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("OnCreateView");
        return inflater.inflate(R.layout.fragment_homepage_recommend, null);
    }

    /**
     * 加载图片
     */
    private Handler imageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mPhotoWall = (GridView) RecommendFragment.this.getActivity().findViewById(R.id.homepage_recommend);
            adapter = new HomepageAdapter(RecommendFragment.this.getContext(), 0, videos, mPhotoWall);
            mPhotoWall.setAdapter(adapter);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 退出程序时结束所有的下载任务
        adapter.cancelAllTasks();
    }
}
