package com.yijian.them.ui.message;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.ui.home.HomeMoudle;
import com.yijian.them.ui.message.adapter.SystemMessageAdapter;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yqjr.utils.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SystemMessageFragment extends BasicFragment {
    @BindView(R.id.ivDefault)
    ImageView ivDefault;
    @BindView(R.id.tvDefault)
    TextView tvDefault;
    @BindView(R.id.llDefault)
    LinearLayout llDefault;
    @BindView(R.id.lvSystem)
    ListView lvSystem;
    private SystemMessageAdapter adapter;

    @Override
    protected void onClickEvent() {

    }

    @Override
    protected View getResourceView() {
        View view = View.inflate(getActivity(), R.layout.fragment_system_message, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView(Bundle bundle) {
        context = (BasicActivity)getActivity();
        adapter = new SystemMessageAdapter();
        lvSystem.setAdapter(adapter);
        systemMessage();
        lvSystem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HomeMoudle.DataBean item = adapter.getItem(position);
                String contentUrl = item.getContentUrl();
                String title = item.getTitle();
                JumpUtils.jumpWebActivity(getActivity(),title,contentUrl);
            }
        });
    }

    private void systemMessage() {
        Http.http.createApi(AuthApi.class).systemMessage()
                .compose(context.<JsonResult<List<HomeMoudle.DataBean>>>bindToLifecycle())
                .compose(context.<JsonResult<List<HomeMoudle.DataBean>>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<List<HomeMoudle.DataBean>>() {
                    @Override
                    public void success(List<HomeMoudle.DataBean> response, int code) {
                        if (response != null && response.size() > 0) {
                            llDefault.setVisibility(View.GONE);
                            adapter.setData(response);
                        } else {
                            llDefault.setVisibility(View.VISIBLE);
                            tvDefault.setText("暂无系统消息");
                            ivDefault.setImageResource(R.mipmap.default_message);
                        }
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }

}
