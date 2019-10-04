package com.yijian.them.ui.message.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.common.Config;
import com.yijian.them.ui.message.MessageActivity;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yqjr.utils.spUtils.SPUtils;
import com.yqjr.utils.utils.ToastUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditTeamFragment extends BasicFragment {
    @BindView(R.id.etNickOrRemark)
    EditText etNickOrRemark;
    @BindView(R.id.tvTeamTitle)
    TextView tvTeamTitle;
    private int type = 7;  //7 小队名称  8 签名
    private ChatInfo chatInfo;

    public void setType(int type) {
        this.type = type;
    }

    @Override
    protected View getResourceView() {
        View rootView = View.inflate(getActivity(), R.layout.fragment_edit_team, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onClickEvent() {
        showInput();
    }

    @Override
    protected void initView(Bundle bundle) {
        context = (BasicActivity) getActivity();
        if (type == 7) {
            tvTeamTitle.setText("修改群名称");
            String chatName = chatInfo.getChatName();
            etNickOrRemark.setHint(chatName + "    限制16个字");
            etNickOrRemark.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        } else {
            tvTeamTitle.setText("修改群公告");
            String desc = chatInfo.getDesc();
            etNickOrRemark.setHint(desc + "   限制40个字");
            etNickOrRemark.setFilters(new InputFilter[]{new InputFilter.LengthFilter(40)});
        }
    }

    private void showInput() {
        Timer timer = new Timer();//设置定时器
        timer.schedule(new TimerTask() {
            @Override
            public void run() {//弹出软键盘的代码
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etNickOrRemark, InputMethodManager.RESULT_SHOWN);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }, 300);//设置300毫秒的时长

    }

    public void setChatInfo(ChatInfo chatInfo, int type) {
        this.chatInfo = chatInfo;
        this.type = type;
    }


    @OnClick({R.id.tvCannel, R.id.tvState})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvCannel:
                getActivity().finish();
                break;
            case R.id.tvState:
                String trim = etNickOrRemark.getText().toString().trim();
                if (type == 7) {
                    if (TextUtils.isEmpty(trim)) {
                        ToastUtils.toastCenter(getActivity(), "请输入群名称");
                        return;
                    }
                    editTeamName(trim);
                } else {
                    if (TextUtils.isEmpty(trim)) {
                        ToastUtils.toastCenter(getActivity(), "请输入群公告");
                        return;
                    }
                    editTeamDesc(trim);
                }
                break;
        }
    }

    /**
     * 修改群名称
     *
     * @param teamName
     */
    private void editTeamName(final String teamName) {
        final String groupId = chatInfo.getId();
        Http.http.createApi(AuthApi.class).editTeamName(groupId.replace("team:teamId:", ""), teamName)
                .compose(context.<JsonResult<String>>bindToLifecycle())
                .compose(context.<JsonResult<String>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String response, int code) {
                        ToastUtils.toastCenter(getActivity(), "修改成功");
                        chatInfo.setChatName(teamName);
                        SPUtils.putString(Config.GROUPTITLE, teamName);
                        Intent intent = new Intent();
                        intent.putExtra("teamName",teamName);
                        getActivity().setResult(0, intent);
                        getActivity().finish();
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }

    /**
     * 修改群公告
     *
     * @param teamDesc
     */
    private void editTeamDesc(final String teamDesc) {
        final String groupId = chatInfo.getId();
        Http.http.createApi(AuthApi.class).editTeamDesc(groupId.replace("team:teamId:", ""), teamDesc)
                .compose(context.<JsonResult<String>>bindToLifecycle())
                .compose(context.<JsonResult<String>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String response, int code) {
                        ToastUtils.toastCenter(getActivity(), "修改成功");
                        chatInfo.setDesc(teamDesc);
                        getActivity().finish();

                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }
}
