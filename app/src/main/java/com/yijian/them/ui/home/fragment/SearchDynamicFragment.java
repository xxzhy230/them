package com.yijian.them.ui.home.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yijian.them.R;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.common.Config;
import com.yijian.them.ui.home.HomeMoudle;
import com.yijian.them.ui.home.adapter.TagAdapter;
import com.yijian.them.ui.home.adapter.TuijianAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchDynamicFragment extends BasicFragment {
    @BindView(R.id.lvSearchTag)
    ListView lvSearchTag;
    @BindView(R.id.ivDefault)
    ImageView ivDefault;
    @BindView(R.id.tvDefault)
    TextView tvDefault;
    @BindView(R.id.llDefault)
    LinearLayout llDefault;
    private TuijianAdapter adapter;

    @Override
    protected void onClickEvent() {

    }

    @Override
    protected View getResourceView() {
        View view = View.inflate(getActivity(), R.layout.fragment_search_dynamic, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView(Bundle bundle) {
        adapter = new TuijianAdapter();
        lvSearchTag.setAdapter(adapter);
    }

    public void setData(List<HomeMoudle.DataBean> response) {
        if (response != null && response.size() > 0) {
            llDefault.setVisibility(View.GONE);
            adapter.clear();
            adapter.setData(response);
        } else {
            llDefault.setVisibility(View.VISIBLE);
            tvDefault.setText("暂无动态");
            ivDefault.setImageResource(R.mipmap.no_data_bg);
        }
    }
}
