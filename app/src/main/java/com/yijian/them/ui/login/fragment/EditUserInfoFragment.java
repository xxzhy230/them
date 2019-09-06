package com.yijian.them.ui.login.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.yijian.them.ui.login.DataMoudle;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.Times;
import com.yijian.them.utils.dialog.SealectImageDialog;
import com.yijian.them.utils.dialog.DialogOnitem;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yijian.them.view.CircleImageView;
import com.yqjr.superviseapp.utils.ext.Klog;
import com.yqjr.utils.base.BaseFragment;
import com.yqjr.utils.spUtils.SPUtils;
import com.yqjr.utils.utils.ToastUtils;
import com.yqjr.utils.wheel.BottomDialog;
import com.yqjr.utils.wheel.WheelView;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditUserInfoFragment extends BasicFragment {
    @BindView(R.id.civHead)
    CircleImageView civHead;
    @BindView(R.id.ivHeadState)
    ImageView ivHeadState;
    @BindView(R.id.rlHead)
    RelativeLayout rlHead;
    @BindView(R.id.etNick)
    EditText etNick;
    @BindView(R.id.rbSexMan)
    RadioButton rbSexMan;
    @BindView(R.id.rbSexWoman)
    RadioButton rbSexWoman;
    @BindView(R.id.rgSex)
    RadioGroup rgSex;
    @BindView(R.id.tvBirth)
    TextView tvBirth;
    @BindView(R.id.tvLogin)
    TextView tvLogin;
    private String password = "";
    private View rootView;
    //拍照
    private static final int PHOTO_REQUEST_CAREMA = 1;
    //相册
    public static final int SELECT_PHOTO = 2;
    private String[] reportArr = new String[]{"从手机相册选择", "拍照"};
    private BottomDialog bottomDialog;
    private int monthIndex;
    private String birthday;
    private File headFile;
    private String sex = "";
    private String phone;

    @Override
    protected View getResourceView() {
        rootView = View.inflate(getActivity(), R.layout.fragment_edit_user_info, null);
        return rootView;
    }

    @Override
    public void onClickEvent() {

        rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbSexMan:
                        sex = "1";
                        break;
                    case R.id.rbSexWoman:
                        sex = "0";
                        break;
                }
                String nick = etNick.getText().toString().trim();
                if (!TextUtils.isEmpty(nick) && headFile != null &&
                        !TextUtils.isEmpty(sex) && !TextUtils.isEmpty(birthday)) {
                    tvLogin.setBackgroundResource(R.drawable.shape_3b7aff_25_bg);
                    tvLogin.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });
        etNick.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()) && headFile != null && !TextUtils.isEmpty(sex) && !TextUtils.isEmpty(birthday)) {
                    tvLogin.setBackgroundResource(R.drawable.shape_3b7aff_25_bg);
                    tvLogin.setTextColor(getResources().getColor(R.color.white));
                } else {
                    tvLogin.setBackgroundResource(R.drawable.shape_f7f7f7_25_bg);
                    tvLogin.setTextColor(getResources().getColor(R.color.color_FF9F9F9F));
                }
            }
        });
    }

    @Override
    protected void initView(Bundle bundle) {
        context = (BasicActivity) getActivity();
        ButterKnife.bind(this, rootView);
        password = getActivity().getIntent().getStringExtra(Config.PASSWORD);
        phone = getActivity().getIntent().getStringExtra(Config.PHONE);
    }


    @OnClick({R.id.rlHead, R.id.tvBirth, R.id.tvLogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlHead:
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
            case R.id.tvBirth:
                pickBirth();
                break;
            case R.id.tvLogin:
                if (headFile == null) {
                    ToastUtils.toastCenter(getActivity(), "请选择头像");
                    return;
                }
                String nickName = etNick.getText().toString().trim();
                if (TextUtils.isEmpty(nickName)) {
                    ToastUtils.toastCenter(getActivity(), "请输入昵称");
                    return;
                }
                if (TextUtils.isEmpty(sex)) {
                    ToastUtils.toastCenter(getActivity(), "请选择性别");
                    return;
                }
                if (TextUtils.isEmpty(birthday)) {
                    ToastUtils.toastCenter(getActivity(), "请选择生日");
                    return;
                }

                register(headFile, nickName, sex, birthday);
                break;
        }
    }


    private void register(File headFile, String nickName, String sex, String birthday) {
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), headFile);
        MultipartBody.Part part = MultipartBody.Part.createFormData("headImg", headFile.getName(), fileBody);
        Http.http.createApi(AuthApi.class).register(part, nickName, birthday, sex, phone, password)

                .compose(context.<JsonResult<DataMoudle.DataBean>>bindToLifecycle())
                .compose(context.<JsonResult<DataMoudle.DataBean>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<DataMoudle.DataBean>() {
                    @Override
                    public void success(DataMoudle.DataBean response,int code) {
                        Log.d("注册: ", response + "");
                        if (response != null) {
                            String token = response.getToken();
                            String userSign = response.getUserSign();
                            SPUtils.putToken(token);
                            SPUtils.putString(Config.USERSIGN, userSign);
                            JumpUtils.jumpMainActivity(getActivity());
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
        if (resultCode == getActivity().RESULT_OK) {
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
                        Picasso.with(getActivity()).load(headFile).into(civHead);
                        String nick = etNick.getText().toString().trim();
                        if (!TextUtils.isEmpty(nick.toString()) && headFile != null &&
                                !TextUtils.isEmpty(sex) && !TextUtils.isEmpty(birthday)) {
                            tvLogin.setBackgroundResource(R.drawable.shape_3b7aff_25_bg);
                            tvLogin.setTextColor(getResources().getColor(R.color.white));
                        }
                    }
                    break;
            }
        }
    }

    /**
     * 生日
     */
    private void pickBirth() {
        View outerView1 = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_birthday_select, null);
        //年滚轮
        final WheelView wv1 = (WheelView) outerView1.findViewById(R.id.wv1);
        //月滚轮
        final WheelView wv2 = (WheelView) outerView1.findViewById(R.id.wv2);
        //日滚轮
        final WheelView wv3 = (WheelView) outerView1.findViewById(R.id.wv3);
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        wv1.setItems(Times.yearList(30), Times.getYear(50, 2020));
        wv2.setItems(Times.monthList(), 0);
        wv3.setItems(Times.dayList("2020", "01"), 0);
        wv1.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                String yearItem = wv1.getSelectedItem();
                String monthItem = wv2.getSelectedItem();
                yearItem = yearItem.substring(0, yearItem.length() - 1);
                monthItem = monthItem.substring(0, monthItem.length() - 1);
                wv2.setItems(Times.monthList(), monthIndex);
                wv3.setItems(Times.dayList(yearItem, monthItem), 0);
            }
        });
        wv2.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(int index, String item) {
                String yearItem = wv1.getSelectedItem();
                String monthItem = wv2.getSelectedItem();
                yearItem = yearItem.substring(0, yearItem.length() - 1);
                monthItem = monthItem.substring(0, monthItem.length() - 1);
                monthIndex = index;
                wv3.setItems(Times.dayList(yearItem, monthItem), 0);
            }
        });
        wv3.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
            }
        });
        TextView tv_ok = (TextView) outerView1.findViewById(R.id.tv_ok);
        TextView tv_cancel = (TextView) outerView1.findViewById(R.id.tv_cancel);
        //点击确定
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                bottomDialog.dismiss();
                String mSelectDate = wv1.getSelectedItem();
                String mSelectHour = wv2.getSelectedItem();
                String mSelectMin = wv3.getSelectedItem();
                mSelectDate = mSelectDate.substring(0, mSelectDate.length() - 1);
                mSelectHour = mSelectHour.substring(0, mSelectHour.length() - 1);
                mSelectMin = mSelectMin.substring(0, mSelectMin.length() - 1);
                birthday = mSelectDate + "-" + mSelectHour + "-" + mSelectMin;
                tvBirth.setText(birthday);
                String nick = etNick.getText().toString().trim();
                if (!TextUtils.isEmpty(nick) && headFile != null &&
                        !TextUtils.isEmpty(sex) && !TextUtils.isEmpty(birthday)) {
                    tvLogin.setBackgroundResource(R.drawable.shape_3b7aff_25_bg);
                    tvLogin.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });
        //点击取消
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                bottomDialog.dismiss();
            }
        });
        //防止弹出两个窗口
        if (bottomDialog != null && bottomDialog.isShowing()) {
            return;
        }

        bottomDialog = new BottomDialog(getActivity(), R.style.ActionSheetDialogStyle);
        //将布局设置给Dialog
        bottomDialog.setContentView(outerView1);
        bottomDialog.show();//显示对话框
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
