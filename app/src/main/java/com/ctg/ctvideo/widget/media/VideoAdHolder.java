package com.ctg.ctvideo.widget.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ctg.ctvideo.R;
import com.ctg.ctvideo.services.BitmapWorkerService;

import java.util.ArrayList;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.MediaPlayerProxy;

public class VideoAdHolder {
    private IMediaPlayer mMediaPlayer;

    private IjkVideoView mIjkVideoView;
    private FrameLayout mFrameLayout;
    private ImageView mImageView;
    private Button mButton;
    private Bitmap mBitmap;
    private String adMessage;

    private VideoAdThread videoAdThread;
    private List<VideoAdTask> taskCollection = new ArrayList<VideoAdTask>();

    public VideoAdHolder(IjkVideoView ijkVideoView, FrameLayout frameLayout) {
        mIjkVideoView = ijkVideoView;
        mFrameLayout = frameLayout;
        mImageView = (ImageView) mFrameLayout.findViewById(R.id.video_ad_image);
        mButton = (Button) mFrameLayout.findViewById(R.id.video_ad_close);

        // 点击广告
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessage(MSG_CLICK_AD);
            }
        });

        // 关闭广告
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessage(MSG_HIDE_AD);
            }
        });
    }

    public void setMediaPlayer(IMediaPlayer mp) {
        mMediaPlayer = mp;
        if (mMediaPlayer != null) {
            videoAdThread = new VideoAdThread();
            videoAdThread.start();
        } else if(videoAdThread != null) {
            videoAdThread.interrupt();
        }
    }

    public void addVideoAdTask(long time, String imageUrl) {
        VideoAdTask task = new VideoAdTask(time, imageUrl);
        taskCollection.add(task);
    }

    public static final int MSG_SHOW_AD = 1;
    public static final int MSG_HIDE_AD = 2;
    public static final int MSG_CLICK_AD = 3;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_AD:
                    if (mBitmap == null) {
                        BitmapWorkerService.setImageView(null, mImageView);
                    } else {
                        mImageView.setImageBitmap(mBitmap);
                    }
                    mIjkVideoView.pause();
                    mFrameLayout.setVisibility(View.VISIBLE);
                    break;
                case MSG_HIDE_AD:
                    mFrameLayout.setVisibility(View.INVISIBLE);
                    mIjkVideoView.start();
                    break;
                case MSG_CLICK_AD:
                    System.out.println("clickAd:" + adMessage);
                    break;
            }
        }
    };

    /**
     * 广告线程，监听并显示进入时间点的广告
     */
    class VideoAdThread extends Thread {
        public void run() {
            while (true) {
                try {
                    IjkMediaPlayer mp = null;
                    if (mMediaPlayer == null)
                        break;
                    if (mMediaPlayer instanceof IjkMediaPlayer) {
                        mp = (IjkMediaPlayer) mMediaPlayer;
                    } else if (mMediaPlayer instanceof MediaPlayerProxy) {
                        MediaPlayerProxy proxy = (MediaPlayerProxy) mMediaPlayer;
                        IMediaPlayer internal = proxy.getInternalMediaPlayer();
                        if (internal != null && internal instanceof IjkMediaPlayer)
                            mp = (IjkMediaPlayer) internal;
                    }
                    if (mp == null)
                        break;

                    // 显示广告
                    long currentTime = mp.getCurrentPosition();
                    for (VideoAdTask task : taskCollection) {
                        if (currentTime > task.time) {
                            adMessage = task.imageUrl;
                            mBitmap = BitmapWorkerService.getImageBitmap(task.imageUrl);
                            mHandler.sendEmptyMessage(MSG_SHOW_AD);
                            taskCollection.remove(task);
                            break;
                        }
                    }
                    Thread.sleep(500);

                } catch (Exception e) {
                    break;
                }
            }
        }
    }

    /**
     * 广告
     */
    class VideoAdTask {
        public long time;
        public String imageUrl;

        public VideoAdTask() {

        }

        public VideoAdTask(long time, String imageUrl) {
            this.time = time;
            this.imageUrl = imageUrl;
            BitmapWorkerService.preloadImage(imageUrl);
        }
    }


//    public VideoAdHolder(Context context, TableLayout tableLayout) {
//        mTableLayoutBinder = new TableLayoutBinder(context, tableLayout);
//    }
//
//    private void appendSection(int nameId) {
//        mTableLayoutBinder.appendSection(nameId);
//    }
//
//    private void appendRow(int nameId) {
//        View rowView = mTableLayoutBinder.appendRow2(nameId, null);
//        mRowMap.put(nameId, rowView);
//    }
//
//    private void setRowValue(int id, String value) {
//        View rowView = mRowMap.get(id);
//        if (rowView == null) {
//            rowView = mTableLayoutBinder.appendRow2(id, value);
//            mRowMap.put(id, rowView);
//        } else {
//            mTableLayoutBinder.setValueText(rowView, value);
//        }
//    }
//
//    public void setMediaPlayer(IMediaPlayer mp) {
//        mMediaPlayer = mp;
//        if (mMediaPlayer != null) {
//            mHandler.sendEmptyMessageDelayed(MSG_UPDATE_HUD, 500);
//        } else {
//            mHandler.removeMessages(MSG_UPDATE_HUD);
//        }
//    }
//
//    private static String formatedDurationMilli(long duration) {
//        if (duration >=  1000) {
//            return String.format(Locale.US, "%.2f sec", ((float)duration) / 1000);
//        } else {
//            return String.format(Locale.US, "%d msec", duration);
//        }
//    }
//
//    private static String formatedSize(long bytes) {
//        if (bytes >= 100 * 1000) {
//            return String.format(Locale.US, "%.2f MB", ((float)bytes) / 1000 / 1000);
//        } else if (bytes >= 100) {
//            return String.format(Locale.US, "%.1f KB", ((float)bytes) / 1000);
//        } else {
//            return String.format(Locale.US, "%d B", bytes);
//        }
//    }
//
//    private static final int MSG_UPDATE_HUD = 1;
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case MSG_UPDATE_HUD: {
//                    VideoAdHolder holder = VideoAdHolder.this;
//                    IjkMediaPlayer mp = null;
//                    if (mMediaPlayer == null)
//                        break;
//                    if (mMediaPlayer instanceof IjkMediaPlayer) {
//                        mp = (IjkMediaPlayer) mMediaPlayer;
//                    } else if (mMediaPlayer instanceof MediaPlayerProxy) {
//                        MediaPlayerProxy proxy = (MediaPlayerProxy) mMediaPlayer;
//                        IMediaPlayer internal = proxy.getInternalMediaPlayer();
//                        if (internal != null && internal instanceof IjkMediaPlayer)
//                            mp = (IjkMediaPlayer) internal;
//                    }
//                    if (mp == null)
//                        break;
//
//                    int vdec = mp.getVideoDecoder();
//                    switch (vdec) {
//                        case IjkMediaPlayer.FFP_PROPV_DECODER_AVCODEC:
//                            setRowValue(R.string.vdec, "avcodec");
//                            break;
//                        case IjkMediaPlayer.FFP_PROPV_DECODER_MEDIACODEC:
//                            setRowValue(R.string.vdec, "MediaCodec");
//                            break;
//                        default:
//                            setRowValue(R.string.vdec, "");
//                            break;
//                    }
//
//                    float fpsOutput = mp.getVideoOutputFramesPerSecond();
//                    float fpsDecode = mp.getVideoDecodeFramesPerSecond();
//                    setRowValue(R.string.fps, String.format(Locale.US, "%.2f / %.2f", fpsDecode, fpsOutput));
//
//                    long videoCachedDuration = mp.getVideoCachedDuration();
//                    long audioCachedDuration = mp.getAudioCachedDuration();
//                    long videoCachedBytes    = mp.getVideoCachedBytes();
//                    long audioCachedBytes    = mp.getAudioCachedBytes();
//
//                    setRowValue(R.string.v_cache, String.format(Locale.US, "%s, %s", formatedDurationMilli(videoCachedDuration), formatedSize(videoCachedBytes)));
//                    setRowValue(R.string.a_cache, String.format(Locale.US, "%s, %s", formatedDurationMilli(audioCachedDuration), formatedSize(audioCachedBytes)));
//
//                    mHandler.removeMessages(MSG_UPDATE_HUD);
//                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE_HUD, 500);
//                }
//            }
//        }
//    };
}
