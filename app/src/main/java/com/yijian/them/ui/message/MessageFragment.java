package com.yijian.them.ui.message;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.imsdk.TIMConversationType;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.GroupChatManagerKit;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationListLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo;
import com.yijian.them.R;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.utils.JumpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    protected View getResourceView() {
        View rootView = View.inflate(getActivity(), R.layout.fragment_message, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onClickEvent() {

    }

    @Override
    protected void initView(Bundle bundle) {
        // 未读消息监视器
        ConversationManagerKit.getInstance().addUnreadWatcher(this);
        GroupChatManagerKit.getInstance();
        TitleBarLayout titleBar = conversationLayout.getTitleBar();
        titleBar.setVisibility(View.GONE);

    }

    @Override
    public void onResume() {
        super.onResume();
        // 会话列表面板的默认 UI 和交互初始化
        conversationLayout.initDefault();
        conversationLayout.getConversationList().setOnItemClickListener(new ConversationListLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i, ConversationInfo conversationInfo) {
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setType(conversationInfo.isGroup() ? TIMConversationType.Group : TIMConversationType.C2C);
                chatInfo.setId(conversationInfo.getId());
                chatInfo.setChatName(conversationInfo.getTitle());
                JumpUtils.jumpMessageActivity(getActivity(), 0,chatInfo);
            }
        });
    }

    @OnClick({R.id.rlSystem, R.id.rlTeamMessage, R.id.rlPZ})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlSystem:
                ChatInfo chatInfo = new ChatInfo();
                JumpUtils.jumpMessageActivity(getActivity(), 0,chatInfo);
                break;
            case R.id.rlTeamMessage:
                break;
            case R.id.rlPZ:
                break;
        }
    }

    @Override
    public void updateUnread(int i) {
        System.out.println("----------------"+i);
    }

    private void startChatActivity(ConversationInfo conversationInfo) {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(conversationInfo.isGroup() ? TIMConversationType.Group : TIMConversationType.C2C);
        chatInfo.setId(conversationInfo.getId());
        chatInfo.setChatName(conversationInfo.getTitle());

    }
}
