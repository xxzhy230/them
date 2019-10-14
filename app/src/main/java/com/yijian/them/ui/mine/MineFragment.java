package com.yijian.them.ui.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;
import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.common.Config;
import com.yijian.them.ui.login.DataMoudle;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.Times;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yijian.them.view.CircleImageView;
import com.yqjr.utils.Utils;
import com.yqjr.utils.spUtils.SPUtils;
import com.yqjr.utils.utils.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MineFragment extends BasicFragment {

    @BindView(R.id.civHead)
    CircleImageView civHead;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvRemark)
    TextView tvRemark;
    @BindView(R.id.tvDongTai)
    TextView tvDongTai;
    @BindView(R.id.llDongTai)
    LinearLayout llDongTai;
    @BindView(R.id.tvZan)
    TextView tvZan;
    @BindView(R.id.llZan)
    LinearLayout llZan;
    @BindView(R.id.tvGuanzhu)
    TextView tvGuanzhu;
    @BindView(R.id.llGuanzhu)
    LinearLayout llGuanzhu;
    @BindView(R.id.tvFensi)
    TextView tvFensi;
    @BindView(R.id.llFensi)
    LinearLayout llFensi;
    @BindView(R.id.llEditInfo)
    LinearLayout llEditInfo;
    @BindView(R.id.llFeedback)
    LinearLayout llFeedback;
    @BindView(R.id.tvVersionInfo)
    TextView tvVersionInfo;
    @BindView(R.id.llVersionInfo)
    LinearLayout llVersionInfo;
    @BindView(R.id.llAboutThem)
    LinearLayout llAboutThem;
    @BindView(R.id.llSetting)
    LinearLayout llSetting;

    @Override
    protected View getResourceView() {
        View rootView = View.inflate(getActivity(), R.layout.fragment_mine, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onClickEvent() {

    }

    @Override
    protected void initView(Bundle bundle) {
        context = (BasicActivity) getActivity();
        getUserInfo();
        String versionCode = Utils.getVersionName();
        tvVersionInfo.setText("当前版本V" + versionCode);
    }

    private void getStatistics() {
        Http.http.createApi(AuthApi.class).getStatistics(SPUtils.getInt(Config.USERID) + "")
                .compose(context.<JsonResult<DataMoudle.DataBean>>bindToLifecycle())
                .compose(context.<JsonResult<DataMoudle.DataBean>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<DataMoudle.DataBean>() {
                    @Override
                    public void success(DataMoudle.DataBean response, int code) {
                        Log.d("获取统计数据", response + "");
                        int dynamicCount = response.getDynamicCount();
                        int fansCount = response.getFansCount();
                        int followingCount = response.getFollowingCount();
                        int likeCount = response.getLikeCount();

                        tvDongTai.setText(dynamicCount + "");
                        tvFensi.setText(fansCount + "");
                        tvGuanzhu.setText(followingCount + "");
                        tvZan.setText(likeCount + "");
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }

    private void getUserInfo() {
        Http.http.createApi(AuthApi.class).getUser()
                .compose(context.<JsonResult<DataMoudle.DataBean>>bindToLifecycle())
                .compose(context.<JsonResult<DataMoudle.DataBean>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<DataMoudle.DataBean>() {
                    @Override
                    public void success(DataMoudle.DataBean response, int code) {
                        Log.d("获取用户信息: ", response + "");
                        String birthday = response.getBirthday();
                        String gender = response.getGender();
                        String nickName = response.getNickName();
                        int userId = response.getUserId();
                        String regn = response.getRegn();
                        String realImg = response.getRealImg();
                        String sign = response.getSign();

                        SPUtils.putString(Config.BIRTHDAY, birthday);
                        SPUtils.putString(Config.GENDER, gender);
                        SPUtils.putString(Config.NICKNAME, nickName);
                        SPUtils.putInt(Config.USERID, userId);
                        SPUtils.putString(Config.REGN, regn);
                        SPUtils.putString(Config.REALIMG, realImg);
                        SPUtils.putString(Config.SIGN, sign);

                        Picasso.with(getActivity()).load(realImg).into(civHead);
                        tvUserName.setText(nickName);
                        tvRemark.setText(sign);
                        String editUserInfo = SPUtils.getString(Config.EDITUSERINFO);
                        if (!TextUtils.isEmpty(editUserInfo)) {
                            setUserInfo(nickName,  gender, realImg, sign);
                            SPUtils.putString(Config.EDITUSERINFO, "");
                        }

                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }


    @OnClick({R.id.llUserInfo, R.id.llDongTai, R.id.llZan, R.id.llGuanzhu, R.id.llFensi, R.id.llEditInfo, R.id.llFeedback, R.id.llVersionInfo, R.id.llAboutThem, R.id.llSetting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llUserInfo:
                JumpUtils.jumpUserInfoActivity(getActivity(), SPUtils.getInt(Config.USERID));
                break;
            case R.id.llDongTai:
                JumpUtils.jumpUserInfoActivity(getActivity(), SPUtils.getInt(Config.USERID));
                break;
            case R.id.llZan:
                JumpUtils.jumpUserInfoActivity(getActivity(), SPUtils.getInt(Config.USERID));
                break;
            case R.id.llGuanzhu:
                JumpUtils.jumpUserInfoActivity(getActivity(), SPUtils.getInt(Config.USERID));
                break;
            case R.id.llFensi:
                JumpUtils.jumpUserInfoActivity(getActivity(), SPUtils.getInt(Config.USERID));
                break;
            case R.id.llEditInfo:
                JumpUtils.jumpMineActivity(getActivity(), 0, "资料修改");
                break;
            case R.id.llFeedback:
                JumpUtils.jumpMineActivity(getActivity(), 1, "用户反馈");
                break;
            case R.id.llVersionInfo:
                JumpUtils.jumpMineActivity(getActivity(), 3, "版本信息");
                break;
            case R.id.llAboutThem:
                JumpUtils.jumpMineActivity(getActivity(), 3, "关于Them");
                break;
            case R.id.llSetting:
                JumpUtils.jumpMineActivity(getActivity(), 4, "设置");
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String editUserInfo = SPUtils.getString(Config.EDITUSERINFO);
        if (!TextUtils.isEmpty(editUserInfo)) {
            getUserInfo();
        }
        getStatistics();
    }

    /**
     * 设置个人信息
     *
     * @param nickName
     * @param gender
     * @param realImg  * @param sign
     */
    private void setUserInfo(String nickName, String gender, String realImg, String sign) {
        HashMap<String, Object> map = new HashMap();
        map.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_NICK, nickName);
        map.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_GENDER, gender);
        map.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_FACEURL, realImg);
        map.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_SELFSIGNATURE, sign);
        TIMFriendshipManager.getInstance().modifySelfProfile(map, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.d("修改IM资料", "失败----" + s);
            }

            @Override
            public void onSuccess() {
                Log.d("修改IM资料", "成功");
            }
        });
    }
}
