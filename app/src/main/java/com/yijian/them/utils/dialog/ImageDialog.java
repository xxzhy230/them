package com.yijian.them.utils.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.xxzhy.okhttputils.callback.FileCallBack;
import com.yijian.them.R;
import com.yijian.them.view.FlyBanner;
import com.yqjr.utils.service.OkHttp;
import com.yqjr.utils.utils.ToastUtils;

import java.io.File;
import java.util.List;

import okhttp3.Call;


public class ImageDialog extends BaseDialog {
    OnSaveImageListener onSaveImageListener;
    private List<String> urls;
    private int selectPosition;
    private int position = 0;
    private Context mContext;

    public void setOnSaveImageListener(OnSaveImageListener onSaveImageListener) {
        this.onSaveImageListener = onSaveImageListener;
    }

    public ImageDialog(Context context, List<String> urls, int position) {
        super(context);
        mContext = context;
        this.urls = urls;
        this.position = position;
        init();
    }

    private void init() {
        View view = View.inflate(mContext, R.layout.dialog_image, null);
        ImageView ivImageSave = view.findViewById(R.id.ivImageSave);
        FlyBanner fbIntrodcem = view.findViewById(R.id.fbIntrodcem);
        fbIntrodcem.setImagesUrl(urls,position);
        setContentView(view);
        ivImageSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = urls.get(selectPosition);
                if (onSaveImageListener != null) {
                    onSaveImageListener.onSaveImage(url);
                }
            }
        });
        fbIntrodcem.setOnItemChangeListener(new FlyBanner.OnItemChangeListener() {
            @Override
            public void onItemChange(int position) {
                selectPosition = position;
            }
        });
        fbIntrodcem.setOnItemClickListener(new FlyBanner.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        Window window = getWindow();
        window.setBackgroundDrawableResource(R.color.color_f2);
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度
        lp.height = WindowManager.LayoutParams.MATCH_PARENT; // 高度
        lp.y = 0;
        window.setAttributes(lp);
        try {
          
            if (mContext != null && mContext instanceof Activity) {
                if (((Activity) mContext).isFinishing()) {
                    return;
                }
            }
            if (isShowing()) {
                return;
            }
            super.show();
        } catch (Throwable t) {

        }
    }

    /**
     * 保存图片
     */
    public static final int SELECT_PHOTO = 2;

    public void saveImage(String url) {
        if (ContextCompat.checkSelfPermission(mContext,
                "android.permission.WRITE_EXTERNAL_STORAGE")
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mContext,
                "android.permission.READ_EXTERNAL_STORAGE")
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA"},
                    SELECT_PHOTO);
        } else {
            downLoad(url);
        }
    }

    private void downLoad(String url) {
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "them";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        AlertUtils.showProgress(false,mContext);
        OkHttp.get().url(url).build().execute(new FileCallBack(storePath, System.currentTimeMillis() + ".jpg") {
            @Override
            public void onError(Call call, Exception e, int i) {
                System.out.println(e.toString());
            }

            @Override
            public void onResponse(File file, int i) {
                Uri uri = Uri.fromFile(file);
                mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                AlertUtils.dismissProgress();
                ToastUtils.toastCenter(mContext,"保存完成");
            }

        });
    }

    public interface OnSaveImageListener {
        void onSaveImage(String url);
    }

}
