package com.yijian.them.utils.http;


public class JsonResult<T> {

    public T data;
    public int code;
    public String msg;

    public boolean isOk() {
        if(code == 0){
            return true;
        }
        if(code == 503){
//            BaseApplication.showToast(CheckUtil.isNull(Hawk.get(PreferenceKey.SESSION, "")) ? "您还未登录，请登录。" : "登录信息已过期，请重新登录。");
//            BaseApplication.sendBrocast();
            return false;
        }
        return false;
    }

    public String toString() {
        return JsonUtil.toJson(this);
    }

}
