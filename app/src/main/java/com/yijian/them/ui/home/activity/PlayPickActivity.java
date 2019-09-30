package com.yijian.them.ui.home.activity;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.tencent.qcloud.tim.uikit.component.video.UIKitVideoView;
import com.tencent.qcloud.tim.uikit.component.video.VideoViewActivity;
import com.tencent.qcloud.tim.uikit.component.video.proxy.IPlayer;
import com.tencent.qcloud.tim.uikit.utils.ImageUtil;
import com.tencent.qcloud.tim.uikit.utils.ScreenUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.yijian.them.common.Config;

/**
 * 单独的视频播放页面
 */
public class PlayPickActivity extends AppCompatActivity {

    private static final String TAG = VideoViewActivity.class.getSimpleName();

    private UIKitVideoView mVideoView;
    private int videoWidth = 0;
    private int videoHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TUIKitLog.i(TAG, "onCreate start");
        super.onCreate(savedInstanceState);
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(com.tencent.qcloud.tim.uikit.R.layout.activity_video_view);
        mVideoView = findViewById(com.tencent.qcloud.tim.uikit.R.id.video_play_view);

        String imagePath = getIntent().getStringExtra(TUIKitConstants.CAMERA_IMAGE_PATH);
        String videoUri = getIntent().getStringExtra(Config.VIDEOURL);
//        Bitmap firstFrame = ImageUtil.getBitmapFormPath(imagePath);
//        if (firstFrame != null) {
//            videoWidth = firstFrame.getWidth();
//            videoHeight = firstFrame.getHeight();
//            updateVideoView();
//        }
        Uri uri = Uri.parse(videoUri);
        mVideoView.setVideoURI(uri);
        mVideoView.setOnPreparedListener(new IPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IPlayer mediaPlayer) {
                mVideoView.start();
            }
        });
        mVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                } else {
                    mVideoView.start();
                }
            }
        });

        findViewById(com.tencent.qcloud.tim.uikit.R.id.video_view_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.stop();
                finish();
            }
        });
        TUIKitLog.i(TAG, "onCreate end");
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        TUIKitLog.i(TAG, "onConfigurationChanged start");
        super.onConfigurationChanged(newConfig);
        updateVideoView();
        TUIKitLog.i(TAG, "onConfigurationChanged end");
    }

    private void updateVideoView() {
        TUIKitLog.i(TAG, "updateVideoView videoWidth: " + videoWidth + " videoHeight: " + videoHeight);
        if (videoWidth <= 0 && videoHeight <= 0) {
            return;
        }
        boolean isLandscape = true;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            isLandscape = false;
        }

        int deviceWidth;
        int deviceHeight;
        if (isLandscape) {
            deviceWidth = Math.max(ScreenUtil.getScreenWidth(this), ScreenUtil.getScreenHeight(this));
            deviceHeight = Math.min(ScreenUtil.getScreenWidth(this), ScreenUtil.getScreenHeight(this));
        } else {
            deviceWidth = Math.min(ScreenUtil.getScreenWidth(this), ScreenUtil.getScreenHeight(this));
            deviceHeight = Math.max(ScreenUtil.getScreenWidth(this), ScreenUtil.getScreenHeight(this));
        }
        int[] scaledSize = ScreenUtil.scaledSize(deviceWidth, deviceHeight, videoWidth, videoHeight);
        //  TUIKitLog.i(TAG, "scaled width: " + scaledSize[0] + " height: " + scaledSize[1]);
        ViewGroup.LayoutParams params = mVideoView.getLayoutParams();
        params.width = scaledSize[0];
        params.height = scaledSize[1];
        mVideoView.setLayoutParams(params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.stop();
        }
    }

}
