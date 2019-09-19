package com.yijian.them.ui.mine.fragment;

import android.os.Bundle;
import android.view.View;

import com.yijian.them.R;
import com.yijian.them.basic.BasicFragment;

import butterknife.ButterKnife;

public class VersionFragment extends BasicFragment {
    @Override
    protected View getResourceView() {
        View rootView = View.inflate(getActivity(), R.layout.fragment_about, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onClickEvent() {

    }

    @Override
    protected void initView(Bundle bundle) {

    }
}
