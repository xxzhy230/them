package com.yijian.them.ui.mine.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.ui.mine.adapter.FollwerAdapter;
import com.yijian.them.ui.mine.moudel.Follwermoudel;
import com.yijian.them.utils.dialog.AlertUtils;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yqjr.utils.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BlackListFragment extends BasicFragment {
    @BindView(R.id.ivDefault)
    ImageView ivDefault;
    @BindView(R.id.tvDefault)
    TextView tvDefault;
    @BindView(R.id.llDefault)
    LinearLayout llDefault;
    @BindView(R.id.lbBalckList)
    ListView lbBalckList;

    private FollwerAdapter follwerAdapter;

    @Override
    protected void onClickEvent() {
    }

    @Override
    protected View getResourceView() {
        View view = View.inflate(getActivity(), R.layout.fragment_black_list, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView(Bundle bundle) {
        context = (BasicActivity) getActivity();
        follwerAdapter = new FollwerAdapter(3);
        lbBalckList.setAdapter(follwerAdapter);
        follwerAdapter.setOnDelBlackListener(new FollwerAdapter.OnDelBlackListener() {
            @Override
            public void onDelBlack(Follwermoudel.DataBean dataBean) {
                delBlackUser(dataBean);
            }
        });
        blackList();
    }

    /**
     * 移除黑名单
     *
     * @param dataBean
     */
    private void delBlackUser(final Follwermoudel.DataBean dataBean) {
        Http.http.createApi(AuthApi.class).delBlackUser(dataBean.getUserId() + "")
                .compose(context.<JsonResult<String>>bindToLifecycle())
                .compose(context.<JsonResult<String>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String response, int code) {
                        Log.d("移除黑名单: ", "");
                        follwerAdapter.delItem(dataBean);
                        int count = follwerAdapter.getCount();
                        if (count == 0) {
                            llDefault.setVisibility(View.VISIBLE);
                            tvDefault.setText("暂无添加黑名单成员");
                        } else {
                            llDefault.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }

    /**
     * 黑名单列表
     */
    private void blackList() {
        Http.http.createApi(AuthApi.class).blackList()
                .compose(context.<JsonResult<List<Follwermoudel.DataBean>>>bindToLifecycle())
                .compose(context.<JsonResult<List<Follwermoudel.DataBean>>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<List<Follwermoudel.DataBean>>() {
                    @Override
                    public void success(List<Follwermoudel.DataBean> response, int code) {
                        Log.d("黑名单列表: ", "");
                        if (response != null && response.size() > 0) {
                            llDefault.setVisibility(View.GONE);
                            follwerAdapter.setData(response);
                        } else {
                            llDefault.setVisibility(View.VISIBLE);
                            tvDefault.setText("暂无添加黑名单成员");
                        }
                    }
                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }
}
