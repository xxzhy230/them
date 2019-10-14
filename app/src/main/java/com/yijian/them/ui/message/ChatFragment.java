package com.yijian.them.ui.message;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.qcloud.tim.uikit.component.NoticeLayout;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.ChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatManagerKit;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageLayout;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.yijian.them.R;
import com.yijian.them.common.Config;
import com.yijian.them.utils.JumpUtils;
import com.yqjr.utils.Utils;
import com.yqjr.utils.base.BaseFragment;
import com.yqjr.utils.spUtils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        chatManager = chatLayout.getChatManager();
        TitleBarLayout titleBar = chatLayout.getTitleBar();
        titleBar.getRightIcon().setImageResource(R.mipmap.home_more);
        titleBar.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TIMConversationType type = chatInfo.getType();
                String id = chatInfo.getId();
                if (type.equals(TIMConversationType.C2C)) {
                    JumpUtils.jumpMessageActivity(getActivity(), 1, chatInfo);
                } else {
                    if (id.contains("teamId")) {
                        JumpUtils.jumpMessageActivity(getActivity(), 1, chatInfo);
                    } else {
                        JumpUtils.jumpMessageActivity(getActivity(), 5, chatInfo);
                    }
                }
            }
        });
        TIMConversationType type = chatInfo.getType();
        if (type.equals(TIMConversationType.C2C)) {
            List<String> mList = new ArrayList<>();
            mList.add(chatInfo.getId());
            mList.add(SPUtils.getInt(Config.USERID) + "");
            TIMFriendshipManager.getInstance().getUsersProfile(mList, true, new TIMValueCallBack<List<TIMUserProfile>>() {
                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                    for (int i = 0; i < timUserProfiles.size(); i++) {
                        Log.d("头像", timUserProfiles.get(i).getFaceUrl());
                    }
                }
            });
        } else {
            setHeadImage();
        }
        setNotice();
        setMessageUserNick();
    }
    @Override
    public void onResume() {
        super.onResume();

        String groupTitle = SPUtils.getString(Config.GROUPTITLE);
        if (!TextUtils.isEmpty(groupTitle)) {
            chatLayout.getTitleBar().setTitle(groupTitle, TitleBarLayout.POSITION.MIDDLE);
            SPUtils.putString(Config.GROUPTITLE, null);
        }
    }



    private void setNotice() {
        // 从 ChatLayout 里获取 NoticeLayout
        NoticeLayout noticeLayout = chatLayout.getNoticeLayout();
// 可以使通知区域一致展示
        noticeLayout.alwaysShow(false);
        noticeLayout.setVisibility(View.GONE);
    }

    private void setMessageUserNick() {
// 从 ChatLayout 里获取 MessageLayout
        final MessageLayout messageLayout = chatLayout.getMessageLayout();
////// 设置头像 //////
// 设置默认头像，默认与朋友与自己的头像相同
        messageLayout.setAvatar(R.mipmap.register_icon_avatar);
// 设置头像圆角，不设置则默认不做圆角处理
        messageLayout.setAvatarRadius(50);
// 设置头像大小
        messageLayout.setAvatarSize(new int[]{48, 48});
        messageLayout.setOnItemClickListener(new MessageLayout.OnItemClickListener() {
            @Override
            public void onMessageLongClick(View view, int position, MessageInfo messageInfo) {
                messageLayout.showItemPopMenu(position, messageInfo, view);
            }

            @Override
            public void onUserIconClick(View view, int position, MessageInfo messageInfo) {

            }
        });
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
