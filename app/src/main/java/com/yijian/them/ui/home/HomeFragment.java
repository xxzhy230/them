package com.yijian.them.ui.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yijian.them.R;
import com.yijian.them.ui.home.adapter.HomeAdapter;
import com.yijian.them.ui.home.fragment.GuanzhuFragment;
import com.yijian.them.ui.home.fragment.NearbyFragment;
import com.yijian.them.ui.home.fragment.NewFragment;
import com.yijian.them.ui.home.fragment.TuijianFragment;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.view.NoScrollViewPager;
import com.yqjr.utils.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment {
    @BindView(R.id.tvTuijian)
    TextView tvTuijian;
    @BindView(R.id.tvNeary)
    TextView tvNeary;
    @BindView(R.id.tvGuanzhu)
    TextView tvGuanzhu;
    @BindView(R.id.tvNew)
    TextView tvNew;
    @BindView(R.id.ivSearch)
    ImageView ivSearch;
    @BindView(R.id.ivAdd)
    ImageView ivAdd;
    @BindView(R.id.nsvpHome)
    NoScrollViewPager nsvpHome;
    private List<Fragment> fragments = new ArrayList<>();
    private TuijianFragment tuijianFragment = new TuijianFragment();
    private NearbyFragment nearbyFragment = new NearbyFragment();
    private GuanzhuFragment guanzhuFragment = new GuanzhuFragment();
    private NewFragment newFragment = new NewFragment();

    @Override
    protected View getResourceView() {
        View rootView = View.inflate(getActivity(), R.layout.fragment_home, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onClickEvent() {

    }

    @Override
    protected void initView(Bundle bundle) {
        initFragment();
        nsvpHome.setLRScroll(true);
        HomeAdapter homeAdapter = new HomeAdapter(getChildFragmentManager(), fragments);
        nsvpHome.setCurrentItem(0);
        nsvpHome.setOffscreenPageLimit(4);
        nsvpHome.setAdapter(homeAdapter);
    }

    private void initFragment() {
        fragments.clear();
        fragments.add(tuijianFragment);
        fragments.add(nearbyFragment);
        fragments.add(guanzhuFragment);
        fragments.add(newFragment);
    }


    @OnClick({R.id.tvTuijian, R.id.tvNeary, R.id.tvGuanzhu, R.id.ivSearch, R.id.ivAdd, R.id.tvNew})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvTuijian:
                nsvpHome.setCurrentItem(0);
                showTextState(0);
                break;
            case R.id.tvNeary:
                nsvpHome.setCurrentItem(1);
                showTextState(1);
                break;
            case R.id.tvGuanzhu:
                nsvpHome.setCurrentItem(2);
                showTextState(2);
                break;
            case R.id.tvNew:
                nsvpHome.setCurrentItem(3);
                showTextState(3);
                break;
            case R.id.ivSearch:
                JumpUtils.jumpDynamicActivity(getActivity(), 9, "", "");
                break;
            case R.id.ivAdd:
                JumpUtils.jumpDynamicActivity(getActivity(), 2, "", "");
                break;
        }
    }

    private void showTextState(int type) {
        switch (type) {
            case 0:
                tvTuijian.setTextColor(getResources().getColor(R.color.black));
                tvTuijian.setTextSize(18);
                tvGuanzhu.setTextColor(getResources().getColor(R.color.color_FF666666));
                tvGuanzhu.setTextSize(14);
                tvNeary.setTextColor(getResources().getColor(R.color.color_FF666666));
                tvNeary.setTextSize(14);
                tvNew.setTextColor(getResources().getColor(R.color.color_FF666666));
                tvNew.setTextSize(14);
                break;
            case 1:
                tvTuijian.setTextColor(getResources().getColor(R.color.color_FF666666));
                tvTuijian.setTextSize(14);
                tvGuanzhu.setTextColor(getResources().getColor(R.color.color_FF666666));
                tvGuanzhu.setTextSize(14);
                tvNeary.setTextColor(getResources().getColor(R.color.black));
                tvNeary.setTextSize(18);
                tvNew.setTextColor(getResources().getColor(R.color.color_FF666666));
                tvNew.setTextSize(14);
                break;
            case 2:
                tvTuijian.setTextColor(getResources().getColor(R.color.color_FF666666));
                tvTuijian.setTextSize(14);
                tvNeary.setTextColor(getResources().getColor(R.color.color_FF666666));
                tvNeary.setTextSize(14);
                tvGuanzhu.setTextColor(getResources().getColor(R.color.black));
                tvGuanzhu.setTextSize(18);
                tvNew.setTextColor(getResources().getColor(R.color.color_FF666666));
                tvNew.setTextSize(14);
                break;

            case 3:
                tvTuijian.setTextColor(getResources().getColor(R.color.color_FF666666));
                tvTuijian.setTextSize(14);
                tvNeary.setTextColor(getResources().getColor(R.color.color_FF666666));
                tvNeary.setTextSize(14);
                tvGuanzhu.setTextColor(getResources().getColor(R.color.color_FF666666));
                tvGuanzhu.setTextSize(14);
                tvNew.setTextColor(getResources().getColor(R.color.black));
                tvNew.setTextSize(18);
                break;
        }

    }
}
