package com.yijian.them.ui.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.ChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatManagerKit;
import com.yijian.them.R;
import com.yijian.them.common.App;
import com.yijian.them.utils.JumpUtils;
import com.yqjr.utils.Utils;
import com.yqjr.utils.base.BaseActivity;
import com.yqjr.utils.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ChatFragment extends BaseFragment {
    @BindView(R.id.chat_layout)
    ChatLayout chatLayout;
    @BindView(R.id.v_include_bar)
    View vIncludeBar;
    private ChatInfo chatInfo;
    private ChatManagerKit chatManager;

    @Override
    protected View getResourceView() {
        View view = View.inflate(getActivity(), R.layout.fragment_chart, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onClickEvent() {
        if (vIncludeBar != null) {
            ViewGroup.LayoutParams layoutParams = vIncludeBar.getLayoutParams();
            layoutParams.height = Utils.stateHeight;
            vIncludeBar.setLayoutParams(layoutParams);
            vIncludeBar.setBackgroundResource(R.color.color_69);
        }
    }

    @Override
    protected void initView(Bundle bundle) {
// 单聊面板的默认 UI 和交互初始化
        chatLayout.initDefault();
// 传入 ChatInfo 的实例，这个实例必须包含必要的聊天信息，一般从调用方传入
        chatLayout.setChatInfo(chatInfo);
//        chatLayout.getMessageLayout().setBackgroundResource(R.color.white);
//        chatLayout.getInputLayout().setBackgroundResource(R.color.white);
        chatManager = chatLayout.getChatManager();
        TitleBarLayout titleBar = chatLayout.getTitleBar();
        titleBar.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtils.jumpMessageActivity(getActivity(), 1, chatInfo);
            }
        });
        setHeadImage();
    }

    private void setHeadImage() {
        if (chatInfo == null) {
            return;
        }
        TIMGroupManager.getInstance().getGroupMembers(chatInfo.getId(), new TIMValueCallBack<List<TIMGroupMemberInfo>>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<TIMGroupMemberInfo> timGroupMemberInfos) {
                List<String> mList = new ArrayList<>();
                for (TIMGroupMemberInfo timGroupMemberInfo : timGroupMemberInfos) {
                    mList.add(timGroupMemberInfo.getUser());
                }
                TIMFriendshipManager.getInstance().getUsersProfile(mList, true, new TIMValueCallBack<List<TIMUserProfile>>() {
                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                    }
                });
            }
        });
        TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
            @Override
            public void onError(int i, String s) {
            }

            @Override
            public void onSuccess(TIMUserProfile timUserProfile) {
            }
        });
    }

    public void setChatInfo(ChatInfo chatInfo) {
        this.chatInfo = chatInfo;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatManager != null) {
            chatManager.destroyChat();
        }
    }
}
