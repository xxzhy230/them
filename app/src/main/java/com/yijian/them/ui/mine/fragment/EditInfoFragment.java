package com.yijian.them.ui.mine.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.yijian.them.utils.dialog.DialogOnitem;
import com.yijian.them.utils.dialog.SealectImageDialog;
import com.yijian.them.utils.fragments.Fragments;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yijian.them.view.CircleImageView;
import com.yqjr.utils.spUtils.SPUtils;
import com.yqjr.utils.utils.ToastUtils;
import com.yqjr.utils.wheel.BottomDialog;
import com.yqjr.utils.wheel.WheelView;
import com.yqjr.utils.wheel.WheelViewUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

public class EditInfoFragment extends BasicFragment {
    @BindView(R.id.civHead)
    CircleImageView civHead;
    @BindView(R.id.llHead)
    LinearLayout llHead;
    @BindView(R.id.tvNick)
    TextView tvNick;
    @BindView(R.id.llNick)
    LinearLayout llNick;
    @BindView(R.id.tvSex)
    TextView tvSex;
    @BindView(R.id.llSex)
    LinearLayout llSex;
    @BindView(R.id.tvBirthday)
    TextView tvBirthday;
    @BindView(R.id.llBirthday)
    LinearLayout llBirthday;
    @BindView(R.id.tvRemark)
    TextView tvRemark;
    @BindView(R.id.llRemark)
    LinearLayout llRemark;
    private int monthIndex;
    private BottomDialog bottomDialog;
    private String birthday;
    private List<String> sexList = new ArrayList<>();
    private int sex;
    //拍照
    private static final int PHOTO_REQUEST_CAREMA = 1;
    //相册
    public static final int SELECT_PHOTO = 2;
    private String[] reportArr = new String[]{"从手机相册选择", "拍照"};
    private File headFile;

    @Override
    protected View getResourceView() {
        View rootView = View.inflate(getActivity(), R.layout.fragment_edit_info, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onClickEvent() {
        sexList.clear();
        String[] stringArray = getResources().getStringArray(R.array.sex);
        for (int i = 0; i < stringArray.length; i++) {
            sexList.add(stringArray[i]);
        }
    }

    @Override
    protected void initView(Bundle bundle) {
        context = (BasicActivity) getActivity();
        String headImage = SPUtils.getString(Config.REALIMG);
        if (!TextUtils.isEmpty(headImage)) {
            Picasso.with(getActivity()).load(headImage).into(civHead);
        }

        String gender = SPUtils.getString(Config.GENDER);
        if ("1".equals(gender)) {
            tvSex.setText("男");
        } else {
            tvSex.setText("女");
        }

        String birthday = SPUtils.getString(Config.BIRTHDAY);
        tvBirthday.setText(birthday);
    }

    @Override
    public void onResume() {
        super.onResume();
        String nickName = SPUtils.getString(Config.NICKNAME);
        tvNick.setText(nickName);
        String sign = SPUtils.getString(Config.SIGN);
        tvRemark.setText(sign);
    }

    @OnClick({R.id.llHead, R.id.llNick, R.id.llSex, R.id.llBirthday, R.id.llRemark})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llHead:
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
            case R.id.llNick:
                JumpUtils.jumpMineActivity(getActivity(),10,"更改昵称");
                break;
            case R.id.llSex:
//                getSex();
                break;
            case R.id.llBirthday:
                pickBirth();
                break;
            case R.id.llRemark:
                JumpUtils.jumpMineActivity(getActivity(),11,"个性签名");

                break;
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
//                tvBirthday.setText(birthday);
                setUser(3);
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
     * 性别
     */
    private void getSex() {
        BottomDialog sexDialog = WheelViewUtils.init(getActivity())
                .setTitle("选择性别")
                .setTitleBg(R.color.transparent)
                .setTextColorCenter(R.color.color_3B7AFF)
                .setTextSizeCenter(16)
                .setItemVisible(5)
                .setTextSizeOuter(14)
                .setLineColor(R.color.line_color)
                .setConfirmTitle("确定")
                .setConfirmTextSize(16)
                .setCancelTitle("取消")
                .setConfirmColor(R.color.color_3B7AFF)
                .setBackgroundColor(R.drawable.white_bg)
                .setData(sexList, 1)
                .setOnSubmitListener(new WheelViewUtils.OnSubmitListener() {
                    @Override
                    public void onSubmit(int i, String s, int i1) {
                        if ("男".equals(s)) {
                            sex = 1;
                        } else {
                            sex = 0;
                        }
                        setUser(2);
                    }
                })
                .build();
        sexDialog.show();
    }

    /**
     * 修改用户信息
     */
    private void setUser(final int type) {
        AuthApi api = Http.http.createApi(AuthApi.class);
        Observable<JsonResult<DataMoudle.DataBean>> jsonResultObservable = null;
        if (type == 1) {//头像
            RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), headFile);
            MultipartBody.Part part = MultipartBody.Part.createFormData("headImg", headFile.getName(), fileBody);
            jsonResultObservable = api.setUserheadImg(part);
        } else if (type == 2) {//性别
            jsonResultObservable = api.setUserSex(sex + "");
        } else if (type == 3) {//生日
            jsonResultObservable = api.setUserBirthday(birthday);
        }

        jsonResultObservable.compose(context.<JsonResult<DataMoudle.DataBean>>bindToLifecycle())
                .compose(context.<JsonResult<DataMoudle.DataBean>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<DataMoudle.DataBean>() {
                    @Override
                    public void success(DataMoudle.DataBean response,int code) {
                        if (type == 1) {//头像
                            Picasso.with(getActivity()).load(headFile).into(civHead);
                        } else if (type == 2) {//性别
                            if (sex == 1) {
                                tvSex.setText("男");
                            } else {
                                tvSex.setText("女");
                            }
                        } else if (type == 3) {//生日
                            tvBirthday.setText(birthday);
                            SPUtils.putString(Config.BIRTHDAY, birthday);
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
                        setUser(1);
//                        Picasso.with(getActivity()).load(headFile).into(civHead);
                    }
                    break;
            }
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
