package com.yijian.them.ui.home.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yijian.them.R;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.common.Config;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchDynamicFragment extends BasicFragment {
    @BindView(R.id.lvSearchTag)
    ListView lvSearchTag;

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

    }

    public void searchKey(String key){

    }
}
