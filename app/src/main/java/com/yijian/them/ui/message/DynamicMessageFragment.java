package com.yijian.them.ui.message;

import android.os.Bundle;
import android.view.View;

import com.yijian.them.R;
import com.yijian.them.basic.BasicFragment;

public class DynamicMessageFragment extends BasicFragment {
    @Override
    protected void onClickEvent() {

    }

    @Override
    protected View getResourceView() {
        View view = View.inflate(getActivity(), R.layout.fragment_dynamic_message,null);
        return view;
    }

    @Override
    protected void initView(Bundle bundle) {

    }
}
