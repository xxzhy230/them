package com.yijian.them.ui.home.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.squareup.picasso.Picasso;
import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.common.Config;
import com.yijian.them.ui.home.adapter.ImageAdapter;
import com.yijian.them.ui.login.DataMoudle;
import com.yijian.them.utils.FileUtil;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.dialog.AlertUtils;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yijian.them.view.NoScrollGridView;
import com.yqjr.utils.spUtils.SPUtils;
import com.yqjr.utils.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

public class SendDynamicFragment extends BasicFragment {
    @BindView(R.id.tvCancel)
    TextView tvCancel;
    @BindView(R.id.tvSend)
    TextView tvSend;
    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.tvLocation)
    TextView tvLocation;
    @BindView(R.id.tvHot)
    TextView tvHot;
    @BindView(R.id.tvGroup)
    TextView tvGroup;
    @BindView(R.id.nsgvImage)
    NoScrollGridView nsgvImage;
    @BindView(R.id.rlVideo)
    RelativeLayout rlVideo;
    @BindView(R.id.ivVideoBg)
    ImageView ivVideoBg;

    private String cityCode;
    private double latitude;
    private double longitude;
    private String localName;
    private int selectNum = 0;
    private List<String> mList = new ArrayList<>();
    private ImageAdapter imageAdapter;
    private String content;
    private String videoPath;
    private String groupName = "";
    private String groupId = "";
    private String tagId;
    private String tagName;
    private int type;


    @Override
    protected void onClickEvent() {
        context = (BasicActivity) getActivity();
    }

    @Override
    protected View getResourceView() {
        View rootView = View.inflate(getActivity(), R.layout.fragment_send_dynamic, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initView(Bundle bundle) {
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString();
                if (!TextUtils.isEmpty(content) && content.length() > 6) {
                    tvSend.setTextColor(getResources().getColor(R.color.black));
                    tvSend.setEnabled(true);
                } else {
                    tvSend.setEnabled(false);
                    tvSend.setTextColor(getResources().getColor(R.color.color_FF666666));
                }
            }
        });
        initImage();

    }

    private void initImage() {
        mList.clear();
        mList.add("1");
        imageAdapter = new ImageAdapter(mList, 2);
        nsgvImage.setAdapter(imageAdapter);
        imageAdapter.setOnAddImageListener(new ImageAdapter.OnAddImageListener() {
            @Override
            public void onAddImage() {
                select_photo();
            }

            @Override
            public void onDelImage(int position) {
                String s = mList.get(position);
                mList.remove(s);
                imageAdapter.notifyDataSetChanged();
            }
        });


    }


    @OnClick({R.id.tvCancel, R.id.tvSend, R.id.tvLocation, R.id.tvHot, R.id.tvGroup, R.id.ivPlay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvCancel:
                getActivity().finish();
                break;
            case R.id.tvSend:
                content = etContent.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    return;
                }
                sendDynamic();
                break;
            case R.id.tvLocation:
                JumpUtils.jumpDynamicActivity(this, 3, "", "");
                break;
            case R.id.tvHot:
                JumpUtils.jumpDynamicActivity(this, 4, "选择话题", "");
                break;
            case R.id.tvGroup:
                JumpUtils.jumpDynamicActivity(this, 5, "选择群聊", "");
                break;
            case R.id.ivPlay:
                PictureSelector.create(getActivity()).externalPictureVideo(videoPath);
                break;
        }
    }

    private void sendDynamic() {
        AlertUtils.showProgress(false, getActivity());
        AuthApi api = Http.http.createApi(AuthApi.class);
        Observable<JsonResult<String>> jsonResultObservable = null;
        if (type == 2) {
            File file = new File(videoPath);
            RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("video", file.getName(), fileBody);
            jsonResultObservable = api.sendDynamicVideo(part, cityCode, content, groupId, groupName,
                    latitude + "", longitude + "", localName, tagId, tagName);
        } else {
            MultipartBody.Part[] part = new MultipartBody.Part[selectNum];
            for (int i = 0; i < mList.size(); i++) {
                String s = mList.get(i);
                if (!"1".equals(s)) {
                    File file = new File(mList.get(i));
                    RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    part[i] = MultipartBody.Part.createFormData("imgs", file.getName(), fileBody);
                }
            }
            jsonResultObservable = api.sendDynamic(part, cityCode, content, groupId, groupName,
                    latitude + "", longitude + "", localName, tagId, tagName);
        }
        jsonResultObservable
                .compose(context.<JsonResult<String>>bindToLifecycle())
                .compose(context.<JsonResult<String>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String response, int code) {
                        Log.d("发送动态: ", response + "");
                        if (code == 200) {
                            ToastUtils.toastCenter(getActivity(), "发送成功");
                            getActivity().finish();
                        }
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }

    /**
     * 从相册选择
     */
    private final int SELECT_PHOTO = 10001;

    private void select_photo() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                "android.permission.WRITE_EXTERNAL_STORAGE")
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(),
                "android.permission.READ_EXTERNAL_STORAGE")
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(),
                "android.permission.CAMERA")
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA"},
                    SELECT_PHOTO);
        } else {
            openAlbum();
        }
    }

    private void openAlbum() {
        PictureSelectionModel pictureSelectionModel = PictureSelector.create(this).openGallery(PictureMimeType.ofAll());
        pictureSelectionModel.compress(true);
        pictureSelectionModel.selectionMode(PictureConfig.SINGLE);
        pictureSelectionModel.isCamera(false).
                minimumCompressSize(200).isGif(false).
                maxSelectNum(9 - selectNum).selectionMode(PictureConfig.MULTIPLE).
                forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == 3) {
            String title = data.getStringExtra("title");
            tvLocation.setText(title);
            cityCode = data.getStringExtra("cityCode");
            latitude = data.getDoubleExtra("latitude", 0.0);
            longitude = data.getDoubleExtra("longitude", 0.0);
            localName = data.getStringExtra("localName");
        } else if (requestCode == PictureConfig.CHOOSE_REQUEST) {
            final List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            if (selectList != null && selectList.size() > 0) {
                LocalMedia localMedia = selectList.get(0);
                String pictureType = localMedia.getPictureType();
                if ("image/jpeg".equals(pictureType)) {
                    type = 1;
                    rlVideo.setVisibility(View.GONE);
                    nsgvImage.setVisibility(View.VISIBLE);
                    selectNum = selectList.size();
                    mList.clear();
                    for (int i = 0; i < selectList.size(); i++) {
                        mList.add(selectList.get(i).getCompressPath());
                    }
                    if (mList.size() < 9) {
                        mList.add("1");
                    }
                    imageAdapter.setData(mList);
                } else if ("video/mp4".equals(pictureType)) {
                    type = 2;
                    rlVideo.setVisibility(View.VISIBLE);
                    nsgvImage.setVisibility(View.GONE);
                    videoPath = localMedia.getPath();
                    Bitmap videoThumb = FileUtil.getVideoThumb(videoPath);
                    ivVideoBg.setImageBitmap(videoThumb);
                } else if ("audio/mpeg".equals(pictureType)) {
                    rlVideo.setVisibility(View.VISIBLE);
                    nsgvImage.setVisibility(View.GONE);
                }
            }
        } else if (requestCode == 5) {
            groupName = data.getStringExtra(Config.GROUPNAME);
            groupId = data.getStringExtra(Config.GROUPID);
            tvGroup.setText(groupName);
        } else if (requestCode == 4) {
            tagId = data.getStringExtra(Config.TAGID);
            tagName = data.getStringExtra(Config.TAGNAME);
//            topicId = data.getStringExtra(Config.TOPICID);
//            topicName = data.getStringExtra(Config.TOPICNAME);
            tvHot.setText(tagName);
        }
    }

    /**
     * @param requestCode  申请码
     * @param permissions  申请的权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SELECT_PHOTO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openAlbum();
            } else {
                ToastUtils.toastCenter(getActivity(), "请开启读写权限");
            }
        }
    }
}
