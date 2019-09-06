package com.yijian.them.utils.http;


import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by zhangdeming on 16/4/25.
 */
public class RequestLogInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String method = request.method();
        if (method.equals("GET")){
            System.out.println(request.url().toString());
        }
        Response response = chain.proceed(request);
        RequestBody requestBody = request.body();
        printParams(requestBody);
        ResponseBody responseBody = response.body();
        String responseBodyString = responseBody.string();
        Log.d("返回数据 ", responseBodyString);
        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
        }

        return response.newBuilder().body(ResponseBody.create(responseBody.contentType(),
                responseBodyString.getBytes())).build();
    }

    private void printParams(RequestBody body) {
        if (body == null) {
            return;
        }
        Buffer buffer = new Buffer();
        try {
            body.writeTo(buffer);
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = body.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF_8);
            }
            String params = buffer.readString(charset);
            Log.d("请求参 ", params);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
