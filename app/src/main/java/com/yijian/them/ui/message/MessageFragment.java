package com.yijian.them.ui.message;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.GroupChatManagerKit;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationListAdapter;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationListLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo;
import com.yijian.them.R;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.common.Config;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.dialog.ConversDialog;
import com.yqjr.utils.spUtils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MessageFragment extends BasicFragment implements ConversationManagerKit.MessageUnreadWatcher {
    @BindView(R.id.conversation_layout)
    ConversationLayout conversationLayout;
    @BindView(R.id.tvSystemNum)
    TextView tvSystemNum;
    @BindView(R.id.rlSystem)
    RelativeLayout rlSystem;
    @BindView(R.id.tvTeamNum)
    TextView tvTeamNum;
    @BindView(R.id.rlTeamMessage)
    RelativeLayout rlTeamMessage;
    @BindView(R.id.tvPZNum)
    TextView tvPZNum;
    @BindView(R.id.rlPZ)
    RelativeLayout rlPZ;
    @BindView(R.id.ivDefault)
    ImageView ivDefault;
    @BindView(R.id.tvDefault)
    TextView tvDefault;
    @BindView(R.id.llDefault)
    LinearLayout llDefault;
    private ConversationListAdapter adapter;

    @Override
    protected View getResourceView() {
        View rootView = View.inflate(getActivity(), R.layout.fragment_message, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onClickEvent() {
        GroupChatManagerKit.getInstance();
    }

    @Override
    protected void initView(Bundle bundle) {
        // 未读消息监视器
        ConversationManagerKit.getInstance().addUnreadWatcher(this);
        TitleBarLayout titleBar = conversationLayout.getTitleBar();
        titleBar.setVisibility(View.GONE);

    }

    private void setConvers() {
        ConversationListLayout listLayout = conversationLayout.getConversationList();
        listLayout.setItemTopTextSize(16); // 设置 item 中 top 文字大小
        listLayout.setItemBottomTextSize(12);// 设置 item 中 bottom 文字大小
        listLayout.setItemDateTextSize(10);// 设置 item 中 timeline 文字大小
        listLayout.enableItemRoundIcon(true);// 设置 item 头像是否显示圆角，默认是方形
        listLayout.disableItemUnreadDot(true);// 设置 item 是否不显示未读红点，默认显示
        // 长按弹出菜单
        listLayout.setOnItemLongClickListener(new ConversationListLayout.OnItemLongClickListener() {
            @Override
            public void OnItemLongClick(View view, int position, ConversationInfo conversationInfo) {
                startPopShow(view, position, conversationInfo);
            }
        });

        conversationLayout.getConversationList().setOnItemClickListener(new ConversationListLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i, ConversationInfo conversationInfo) {
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setType(conversationInfo.isGroup() ? TIMConversationType.Group : TIMConversationType.C2C);
                chatInfo.setId(conversationInfo.getId());
                chatInfo.setChatName(conversationInfo.getTitle());
                JumpUtils.jumpMessageActivity(getActivity(), 0, chatInfo);
            }
        });
    }

    private void startPopShow(View view, final int position, ConversationInfo conversationInfo) {
        ConversDialog conversDialog = new ConversDialog(getActivity());
        int unRead = conversationInfo.getUnRead();
        final String id = conversationInfo.getId();
        conversDialog.show();
        boolean top = conversationInfo.isTop();
        conversDialog.isTop(top);
        if (unRead > 0) {
            conversDialog.isRead(false);
        } else {
            conversDialog.isRead(true);
        }
        conversDialog.setOnClicklistener(new ConversDialog.OnClicklistener() {
            @Override
            public void onClick(int type) {
                if (type == 1) {//置顶
                    ConversationManagerKit.getInstance().setConversationTop(id, true);
                } else if (type == 2) {//取消置顶
                    ConversationManagerKit.getInstance().setConversationTop(id, false);
                } else if (type == 3) {//标记已读
                    ConversationManagerKit.getInstance().updateUnreadTotal(0);
                } else if (type == 4) {//标记未读
                    ConversationManagerKit.getInstance().updateUnreadTotal(1);
                } else if (type == 5) {//删除
                    TIMManager.getInstance().deleteConversation(TIMConversationType.Group, id);
                    adapter.removeItem(position);
                    if (adapter.getItemCount() == 0) {
                        llDefault.setVisibility(View.VISIBLE);
                        tvDefault.setText("暂无消息");
                        ivDefault.setImageResource(R.mipmap.default_message);
                    } else {
                        llDefault.setVisibility(View.GONE);
                    }
                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        // 会话列表面板的默认 UI 和交互初始化
        conversationLayout.initDefault();
        adapter = conversationLayout.getConversationList().getAdapter();
        int itemCount = adapter.getItemCount();
        String groupId = SPUtils.getString(Config.DELTEAMCHAT);
        if (!TextUtils.isEmpty(groupId)) {
            for (int i = 0; i < itemCount; i++) {
                ConversationInfo item = adapter.getItem(i);
                String id = item.getId();
                if (id.equals(groupId)) {
                    boolean group = item.isGroup();
                    if (group) {
                        TIMManager.getInstance().deleteConversation(TIMConversationType.Group, id);
                    } else {
                        TIMManager.getInstance().deleteConversation(TIMConversationType.C2C, id);
                    }
                    adapter.removeItem(i);
                    break;
                }
            }
            SPUtils.putString(Config.DELTEAMCHAT, null);
        }
        if (itemCount == 0) {
            llDefault.setVisibility(View.VISIBLE);
            tvDefault.setText("暂无消息");
            ivDefault.setImageResource(R.mipmap.default_message);
        } else {
            llDefault.setVisibility(View.GONE);
        }
        setConvers();
    }

    @OnClick({R.id.rlSystem, R.id.rlTeamMessage, R.id.rlPZ})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlSystem:
                JumpUtils.jumpMessageActivity(getActivity(), 2, null);
                break;
            case R.id.rlTeamMessage:
                JumpUtils.jumpMessageActivity(getActivity(), 4, null);
                break;
            case R.id.rlPZ:
                JumpUtils.jumpMessageActivity(getActivity(), 3, null);
                break;
        }
    }

    @Override
    public void updateUnread(int i) {
    }

}
