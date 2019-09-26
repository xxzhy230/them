package com.yijian.them.ui.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.common.Config;
import com.yijian.them.ui.home.HomeMoudle;
import com.yijian.them.ui.home.adapter.HotTopicListAdapter;
import com.yijian.them.utils.dialog.AlertUtils;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yqjr.utils.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 话题
 */
public class TagActivity extends BasicActivity {

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.lvTopicTitle)
    ListView lvTopicTitle;
    private HotTopicListAdapter hotTopicAdapter;
    private String topicName;

    @Override
    public int initView() {
        return R.layout.activity_tag;
    }

    @Override
    public void initData() {
        ButterKnife.bind(this);
        topic();
        lvTopicTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (hotTopicAdapter != null) {
                    HomeMoudle.DataBean item = hotTopicAdapter.getItem(position);
                    topicName = item.getTopicName();
                    Intent intent = new Intent();
                    intent.putExtra(Config.TOPICNAME, topicName);
                    intent.putExtra(Config.TOPICID, item.getTopicId());
                    setResult(0, intent);
                    finish();

                }
            }
        });
    }


    private void topic() {
        AlertUtils.showProgress(false, this);
        Http.http.createApi(AuthApi.class).topic()
                .compose(this.<JsonResult<List<HomeMoudle.DataBean>>>bindToLifecycle())
                .compose(this.<JsonResult<List<HomeMoudle.DataBean>>>applySchedulers())
                .subscribe(this.newSubscriber(new CallBack<List<HomeMoudle.DataBean>>() {
                    @Override
                    public void success(List<HomeMoudle.DataBean> dataBeans, int code) {
                        if (dataBeans != null && dataBeans.size() > 0) {
                            dataBeans.remove(dataBeans.get(0));
                            hotTopicAdapter = new HotTopicListAdapter(dataBeans);
                            lvTopicTitle.setAdapter(hotTopicAdapter);
                        } else {
                            AlertUtils.dismissProgress();
                        }
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(TagActivity.this, errorMessage + "");
                    }
                }));
    }


    @OnClick({R.id.ivBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
        }
    }
}
