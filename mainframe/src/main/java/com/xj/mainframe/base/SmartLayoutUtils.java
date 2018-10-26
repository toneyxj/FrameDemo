package com.xj.mainframe.base;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.xj.mainframe.R;
import com.xj.mainframe.view.otherView.LoadingLayout;
import com.xj.refuresh.SmartRefreshLayout;
import com.xj.refuresh.api.RefreshFooter;
import com.xj.refuresh.api.RefreshHeader;
import com.xj.refuresh.footer.ClassicsFooter;
import com.xj.refuresh.header.ClassicsHeader;
import com.xj.refuresh.listener.OnRefreshLoadMoreListener;

/**
 * 刷新布局操作工具类
 * Created by xj on 2018/10/26.
 */
public class SmartLayoutUtils {
    @android.support.annotation.IdRes
    private int contentId = 1001;
    private SmartRefreshLayout refreshLayout;
    private OnRefreshLoadMoreListener loadMoreListener;

    private RefreshHeader header;
    private LoadingLayout loading;
    private RefreshFooter footer;
    //显示内容布局view
    private View contentView;

    public SmartLayoutUtils(SmartRefreshLayout refreshLayout, OnRefreshLoadMoreListener loadMoreListener) {
        this.refreshLayout = refreshLayout;
        this.loadMoreListener = loadMoreListener;

        if (this.refreshLayout == null) {
            throw new NullPointerException("SmartRefreshLayout is not null");
        }
        initView();
    }

    /**
     * 获得提示布局view
     *
     * @return
     */
    public LoadingLayout getLoading() {
        return loading;
    }

    /**
     * 获得主要显示布局view
     *
     * @return
     */
    public View getContentView() {
        return contentView;
    }

    /**
     * 初始化布局参数
     */
    private void initView() {
        header = (RefreshHeader) refreshLayout.findViewById(R.id.heard);
        loading = (LoadingLayout) refreshLayout.findViewById(R.id.loading);
        footer = (RefreshFooter) refreshLayout.findViewById(R.id.footer);

        refreshLayout.setOnRefreshLoadMoreListener(loadMoreListener);
    }

    /**
     * 添加recycleView
     */
    public ListView addListView() {
        contentView = new ListView(loading.getContext());
        initaddView();
        return (ListView) contentView;
    }

    /**
     * 添加recycleView
     */
    public GridView addGrideView() {
        //创建布局
        contentView = new GridView(loading.getContext());
        initaddView();
        return (GridView) contentView;
    }

    /**
     * 添加recycleView
     */
    public RecyclerView addRecycleView() {
        //创建布局
        contentView = new RecyclerView(loading.getContext());
        initaddView();
        return (RecyclerView) contentView;
    }

    private void initaddView() {
        loading.removeAllViews();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        contentView.setLayoutParams(params);
        //是否进行view叠加时的处理，适用于全屏透明效果
        if (contentView instanceof ViewGroup)
            ((ViewGroup) contentView).setClipToPadding(false);
        //设置滑动到顶部是否显示弧形覆盖
        contentView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        contentView.setId(contentId);
        loading.addView(contentView);
    }

    /**
     * 设置底部和顶部刷新布局view颜色
     */
    public void setHeaderAndFootBackColor(@ColorInt int Color){
        if (header instanceof ClassicsHeader){
            ((ClassicsHeader) header).setAccentColor(Color);
        }
        if (footer instanceof ClassicsFooter){
            ((ClassicsFooter) header).setAccentColor(Color);
        }
    }


    /**
     * 注入全新的
     *
     * @param view
     */
    public void setNewView(@NonNull View view) {
        loading.removeAllViews();
        loading.addView(view);
    }

    /**
     * 初始化使用布局类型ListView
     */
    public void initListstyle() {
        refreshLayout.setEnableFooterFollowWhenLoadFinished(true);
        refreshLayout.setDragRate(0.7f);
        refreshLayout.setHeaderMaxDragRate(1.3f);
        refreshLayout.setEnableAutoLoadMore(true);
        refreshLayout.setHeaderTriggerRate(0.5f);
    }

    /**
     * 初始化使用布局类型没有刷新功能
     */
    public void initnostyle() {
        refreshLayout.setEnablePureScrollMode(true);
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setDragRate(0.7f);
    }
}
