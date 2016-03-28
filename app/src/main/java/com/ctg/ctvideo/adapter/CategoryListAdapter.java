package com.ctg.ctvideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.ctg.ctvideo.R;
import com.ctg.ctvideo.model.Category;
import com.ctg.ctvideo.services.BitmapWorkerService;

import java.util.List;

public class CategoryListAdapter extends ArrayAdapter<Category> {

    /**
     * 异步加载图片
     */
    private BitmapWorkerService bitmapWorkerService;

    public CategoryListAdapter(Context context, int textViewResourceId, List<Category> categories) {
        super(context, textViewResourceId, categories);
        this.bitmapWorkerService = new BitmapWorkerService();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Category ca = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_video_list, null);
        } else {
            view = convertView;
        }

        // 设置标题
        final TextView title = (TextView) view.findViewById(R.id.video_list_title);
        title.setText(ca.getTitle());

        // 设置视频列表
        VideoListAdapter adapter = new VideoListAdapter(CategoryListAdapter.this.getContext(), 0, ca.getVodList(), bitmapWorkerService);
        final GridView gridView = (GridView) view.findViewById(R.id.video_list_view);
        gridView.setAdapter(adapter);

        return view;
    }

    /**
     * 取消所有正在下载或等待下载的任务。
     */
    public void cancelAllTasks() {
        bitmapWorkerService.cancelAllTasks();
    }

}
