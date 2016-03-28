package com.ctg.ctvideo.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctg.ctvideo.R;
import com.ctg.ctvideo.activities.VideoActivity;
import com.ctg.ctvideo.model.Video;
import com.ctg.ctvideo.services.BitmapWorkerService;

import java.util.List;

public class VideoListAdapter extends ArrayAdapter<Video> {

    private BitmapWorkerService bitmapWorkerService;

    public VideoListAdapter(Context context, int textViewResourceId, List<Video> videos, BitmapWorkerService bitmapWorkerService) {
        super(context, textViewResourceId, videos);
        this.bitmapWorkerService = bitmapWorkerService;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Video v = getItem(position);

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_video_list_item, null);
        } else {
            view = convertView;
        }

        final ImageView img = (ImageView) view.findViewById(R.id.video_item_img);
        // 给ImageView设置一个Tag，保证异步加载图片时不会乱序
        img.setTag(v.pic);
        bitmapWorkerService.setImageView(v.pic, img);

        // 设置标题
        final TextView title = (TextView) view.findViewById(R.id.video_item_title);
        title.setText(v.title);

        // 设置点击动作
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("videoPath", v.url);
                bundle.putString("videoTitle", "video");
                bundle.putInt("startTime", 0);

                VideoActivity.intentTo(getContext(), v.url, v.title);
            }
        });

        return view;
    }

}
