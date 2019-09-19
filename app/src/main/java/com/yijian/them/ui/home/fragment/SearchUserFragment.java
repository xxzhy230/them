package com.yijian.them.ui.home.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yijian.them.R;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.ui.home.HomeMoudle;
import com.yijian.them.ui.home.adapter.TagAdapter;

import java.util.List;

import butterknife.BindView;

public class SearchUserFragment extends BasicFragment {
    @BindView(R.id.lvSearchTag)
    ListView lvSearchTag;
    @BindView(R.id.ivDefault)
    ImageView ivDefault;
    @BindView(R.id.tvDefault)
    TextView tvDefault;
    @BindView(R.id.llDefault)
    LinearLayout llDefault;
    private TagAdapter adapter;
    @Override
    protected void onClickEvent() {

    }

    @Override
    protected View getResourceView() {
        View view = View.inflate(getActivity(), R.layout.fragment_search_user,null);
        return view;
    }

    @Override
    protected void initView(Bundle bundle) {

    }
    public void setData(List<HomeMoudle.DataBean> response) {
//        if (response != null && response.size() > 0) {
//            llDefault.setVisibility(View.GONE);
//            adapter.clear();
//            adapter.setDataBeans(response);
//        } else {
//            llDefault.setVisibility(View.VISIBLE);
//            tvDefault.setText("暂无用户");
//            ivDefault.setImageResource(R.mipmap.no_data_bg);
//        }
    }
}
