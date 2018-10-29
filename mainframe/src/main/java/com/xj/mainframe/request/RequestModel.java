package com.xj.mainframe.request;

import com.xj.mainframe.request.Listener.RequestObserver;
import com.xj.mainframe.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据请求实列类
 * Created by xj on 2018/10/29.
 */

public class RequestModel {

    private Map<String, String> parameters = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();
    /**
     * 当前请求唯一标识,每一个请求对应一个code
     */
    private String code;
    /**
     * 当前请求url
     */
    private String url;
    /**
     * 当前请求辅助code，如果同一个请求请用不同标识来区分
     */
    private Object extCode="";
    /**
     * 是否可以保存数据在缓存
     */
    private boolean canCache = false;
    /**
     * 数据的观察者
     */
    private RequestObserver observer;
    /**
     * 是否是get请求
     */
    private boolean isGet = false;
    /**
     * 加载完成是否显示提示
     */
    private boolean showFailHide=true;
    /**
     * 请求失败显示布局提示
     */
    private boolean showLoidingLayout=true;

    /**
     * 数据请求model
     *
     * @param url  请求路径
     * @param code 请求code值
     */
    public RequestModel(String url, String code,RequestObserver observer) {
        this.code = code;
        this.url = url;
        this.observer = observer;
    }

    public RequestModel addParameter(String key, String value) {
        parameters.put(key, value);
        return this;
    }

    public RequestModel setParameter(Map<String, String> parameters ) {
        this.parameters=parameters;
        return this;
    }

    public RequestModel setHeaders(Map<String, String> headers ) {
        this.headers=headers;
        return this;
    }

    public RequestModel setExtCode(Object extCode) {
        this.extCode = extCode;
        return this;
    }

    public RequestModel setCanCache(boolean canCache) {
        this.canCache = canCache;
        return this;
    }

    public RequestModel setObserver(RequestObserver observer) {
        this.observer = observer;
        return this;
    }
    public RequestModel setShowFailHide(boolean showFailHide) {
        this.showFailHide = showFailHide;
        return this;
    }
    public RequestModel setShowLoidingLayout(boolean showLoidingLayout) {
        this.showLoidingLayout = showLoidingLayout;
        return this;
    }

    public RequestModel isGet() {
        this.isGet = true;
        return this;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public boolean getGet(){
        return this.isGet;
    }

    public String getCode() {
        return code;
    }

    public String getUrl() {
        return url;
    }

    public Object getExtCode() {
        return extCode;
    }

    public boolean isCanCache() {
        return canCache;
    }

    public RequestObserver getObserver() {
        return observer;
    }

    public boolean isShowFailHide() {
        return showFailHide;
    }


    public boolean isShowLoidingLayout() {
        return showLoidingLayout;
    }

    public void Builder(){
        RequestManager.getInstance().requestData(this);
    }
    @Override
    public String toString() {
        return StringUtils.StringToMd5(url + code + extCode.toString());
    }
}
