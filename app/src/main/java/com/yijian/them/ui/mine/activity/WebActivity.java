package com.yijian.them.ui.mine.activity;

import android.content.Intent;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.yijian.them.R;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.common.Config;
import com.yijian.them.utils.dialog.AlertUtils;


import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhanghongyu on 2018/4/11.
 */

public class WebActivity extends BasicActivity {

    @BindView(R.id.tvTitleBar)
    TextView tvTitleBar;
    @BindView(R.id.webview)
    WebView webview;
    private String url;


    @Override
    public int initView() {
        return R.layout.activity_web;
    }

    @Override
    public void initData() {
        url = getIntent().getStringExtra(Config.ABOUTURL);
        String title = getIntent().getStringExtra(Config.ABOUTTITLE);
        WebSettings settings = webview.getSettings();
        if (!TextUtils.isEmpty(title)) {
            tvTitleBar.setText(title);
        } else {
            tvTitleBar.setText("");
        }
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        if (!TextUtils.isEmpty(url)) {
            AlertUtils.showProgress(false, this);
            webview.loadUrl(url);
        }
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                AlertUtils.dismissProgress();
            }

        });
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // TODO Auto-generated method stub
                super.onReceivedError(view, errorCode, description, failingUrl);
                webview.loadUrl("file:///android_asset/errorpage/error.html");
            }
        });
        //启用支持Javascript

        settings.setSavePassword(false);
        settings.setJavaScriptEnabled(true);
        //WebView加载页面优先使用缓存加载
//        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
    }

    @OnClick(R.id.tvTitleBar)
    public void onViewClicked() {
        finish();
    }

}
