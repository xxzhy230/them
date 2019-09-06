package com.yijian.them.utils.http;

import java.util.List;

/**
 * 多接口请求回调
 * Author : zhouyx
 * Date   : 2016/9/21
 */
public abstract class ZipCallBack {

    public void filter(List<JsonResult> response) {
        try {
            if (response == null) {
                fail("网络请求失败",-1);
            } else {
                for (JsonResult jsonResult : response) {
                    if (!jsonResult.isOk()) {
                        fail(jsonResult.msg,jsonResult.code);
                        return;
                    }
                }
                success(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void success(List<JsonResult> response);

    public abstract void fail(String errorMessage, int status);

}
