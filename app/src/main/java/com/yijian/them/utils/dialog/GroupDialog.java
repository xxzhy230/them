package com.yijian.them.utils.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.yijian.them.R;
import com.yijian.them.utils.JumpUtils;
import com.yqjr.utils.utils.ToastUtils;

public class GroupDialog extends BaseDialog {
    private String groupId;
    private String groupName;
    private OnJoinListener onJoinListener;

    public void setOnJoinListener(OnJoinListener onJoinListener) {
        this.onJoinListener = onJoinListener;
    }

    public GroupDialog(Context context, String groupId, String groupName) {
        super(context);
        this.groupId = groupId;
        this.groupName = groupName;
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.dialog_group, null);

        TextView tvAddGroup = view.findViewById(R.id.tvAddGroup);
        setContentView(view);
        tvAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onJoinListener != null) {
                    if (groupId.contains("teamId")) {//添加小队
                        onJoinListener.onJoinTeam(groupId,groupName);
                    } else {//加入群聊
                        joinGroup();
                    }
                }
            }
        });
    }


    private void joinGroup() {
        TIMGroupManager.getInstance().applyJoinGroup(groupId, "", new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                dismiss();
                ToastUtils.toastCenter(getContext(), s);
            }

            @Override
            public void onSuccess() {
                dismiss();
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setId(groupId);
                chatInfo.setType(TIMConversationType.Group);
                chatInfo.setChatName(groupName);
                JumpUtils.jumpMessageActivity(getContext(), 0, chatInfo);
            }
        });
    }

    @Override
    public void show() {
        try {
            Context context = getContext();
            if (context != null && context instanceof Activity) {
                if (((Activity) context).isFinishing()) {
                    return;
                }
            }
            if (isShowing()) {
                return;
            }
            super.show();
        } catch (Throwable t) {

        }
    }

    public interface OnJoinListener {
        void onJoinTeam(String teamId,String teamName);
    }
}
