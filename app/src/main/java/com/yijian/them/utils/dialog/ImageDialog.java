package com.yijian.them.utils.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yijian.them.R;
import com.yijian.them.utils.FileUtil;
import com.yijian.them.view.FlyBanner;
import com.yqjr.utils.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;


public class ImageDialog extends BaseDialog {
    OnSaveImageListener onSaveImageListener;
    private List<String> urls;
    private int selectPosition;

    public void setOnSaveImageListener(OnSaveImageListener onSaveImageListener) {
        this.onSaveImageListener = onSaveImageListener;
    }

    public ImageDialog(Context context, List<String> urls) {
        super(context);
        this.urls = urls;
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.dialog_image, null);
        ImageView ivImageSave = view.findViewById(R.id.ivImageSave);
        FlyBanner fbIntrodcem = view.findViewById(R.id.fbIntrodcem);
        fbIntrodcem.setImagesUrl(urls);
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
        lp.y=0;
        window.setAttributes(lp);
        try {
            Context context = getContext();
            if (context != null && context instanceof Activity) {
                if (((Activity) context).isFinishing()) {
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
        if (ContextCompat.checkSelfPermission(getContext(),
                "android.permission.WRITE_EXTERNAL_STORAGE")
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getContext(),
                "android.permission.READ_EXTERNAL_STORAGE")
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(),
                    new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA"},
                    SELECT_PHOTO);
        } else {
            downLoad(url);
        }
    }

    private void downLoad(String url) {
        //Target
        Target target = new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                String imageName = System.currentTimeMillis() + ".png";

                File dcimFile = FileUtil.getDCIMFile(FileUtil.PATH_PHOTOGRAPH, imageName);

                FileOutputStream ostream = null;
                try {
                    ostream = new FileOutputStream(dcimFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                    ostream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                ToastUtils.toastCenter(getContext(), "下载图片失败");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                ToastUtils.toastCenter(getContext(), "下载图片成功");

            }
        };
        Picasso.with(getContext()).load(url).into(target);
    }

    public interface OnSaveImageListener {
        void onSaveImage(String url);
    }

}
