package com.yijian.them.ui.mine.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.ui.mine.adapter.FollwerAdapter;
import com.yijian.them.ui.mine.moudel.Follwermoudel;
import com.yqjr.utils.service.OkHttp;
import com.yqjr.utils.service.StringJsonCallBack;
import com.yqjr.utils.spUtils.SPUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

public class GuanzhuFragment extends BasicFragment {
    @BindView(R.id.lvGuanzhuList)
    ListView lvGuanzhuList;
    @BindView(R.id.ivDefault)
    ImageView ivDefault;
    @BindView(R.id.tvDefault)
    TextView tvDefault;
    @BindView(R.id.llDefault)
    LinearLayout llDefault;
    private String userId;
    private FollwerAdapter adapter;

    public void setUserId(int userId) {
        if (userId == 0) {
            this.userId = "";
        } else {
            this.userId = userId + "";
        }

    }

    @Override
    protected View getResourceView() {
        View rootView = View.inflate(getActivity(), R.layout.fragment_guanzhu_list, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onClickEvent() {

    }

    @Override
    protected void initView(Bundle bundle) {
        adapter = new FollwerAdapter(1);
        lvGuanzhuList.setAdapter(adapter);
        follow();
    }

    private void follow() {
        OkHttp.get().addHeader("token", SPUtils.getToken()).url(AuthApi.FOLLOW + userId)
                .build().execute(new StringJsonCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                super.onError(call, e, i);
            }

            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                Gson gson = new Gson();
                Follwermoudel follwermoudel = gson.fromJson(s, Follwermoudel.class);
                int code = follwermoudel.getCode();
                if (code == 200) {
                    List<Follwermoudel.DataBean> data = follwermoudel.getData();
                    if (data != null && data.size() > 0) {
                        llDefault.setVisibility(View.GONE);
                        adapter.setData(data);
                    } else {
                        ivDefault.setImageResource(R.mipmap.default_attention);
                        tvDefault.setText("Ta没有关注任何人");
                        llDefault.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

}
