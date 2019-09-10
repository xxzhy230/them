package com.yijian.them.ui.team;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.yijian.them.common.App;
import com.yijian.them.common.Config;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.dialog.DialogOnitem;
import com.yijian.them.utils.dialog.SealectImageDialog;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yijian.them.utils.location.LocationUtil;
import com.yijian.them.utils.picasso.PicassoRoundTransform;
import com.yqjr.utils.utils.ToastUtils;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CreatTeamFragment extends BasicFragment {

    //拍照
    private static final int PHOTO_REQUEST_CAREMA = 1;
    //相册
    public static final int SELECT_PHOTO = 2;
    @BindView(R.id.tvCannel)
    TextView tvCannel;
    @BindView(R.id.tvCreaat)
    TextView tvCreaat;
    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.etTeamName)
    EditText etTeamName;
    @BindView(R.id.etTeamContent)
    EditText etTeamContent;
    @BindView(R.id.tvLocation)
    TextView tvLocation;
    private String[] reportArr = new String[]{"从手机相册选择", "拍照"};
    private File headFile;

    @Override
    protected void onClickEvent() {

    }

    @Override
    protected View getResourceView() {
        View view = View.inflate(getActivity(), R.layout.fragment_creat_team, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView(Bundle bundle) {
        context = (BasicActivity) getActivity();
        etTeamContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    if (headFile != null && !TextUtils.isEmpty(etTeamName.getText().toString())) {
                        tvCreaat.setBackgroundResource(R.drawable.shape_3b7aff_5_bg);
                    } else {
                        tvCreaat.setBackgroundResource(R.drawable.shape_gray_5_bg);
                    }
                } else {
                    tvCreaat.setBackgroundResource(R.drawable.shape_gray_5_bg);
                }
            }
        });
        etTeamName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    if (headFile != null && !TextUtils.isEmpty(etTeamContent.getText().toString())) {
                        tvCreaat.setBackgroundResource(R.drawable.shape_3b7aff_5_bg);
                    } else {
                        tvCreaat.setBackgroundResource(R.drawable.shape_gray_5_bg);
                    }
                } else {
                    tvCreaat.setBackgroundResource(R.drawable.shape_gray_5_bg);
                }
            }
        });
    }

    @OnClick({R.id.tvCannel, R.id.tvCreaat, R.id.ivImage, R.id.tvLocation})
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
                final String teamName = etTeamName.getText().toString().trim();
                if (TextUtils.isEmpty(teamName)) {
                    ToastUtils.toastCenter(getActivity(), "请输入小队名称");
                    return;
                }
                final String teamContent = etTeamContent.getText().toString().trim();
                if (TextUtils.isEmpty(teamContent)) {
                    ToastUtils.toastCenter(getActivity(), "请输入小队描述");
                    return;
                }
//                if (tagContent.length() < 5) {
//                    ToastUtils.toastCenter(getActivity(), "话题标签描述不能低于5个字");
//                    return;
//                }
//                if (TextUtils.isEmpty(topicId)) {
//                    ToastUtils.toastCenter(getActivity(), "请选择话题标签");
//                    return;
//                }
                LocationUtil locationUtil = new LocationUtil(getActivity());
                locationUtil.onceLocation(true);
                locationUtil.setOnLocationAddressListener(new LocationUtil.OnLocationAddressListener() {
                    @Override
                    public void onLocation(double latitude, double longitude, String cityCode, String name, String address) {
                        creatTeam(teamName, teamContent, latitude, longitude, cityCode, name);
                    }
                });
                locationUtil.startLocation();

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
            case R.id.tvLocation:
                break;
        }
    }

    private void creatTeam(String teamName, String teamContent, double latitude, double longitude, String cityCode, String cityName) {
        Bitmap bitmap = BitmapFactory.decodeFile(headFile.getPath());
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        System.out.println("图片高度 : " + height);
        System.out.println("图片宽度 : " + width);
        MultipartBody.Part[] part = new MultipartBody.Part[1];
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), headFile);
        part[0] = MultipartBody.Part.createFormData("teamImgs", headFile.getName(), fileBody);
        Http.http.createApi(AuthApi.class).creatTeam(cityCode, latitude + "", longitude + "",
                cityName, teamContent, teamName, height, width, part)
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
                    if (TextUtils.isEmpty(etTeamContent.getText().toString()) || TextUtils.isEmpty(etTeamName.getText().toString())) {
                        tvCreaat.setBackgroundResource(R.drawable.shape_gray_5_bg);
                    } else {
                        tvCreaat.setBackgroundResource(R.drawable.shape_3b7aff_5_bg);
                    }
                }
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
