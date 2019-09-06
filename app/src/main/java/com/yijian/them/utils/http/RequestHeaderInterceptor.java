package com.yijian.them.utils.http;


import android.text.TextUtils;

import com.yqjr.utils.spUtils.SPUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;


public class RequestHeaderInterceptor implements Interceptor {

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        okhttp3.Request.Builder builder = request.newBuilder();
        if (!TextUtils.isEmpty(SPUtils.getToken())) {
            builder.addHeader("token", SPUtils.getToken());
        } else {
            builder.addHeader("token", Http.user_session);
        }
        builder.addHeader("platform", "1");
        return chain.proceed(builder.build());
    }
}