package com.yijian.them.ui.home.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.yijian.them.ui.login.DataMoudle;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.dialog.DialogOnitem;
import com.yijian.them.utils.dialog.SealectImageDialog;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yijian.them.utils.picasso.PicassoRoundTransform;
import com.yqjr.utils.spUtils.SPUtils;
import com.yqjr.utils.utils.ToastUtils;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CreatTagFragment extends BasicFragment {
    @BindView(R.id.tvCannel)
    TextView tvCannel;
    @BindView(R.id.tvCreaat)
    TextView tvCreaat;
    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.etTagName)
    EditText etTagName;
    @BindView(R.id.etTagContent)
    EditText etTagContent;
    @BindView(R.id.tvSelectTag)
    TextView tvSelectTag;

    //拍照
    private static final int PHOTO_REQUEST_CAREMA = 1;
    //相册
    public static final int SELECT_PHOTO = 2;
    private String[] reportArr = new String[]{"从手机相册选择", "拍照"};
    private File headFile;
    private String topicId;
    private String topicName;

    @Override
    protected void onClickEvent() {

    }

    @Override
    protected View getResourceView() {
        View view = View.inflate(getActivity(), R.layout.fragment_creat_tag, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView(Bundle bundle) {
        context = (BasicActivity)getActivity();
    }


    @OnClick({R.id.tvCannel, R.id.tvCreaat, R.id.ivImage, R.id.tvSelectTag})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvCannel:
                getActivity().finish();
                break;
            case R.id.tvCreaat:
                if (headFile == null) {
                    ToastUtils.toastCenter(getActivity(), "请选择图片");
                    return;
                }
                String tagName = etTagName.getText().toString().trim();
                if (TextUtils.isEmpty(tagName)) {
                    ToastUtils.toastCenter(getActivity(), "请输入话题名称");
                    return;
                }
                String tagContent = etTagContent.getText().toString().trim();
                if (TextUtils.isEmpty(tagContent)) {
                    ToastUtils.toastCenter(getActivity(), "请输入话题描述");
                    return;
                }
                if (tagContent.length()< 5){
                    ToastUtils.toastCenter(getActivity(), "话题标签描述不能低于5个字");
                    return;
                }
                if (TextUtils.isEmpty(topicId)) {
                    ToastUtils.toastCenter(getActivity(), "请选择话题标签");
                    return;
                }

                creatTag(tagName, tagContent);
                break;
            case R.id.ivImage:
                new SealectImageDialog(getActivity(), false, null, reportArr,
                        new DialogOnitem() {
                            public void onItemClickListener(int position) {
                                switch (position) {
                                    case 1:
                                        select_photo();
                                        break;
                                    case 0:
                                        takePhoto();
                                    default:
                                        break;
                                }
                            }
                        });
                break;
            case R.id.tvSelectTag:
                JumpUtils.jumpTagActivity(this);
                break;
        }
    }

    private void creatTag(String tagName, String tagContent) {
        MultipartBody.Part[] part = new MultipartBody.Part[1];
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), headFile);
        part[0] = MultipartBody.Part.createFormData("imgs", headFile.getName(), fileBody);
        Http.http.createApi(AuthApi.class).tag(part, tagName, tagContent, topicId)

                .compose(context.<JsonResult<String>>bindToLifecycle())
                .compose(context.<JsonResult<String>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String response, int code) {
                        ToastUtils.toastCenter(getActivity(), "创建成功");
                        getActivity().finish();
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
        PictureSelectionModel pictureSelectionModel = PictureSelector.create(this).openGallery(PictureMimeType.ofImage());
        pictureSelectionModel.compress(true);
        pictureSelectionModel.selectionMode(PictureConfig.SINGLE);
        pictureSelectionModel.isCamera(false).
                imageFormat(PictureMimeType.PNG).
                minimumCompressSize(200).isGif(false).
                forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        if (ContextCompat.checkSelfPermission(getActivity(), "android.permission.WRITE_EXTERNAL_STORAGE")
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(),
                "android.permission.READ_EXTERNAL_STORAGE")
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(),
                "android.permission.CAMERA")
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA"},
                    PHOTO_REQUEST_CAREMA);
        } else {
            openCamera();
        }
    }

    /**
     * 打开相机
     */
    public void openCamera() {
        //检查是否有拍照权限，以免崩溃
        if (ContextCompat.checkSelfPermission(getActivity(), "android.permission.CAMERA")
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{"android.permission.CAMERA"},
                    PHOTO_REQUEST_CAREMA);
            return;
        } else {
            PictureSelectionModel pictureSelectionModel = PictureSelector.create(this)
                    .openCamera(PictureMimeType.ofImage());
            pictureSelectionModel.compress(true);
            pictureSelectionModel.isCamera(false).
                    imageFormat(PictureMimeType.PNG).
                    minimumCompressSize(200).isGif(false).
                    forResult(PictureConfig.CHOOSE_REQUEST);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case PictureConfig.CHOOSE_REQUEST:
                final List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                if (selectList != null && selectList.size() > 0) {
                    LocalMedia localMedia = selectList.get(0);
                    String compressPath = localMedia.getCompressPath();
                    if (TextUtils.isEmpty(compressPath)) {
                        compressPath = localMedia.getPath();
                    }
                    headFile = new File(compressPath);
                    Picasso.with(getActivity()).load(headFile).transform(new PicassoRoundTransform()).into(ivImage);
                }
                break;
            case 1:
                topicName = data.getStringExtra(Config.TOPICNAME);
                topicId = data.getStringExtra(Config.TOPICID);
                tvSelectTag.setText(topicName);
                break;
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
        if (requestCode == PHOTO_REQUEST_CAREMA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                ToastUtils.toastCenter(getActivity(), "请开启拍摄权限！");
            }
        } else if (requestCode == SELECT_PHOTO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openAlbum();
            } else {
                ToastUtils.toastCenter(getActivity(), "请开启权限！");
            }
        }
    }

}
