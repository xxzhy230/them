package com.yijian.them.ui.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.ui.home.HomeMoudle;
import com.yijian.them.ui.message.adapter.DynamicMessageAdapter;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yqjr.utils.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DynamicMessageFragment extends BasicFragment {
    @BindView(R.id.ivDefault)
    ImageView ivDefault;
    @BindView(R.id.tvDefault)
    TextView tvDefault;
    @BindView(R.id.llDefault)
    LinearLayout llDefault;
    @BindView(R.id.lvSystem)
    ListView lvSystem;
    private DynamicMessageAdapter dynamicMessageAdapter;

    @Override
    protected void onClickEvent() {

    }

    @Override
    protected View getResourceView() {
        View view = View.inflate(getActivity(), R.layout.fragment_dynamic_message, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView(Bundle bundle) {
        context = (BasicActivity) getActivity();
        dynamicMessageAdapter = new DynamicMessageAdapter();
        lvSystem.setAdapter(dynamicMessageAdapter);
        dynamicMessage();
    }

    private void dynamicMessage() {
        Http.http.createApi(AuthApi.class).dynamicMessage()
                .compose(context.<JsonResult<List<HomeMoudle.DataBean>>>bindToLifecycle())
                .compose(context.<JsonResult<List<HomeMoudle.DataBean>>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<List<HomeMoudle.DataBean>>() {
                    @Override
                    public void success(List<HomeMoudle.DataBean> response, int code) {
                        if (response != null && response.size() > 0) {
                            llDefault.setVisibility(View.GONE);
                            dynamicMessageAdapter.setData(response);
                        } else {
                            llDefault.setVisibility(View.VISIBLE);
                            tvDefault.setText("暂无动态评论消息");
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
