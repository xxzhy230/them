package com.yijian.them.utils.http;

import android.content.Context;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by longbh on 16/5/24.
 */
public class Http {

    public static String user_session = "";
    public static String baseUrl = "http://39.108.188.145:8077/";

    public static Http http;

    private Retrofit mRetrofit;

    public final static int CONNECT_TIMEOUT = 50;
    public final static int READ_TIMEOUT = 50;
    public final static int WRITE_TIMEOUT = 50;

    private Http(Context context) {
        File httpCacheDirectory = new File(context.getApplicationContext().getCacheDir(), context
                .getApplicationContext().getPackageName());
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new RequestLogInterceptor())
                .addInterceptor(new RequestHeaderInterceptor())
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                .cache(cache)
                .build();

        mRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static boolean isLogin() {
        return !"".equals(user_session);
    }

    public static void initHttp(Context context) {
        http = new Http(context);
    }

    public <T> T createApi(Class<T> tClass) {
        return mRetrofit.create(tClass);
    }

}
