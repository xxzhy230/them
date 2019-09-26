package com.yijian.them.ui.home.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.common.Config;
import com.yijian.them.utils.dialog.AlertUtils;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yqjr.utils.spUtils.SPUtils;
import com.yqjr.utils.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReportActivity extends BasicActivity {
    @BindView(R.id.tvCannel)
    TextView tvCannel;
    @BindView(R.id.cb1)
    CheckBox cb1;
    @BindView(R.id.cb2)
    CheckBox cb2;
    @BindView(R.id.cb3)
    CheckBox cb3;
    @BindView(R.id.cb4)
    CheckBox cb4;
    @BindView(R.id.cb5)
    CheckBox cb5;
    @BindView(R.id.cb6)
    CheckBox cb6;
    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    private String dynamicId;
    private int reportType;
    private String reportedId;
    private String commentId;
    private String tagId;
    private String teamId;

    @Override
    public int initView() {
        return R.layout.activity_report;
    }

    @Override
    public void initData() {
        ButterKnife.bind(this);

        reportType = getIntent().getIntExtra(Config.REPORTTYPE, 0);
        if (reportType == 1) {
            reportedId = getIntent().getStringExtra(Config.REPORTEDID);
            commentId = getIntent().getStringExtra(Config.COMMENTID);
        } else if (reportType == 0) {
            dynamicId = getIntent().getStringExtra(Config.DYNAMICID);
        } else if (reportType == 2) {
            tagId = getIntent().getStringExtra(Config.DYNAMICID);
        }else if (reportType == 3){
            teamId = getIntent().getStringExtra(Config.DYNAMICID);
        }
    }


    @OnClick({R.id.tvCannel, R.id.tvSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvCannel:
                finish();
                break;
            case R.id.tvSubmit:
                boolean checked1 = cb1.isChecked();
                String reason = "";
                if (checked1) {
                    reason = reason + "广告内容";
                }
                boolean checked2 = cb2.isChecked();
                if (checked2) {
                    reason = reason + "不友善内容";
                }
                boolean checked3 = cb3.isChecked();
                if (checked3) {
                    reason = reason + "违规违法内容";
                }
                boolean checked4 = cb4.isChecked();
                if (checked4) {
                    reason = reason + "抄袭";
                }
                boolean checked5 = cb5.isChecked();
                if (checked5) {
                    reason = reason + "虚假互动内容";
                }
                boolean checked6 = cb6.isChecked();
                if (checked6) {
                    reason = reason + "其他";
                }
                if (TextUtils.isEmpty(reason)) {
                    ToastUtils.toastCenter(this, "请选择举报原因");
                    return;
                }
                String content = etContent.getText().toString().trim();
                if (checked6) {
                    if (TextUtils.isEmpty(content)) {
                        ToastUtils.toastCenter(this, "请选择举报原因");
                        return;
                    }
                }
                AlertUtils.showProgress(false, this);
                if (reportType == 0) {
                    dynamicReport(reason + "," + content);
                } else if (reportType == 1) {
                    commentReport(reason + "," + content);
                } else if (reportType == 2){
                    tagReport(reason + "," + content);
                }else if (reportType == 3){
//                    teamId
                    teamReport(reason + "," + content);
                }

                break;
        }
    }

    private void teamReport(String reason) {
        AlertUtils.showProgress(false, this);
        Map map = new HashMap();
        map.put("teamId", teamId);
        map.put("reason", reason);
        map.put("reportBy", SPUtils.getInt(Config.USERID) + "");
        map.put("reportedId", reportedId);
        Http.http.createApi(AuthApi.class).teamReport(map)
                .compose(this.<JsonResult<String>>bindToLifecycle())
                .compose(this.<JsonResult<String>>applySchedulers())
                .subscribe(this.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String response, int code) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(ReportActivity.this, "举报成功");
                        finish();
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(ReportActivity.this, errorMessage + "");
                    }
                }));
    }

    private void commentReport(String reason) {
        AlertUtils.showProgress(false, this);
        Map map = new HashMap();
        map.put("commentId", commentId);
        map.put("reason", reason);
        map.put("reportBy", SPUtils.getInt(Config.USERID) + "");
        map.put("reportedId", reportedId);
        Http.http.createApi(AuthApi.class).commentReport(map)
                .compose(this.<JsonResult<String>>bindToLifecycle())
                .compose(this.<JsonResult<String>>applySchedulers())
                .subscribe(this.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String response, int code) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(ReportActivity.this, "提交成功");
                        finish();
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(ReportActivity.this, errorMessage + "");
                    }
                }));
    }

    private void dynamicReport(String reason) {
        AlertUtils.showProgress(false, this);
        Http.http.createApi(AuthApi.class).dynamicReport(dynamicId + "", reason, SPUtils.getInt(Config.USERID) + "")
                .compose(this.<JsonResult<String>>bindToLifecycle())
                .compose(this.<JsonResult<String>>applySchedulers())
                .subscribe(this.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String response, int code) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(ReportActivity.this, "提交成功");
                        finish();
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(ReportActivity.this, errorMessage + "");
                    }
                }));
    }

    private void tagReport(String reason) {
        AlertUtils.showProgress(false, this);
        Map map = new HashMap();
        map.put("reason", reason);
        map.put("tagId", Integer.parseInt(tagId));

        Http.http.createApi(AuthApi.class).tagReport(map)
                .compose(this.<JsonResult<String>>bindToLifecycle())
                .compose(this.<JsonResult<String>>applySchedulers())
                .subscribe(this.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String response, int code) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(ReportActivity.this, "提交成功");
                        finish();
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(ReportActivity.this, errorMessage + "");
                    }
                }));
    }
}
