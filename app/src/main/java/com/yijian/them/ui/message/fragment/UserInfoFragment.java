package com.yijian.them.ui.message.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.squareup.picasso.Picasso;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;
import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.common.Config;
import com.yijian.them.ui.home.HomeMoudle;
import com.yijian.them.ui.message.MessageActivity;
import com.yijian.them.ui.team.adapter.GroupMembersAdapter;
import com.yijian.them.ui.team.moudle.TeamInfoMoudle;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.dialog.AlertUtils;
import com.yijian.them.utils.dialog.DialogOnitem;
import com.yijian.them.utils.dialog.SealectImageDialog;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yqjr.utils.base.AppManager;
import com.yqjr.utils.spUtils.SPUtils;
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

public class UserInfoFragment extends BasicFragment {

    @BindView(R.id.v_include_bar)
    View vIncludeBar;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvGroupTitle)
    TextView tvGroupTitle;
    @BindView(R.id.sGroupTop)
    Switch sGroupTop;
    @BindView(R.id.llBlackList)
    LinearLayout llBlackList;
    @BindView(R.id.llReport)
    LinearLayout llReport;
    private ChatInfo chatInfo;
    private String userId;
    private boolean topConversation;

    @Override
    protected void onClickEvent() {
    }

    @Override
    protected View getResourceView() {
        View view = View.inflate(getActivity(), R.layout.fragment_user_info, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView(Bundle bundle) {
        context = (BasicActivity) getActivity();
        userId = chatInfo.getId();
        topConversation = ConversationManagerKit.getInstance().isTopConversation(userId);
        if (topConversation) {
            sGroupTop.setChecked(true);
        } else {
            sGroupTop.setChecked(false);
        }
        sGroupTop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ConversationManagerKit.getInstance().setConversationTop(userId, true);
                } else {
                    ConversationManagerKit.getInstance().setConversationTop(userId, false);
                }
            }
        });
    }

    public void setChatInfo(ChatInfo chatInfo) {
        this.chatInfo = chatInfo;
    }
    @OnClick({R.id.ivBack, R.id.llBlackList, R.id.llReport})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                getActivity().finish();
                break;
            case R.id.llBlackList:
                blackUser();
                break;
            case R.id.llReport:
                JumpUtils.jumpReportActivity(getActivity(), chatInfo.getId(), 1, "", userId);
                break;
        }
    }

    /**
     * 加入黑名单
     */
    private void blackUser() {
        AlertUtils.showProgress(false, getActivity());
        Http.http.createApi(AuthApi.class).blackUser(userId + "")
                .compose(context.<JsonResult<String>>bindToLifecycle())
                .compose(context.<JsonResult<String>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String response, int code) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(getActivity(),"加入黑名单成功");
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }
}