package com.yijian.them.utils.http;

public abstract class CallBack<T> {
    public void filter(com.yijian.them.utils.http.JsonResult<T> response) {
        try {
            if (response == null) {
                fail("网络请求失败",-1);
            } else {
                if (response.code==200 || response.code == 10001 || response.code == 10000) {
                    success(response.data,response.code);
                } else {
                    fail(response.msg,response.code);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public abstract void success(T response,int code);

    public abstract void fail(String errorMessage, int status);

}
