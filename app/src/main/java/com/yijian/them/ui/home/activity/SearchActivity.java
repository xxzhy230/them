package com.yijian.them.ui.home.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.common.Config;
import com.yijian.them.ui.home.HomeMoudle;
import com.yijian.them.ui.home.adapter.HomeAdapter;
import com.yijian.them.ui.home.fragment.SearchDynamicFragment;
import com.yijian.them.ui.home.fragment.SearchTagFragment;
import com.yijian.them.ui.home.fragment.SearchUserFragment;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.dialog.AlertUtils;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yijian.them.view.NoScrollViewPager;
import com.yqjr.utils.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BasicActivity {
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.ivClear)
    ImageView ivClear;
    @BindView(R.id.tvTag)
    TextView tvTag;
    @BindView(R.id.tvDynamic)
    TextView tvDynamic;
    @BindView(R.id.tvUser)
    TextView tvUser;
    @BindView(R.id.vpHome)
    ViewPager vpHome;
    private List<Fragment> fragments = new ArrayList<>();
    private SearchDynamicFragment searchDynamicFragment = new SearchDynamicFragment();
    private SearchTagFragment searchTagFragment = new SearchTagFragment();
    private SearchUserFragment searchUserFragment = new SearchUserFragment();
    private int page = 0;
    private int type = 0;
    private String searchKey;

    @Override
    public int initView() {
        return R.layout.activity_search;
    }

    @Override
    public void initData() {
        ButterKnife.bind(this);
        initFragment();
        HomeAdapter homeAdapter = new HomeAdapter(getSupportFragmentManager(), fragments);
        vpHome.setCurrentItem(0);
        vpHome.setAdapter(homeAdapter);
        searchKey = getIntent().getStringExtra(Config.SEARCHKEY);
        etSearch.setText(searchKey);
        etSearch.setSelection(searchKey.length());
        searchKey(searchKey, type);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    ivClear.setVisibility(View.INVISIBLE);
                } else {
                    ivClear.setVisibility(View.VISIBLE);
                }
            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchKey = etSearch.getText().toString().trim();
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                    page = 0;
                    searchKey(searchKey, type);
                }
                return true;
            }
        });
        vpHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                showTextState(i);
                type = i;
                page = 0;
                searchKey(searchKey, type);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    private void initFragment() {
        fragments.clear();
        fragments.add(searchTagFragment);
        fragments.add(searchDynamicFragment);
        fragments.add(searchUserFragment);
    }

    @OnClick({R.id.ivBack, R.id.ivClear, R.id.tvTag, R.id.tvDynamic, R.id.tvUser})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivClear:
                etSearch.setText("");
                break;
            case R.id.tvTag:
                vpHome.setCurrentItem(0);
                showTextState(0);
                type = 0;
                break;
            case R.id.tvDynamic:
                vpHome.setCurrentItem(1);
                showTextState(1);
                type = 1;
                break;
            case R.id.tvUser:
                vpHome.setCurrentItem(2);
                showTextState(2);
                type = 2;
                break;
        }
    }

    private void showTextState(int type) {
        switch (type) {
            case 0:
                tvTag.setTextColor(getResources().getColor(R.color.black));
                tvTag.setTextSize(18);
                tvDynamic.setTextColor(getResources().getColor(R.color.color_FF666666));
                tvDynamic.setTextSize(14);
                tvUser.setTextColor(getResources().getColor(R.color.color_FF666666));
                tvUser.setTextSize(14);
                break;
            case 1:
                tvTag.setTextColor(getResources().getColor(R.color.color_FF666666));
                tvTag.setTextSize(14);
                tvDynamic.setTextColor(getResources().getColor(R.color.black));
                tvDynamic.setTextSize(18);
                tvUser.setTextColor(getResources().getColor(R.color.color_FF666666));
                tvUser.setTextSize(14);

                break;
            case 2:
                tvTag.setTextColor(getResources().getColor(R.color.color_FF666666));
                tvTag.setTextSize(14);
                tvDynamic.setTextColor(getResources().getColor(R.color.color_FF666666));
                tvDynamic.setTextSize(14);
                tvUser.setTextColor(getResources().getColor(R.color.black));
                tvUser.setTextSize(18);
                break;

        }
    }


    private void searchKey(String key, final int type) {
        AlertUtils.showProgress(false, this);
        Map map = new HashMap();
        map.put("key", key);
        map.put("page", page);
        map.put("type", type);
        Http.http.createApi(AuthApi.class).search(map)
                .compose(this.<JsonResult<List<HomeMoudle.DataBean>>>bindToLifecycle())
                .compose(this.<JsonResult<List<HomeMoudle.DataBean>>>applySchedulers())
                .subscribe(this.newSubscriber(new CallBack<List<HomeMoudle.DataBean>>() {
                    @Override
                    public void success(List<HomeMoudle.DataBean> response, int code) {
                        AlertUtils.dismissProgress();
                        Log.d("搜索: ", response + "");
                        if (type == 0) {
                            searchTagFragment.setData(response);
                        } else if (type == 1) {
                            searchDynamicFragment.setData(response);
                        } else {
                            searchUserFragment.setData(response);
                        }
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(SearchActivity.this, errorMessage + "");
                    }
                }));
    }
}
