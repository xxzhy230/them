package com.yijian.them.basic;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.yijian.them.utils.dialog.AlertUtils;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.JsonResult;
import com.yijian.them.utils.http.JsonUtil;
import com.yijian.them.utils.http.ZipCallBack;
import com.yqjr.utils.base.AppManager;
import com.yqjr.utils.utils.StatusBarUtil;

import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public abstract class BasicActivity extends RxAppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBar(this, true, false);
        setContentView(initView());
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initData();
    }

    public abstract int initView();

    public abstract void initData();

    /**
     * RxJava线程调度
     * subscribeOn指定观察者代码运行的线程
     * observerOn()指定订阅者运行的线程
     *
     * @param <T>
     * @return
     */
    public <T> Observable.Transformer<T, T> applySchedulers() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * RxJava对象转换
     * 转换、提取JsonResult包裹的对象
     *
     * @param <T>
     * @return
     */
    protected <T> Func1<JsonResult<T>, T> convert() {
        return new Func1<JsonResult<T>, T>() {
            @Override
            public T call(JsonResult<T> jsonResult) {
                if (jsonResult.isOk()) {
                    return jsonResult.data;
                }
                return null;
            }
        };
    }

    /**
     * RxJava空对象过滤
     * 返回false，即空对象不传递到订阅者处理
     *
     * @param <T>
     * @return
     */
    protected <T> Func1<T, Boolean> emptyObjectFilter() {
        return new Func1<T, Boolean>() {
            @Override
            public Boolean call(T response) {
                return response != null;
            }
        };
    }

    /**
     * RxJava简化版本网络异步回调
     * 过滤了错误码，直接返回实体对象
     *
     * @param callBack
     * @param <T>
     * @return
     */
    public <T> Subscriber<JsonResult<T>> newSubscriber(final CallBack<T> callBack) {
        return new Subscriber<JsonResult<T>>() {
            @Override
            public void onCompleted() {
                AlertUtils.dismissProgress();
            }

            @Override
            public void onError(Throwable e) {
                AlertUtils.dismissProgress();
                String errorMessage = "请求失败";
                int status = -1;
                if (e instanceof HttpException) {
                    HttpException httpException = (HttpException) e;
                    ResponseBody body = httpException.response().errorBody();

                    try {
                        Log.d("返回数据 : ", body.string());
                        JsonResult result = JsonUtil.fromJson(body.string(), JsonResult.class);
                        if (result != null && result.msg != null) {
                            errorMessage = result.msg;
                            status = result.code;
                        }

                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                } else {
                    e.printStackTrace();
                }
                if (callBack == null) {
                    return;
                }
                callBack.fail(errorMessage, status);
            }

            @Override
            public void onNext(JsonResult<T> response) {
                AlertUtils.dismissProgress();
                if (isUnsubscribed()) {
                    return;
                }
                if (callBack == null) {
                    return;
                }
                callBack.filter(response);
            }
        };
    }



}
