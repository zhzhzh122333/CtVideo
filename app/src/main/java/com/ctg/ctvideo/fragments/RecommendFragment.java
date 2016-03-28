package com.ctg.ctvideo.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ctg.ctvideo.R;
import com.ctg.ctvideo.adapter.CategoryListAdapter;
import com.ctg.ctvideo.model.Category;
import com.ctg.ctvideo.model.Video;
import com.ctg.ctvideo.services.CtVideoService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecommendFragment extends Fragment {
    private List<Category> categories;

    private ListView categoryView;

    private CategoryListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread() {
            public void run() {
                try {
                    CtVideoService.login("admin", "gsta123");
                    JSONObject result = CtVideoService.getVodList();

                    // 分类个数
                    int count = result.optInt("count");

                    categories = new ArrayList<Category>();

                    for (int i = 0; i < count; i++) {

                        JSONObject ca = result.getJSONObject(String.valueOf(i));
                        Category category = new Category();
                        category.setTitle("".equals(ca.optString("category")) ? "综合" : ca.optString("category"));

                        JSONArray vodList = ca.optJSONArray("vod_list");
                        List<Video> videos = new ArrayList<Video>();
                        for (int j = 0; j < 9; j++) {
                            if (i > vodList.length()) {
                                break;
                            }

                            Video video = new Video();
                            JSONObject v = vodList.getJSONObject(j);
                            video.title = v.optString("title");
                            video.url = "http://59.120.43.180:17355";
                            video.pic = v.optString("img");
                            videos.add(video);
                        }
                        category.setVodList(videos);
                        categories.add(category);
                    }
                    videoListHandler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_homepage_recommend, null);
    }

    private Handler videoListHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            adapter = new CategoryListAdapter(RecommendFragment.this.getContext(), 0, categories);
            categoryView = (ListView) RecommendFragment.this.getActivity().findViewById(R.id.homepage_recommend_list);
            categoryView.setAdapter(adapter);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 退出程序时结束所有的下载任务
        adapter.cancelAllTasks();
    }
}
