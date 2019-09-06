package com.yijian.them.basic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.JsonResult;
import com.yijian.them.utils.http.JsonUtil;
import com.yijian.them.utils.http.ZipCallBack;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public abstract class BasicFragment extends Fragment {

    public com.yijian.them.basic.BasicActivity context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return getResourceView();
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initView(savedInstanceState);
        this.onClickEvent();
    }
    protected abstract void onClickEvent();

    protected abstract View getResourceView();

    protected abstract void initView(Bundle bundle);


    /**
     * RxJava线程调度
     * subscribeOn指定观察者代码运行的线程
     * observerOn()指定订阅者运行的线程
     *
     * @param <T>
     * @return
     */
    protected <T> Observable.Transformer<T, T> applySchedulers() {
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
    protected <T> Subscriber<JsonResult<T>> newSubscriber(final CallBack<T> callBack) {
        return new Subscriber<JsonResult<T>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                String errorMessage = "请求失败";
                int status = -1;
                if (e instanceof HttpException) {
                    HttpException httpException = (HttpException) e;

                    ResponseBody body = httpException.response().errorBody();
                    try {
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


    public Subscriber<List<JsonResult>> newZipSubscriber(final ZipCallBack callBack) {
        return new Subscriber<List<JsonResult>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                String errorMessage = "请求失败";
                int status = -1;
                if (e instanceof HttpException) {
                    HttpException httpException = (HttpException) e;

                    ResponseBody body = httpException.response().errorBody();
                    try {
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
            public void onNext(List<JsonResult> response) {
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
