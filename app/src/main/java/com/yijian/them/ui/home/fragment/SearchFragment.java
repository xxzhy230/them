package com.yijian.them.ui.home.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.dialog.AlertUtils;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yqjr.utils.utils.ToastUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SearchFragment extends BasicFragment {

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.ivClear)
    ImageView ivClear;
    @BindView(R.id.etSearch)
    EditText etSearch;

    @BindView(R.id.tflTag)
    TagFlowLayout tflTag;
    private TagAdapter adapter;

    @Override
    protected void onClickEvent() {
        getHot();
    }

    @Override
    protected View getResourceView() {
        View view = View.inflate(getActivity(), R.layout.fragment_search, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView(Bundle bundle) {
        context = (BasicActivity) getActivity();
        tflTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                String item = (String) adapter.getItem(position);
                etSearch.setText(item);
                etSearch.setSelection(item.length());
                ivClear.setVisibility(View.VISIBLE);
                JumpUtils.jumpSearchActivity(getActivity(), item);
                return false;
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    ivClear.setVisibility(View.VISIBLE);
                } else {
                    ivClear.setVisibility(View.INVISIBLE);
                }
            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String searchKey = etSearch.getText().toString().trim();
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                    JumpUtils.jumpSearchActivity(getActivity(), searchKey);
                }
                return true;
            }
        });
    }

    private void getHot() {
        Http.http.createApi(AuthApi.class).hot()
                .compose(context.<JsonResult<List<String>>>bindToLifecycle())
                .compose(context.<JsonResult<List<String>>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<List<String>>() {
                    @Override
                    public void success(final List<String> tags, int code) {
                        adapter = new TagAdapter(tags) {
                            @Override
                            public View getView(FlowLayout parent, int position, Object o) {
                                View view = View.inflate(getActivity(), R.layout.item_search_tag, null);
                                TextView tvTagname = view.findViewById(R.id.tvTagname);
                                tvTagname.setText(tags.get(position));
                                return view;
                            }
                        };
                        tflTag.setAdapter(adapter);
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }


    @OnClick({R.id.ivBack, R.id.ivClear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                getActivity().finish();
                break;
            case R.id.ivClear:
                ivClear.setVisibility(View.INVISIBLE);
                etSearch.setText("");
                break;
        }
    }
}
