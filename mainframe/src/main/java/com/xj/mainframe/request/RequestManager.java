package com.xj.mainframe.request;

import android.os.Handler;
import android.support.v4.util.LruCache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xj.mainframe.BaseApplication;
import com.xj.mainframe.configer.APPLog;
import com.xj.mainframe.model.ResponseModel;
import com.xj.mainframe.netState.NetWorkUtil;
import com.xj.mainframe.request.Listener.Observerable;
import com.xj.mainframe.request.Listener.RequestObserver;
import com.xj.mainframe.utils.StringUtils;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 数据请求管理类
 * Created by xj on 2018/10/29.
 */
public class RequestManager implements Observerable {

    private static RequestManager instatnce = null;

    public static RequestManager getInstance() {
        if (instatnce == null) {
            synchronized (RequestManager.class) {
                if (instatnce == null) {
                    instatnce = new RequestManager();
                }
            }
        }
        return instatnce;
    }

    public RequestManager() {
        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 12;
        // 设置图片缓存大小为程序最大可用内存的1/12
        mLruCache = new LruCache<String, String>(cacheSize) {
            @Override
            protected int sizeOf(String key, String bitmap) {
                return bitmap.getBytes().length;
            }
        };
        okHttpClient = new OkHttpClient();
    }

    /**
     * 数据请求缓存类
     */
    private LruCache<String, String> mLruCache;
    private LinkedList<RequestObserver> observers = new LinkedList<>();
    private OkHttpClient okHttpClient;
    private Handler handler=new Handler();

    @Override
    public void registerObserver(RequestObserver observer) {
        if (!observers.contains(observer))
            observers.add(observer);
    }

    @Override
    public void removeObserver(RequestObserver observer) {
        observers.remove(observer);
    }
    private RequestObserver getRequestObserver(RequestModel model){
        if (model==null)return null;
        if (observers.contains(model.getObserver())){
            return model.getObserver();
        }
        return null;
    }

    /**
     * 数据请求
     *
     * @param model
     */
    public void requestData(final RequestModel model) {
        RequestObserver observer=getRequestObserver(model);
        if (observer==null){
            return;
        }
        if (model.isCanCache()){
            //保存缓存的
            String result=mLruCache.get(model.toString());
            if (result!=null){
                try {
                    ResponseModel rmodel= JSON.parseObject(result, ResponseModel.class);
                     //获取数据成功
                    observer.onSucess(model,rmodel.getData());
                    return;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        if (!NetWorkUtil.isNetworkConnected(BaseApplication.context)){
            ResponseModel model1=ResponseModel.initNoNet();
            observer.onFaile(model,model1.getStatus(),model1.getMessage());
            return;
        }

        observer.onstart(model);

        Request.Builder builder = new Request.Builder().url(model.getUrl());

        if (model.getGet()) {
            String getS=StringUtils.getGetUrl(model.getParameters(), model.getUrl());
            APPLog.e("getUrl",getS);
            builder.url(getS);
        } else {
            FormBody.Builder body = new FormBody.Builder();
            //添加post数据
            for (Map.Entry<String, String> entry : model.getParameters().entrySet()) {
                body.add(entry.getKey(), entry.getValue());
            }
            APPLog.e("postUrl",model.getUrl());
            builder.post(body.build());
            builder.url(model.getUrl());
        }
        //添加数据请求头
        if (model.getHeaders() != null && model.getHeaders().size() > 0) {
            //添加header
            for (Map.Entry<String, String> entry : model.getHeaders().entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = builder.build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if (e instanceof SocketTimeoutException) {
                    onFaile(model,-1,"网络状态差001");
                } else if (e instanceof ConnectException) {
                    onFaile(model,-1,"网络状态差002");
                }else {
                    onFaile(model,-1,"未知错误0003");
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String result = response.body().string();
                       ResponseModel rmodel= JSON.parseObject(result, ResponseModel.class);
                        if (rmodel.getStatus()==1){
                            //获取数据成功
                            onSucess(model,rmodel.getData());
                            if (model.isCanCache()){
                                mLruCache.put(model.toString(),result);
                            }
                        }else {
                            onFaile(model,rmodel.getStatus(),rmodel.getMessage());
                        }
                    } else {
                        onFaile(model,-1,"response is fail");
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                    onFaile(model,-1,"未知错误0004");
                }
            }
        });
    }
    private void onFaile(final RequestModel code, final int status , final String msg){
        final RequestObserver observer=getRequestObserver(code);
        if (observer==null)return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                observer.onFaile(code,status,msg);
            }
        });

    }
    private void onSucess(final RequestModel code, final JSONObject json){
        final RequestObserver observer=getRequestObserver(code);
        if (observer==null)return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                observer.onSucess(code,json);
            }
        });
    }
}
