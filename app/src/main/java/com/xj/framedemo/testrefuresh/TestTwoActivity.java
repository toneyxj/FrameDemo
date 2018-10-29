package com.xj.framedemo.testrefuresh;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.xj.framedemo.R;
import com.xj.mainframe.adapter.BaseRecyclerAdapter;
import com.xj.mainframe.adapter.SmartViewHolder;
import com.xj.mainframe.base.SmartLayoutUtils;
import com.xj.mainframe.base.activity.BaseActivity;
import com.xj.mainframe.configer.APPLog;
import com.xj.mainframe.configer.ToastUtils;
import com.xj.mainframe.utils.StatusBarUtil;
import com.xj.mainframe.view.otherView.TitleView;
import com.xj.refuresh.SmartRefreshLayout;
import com.xj.refuresh.api.RefreshLayout;
import com.xj.refuresh.listener.OnRefreshLoadMoreListener;

import java.util.Arrays;
import java.util.Collection;

public class TestTwoActivity extends BaseActivity {
    private TitleView title_view;
    private SmartRefreshLayout refreshLayout;
    private SmartLayoutUtils layoutUtils;
    private BaseRecyclerAdapter<TestTwoActivity.Model> mAdapter;

    @Override
    public void initActivity(Bundle savedInstanceState) {
        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this);
        initView();
        APPLog.e("mMainThreadid 22",android.os.Process.myTid());
        layoutUtils=new SmartLayoutUtils(refreshLayout, new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.getInstance().showToastShort(mAdapter.getCount());
                        if (mAdapter.getCount() > 12) {
                            Toast.makeText(getBaseContext(), "数据全部加载完毕", Toast.LENGTH_SHORT).show();
                            refreshLayout.finishLoadMoreWithNoMoreData();//设置之后，将不会再触发加载事件
                        } else {
                            mAdapter.loadMore(loadModels());
                            refreshLayout.finishLoadMore();
                        }
                    }
                }, 1000);
            }

            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefresh();
                        refreshLayout.setNoMoreData(false);//恢复上拉状态
                    }
                }, 2000);
            }
        });

        layoutUtils.addRecycleView();
        layoutUtils.initListstyle();

        final RecyclerView recyclerView= (RecyclerView) layoutUtils.getContentView();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                layoutUtils.getLoading().hideLayout();
                recyclerView.setAdapter(mAdapter = new BaseRecyclerAdapter<TestTwoActivity.Model>(loadModels(), R.layout.listitem_practice_repast) {
                    @Override
                    protected void onBindViewHolder(SmartViewHolder holder, TestTwoActivity.Model model, int position) {
                        holder.text(R.id.name, model.name);
                        holder.text(R.id.nickname, model.nickname);
                        holder.image(R.id.image, model.imageId);
                        holder.image(R.id.avatar, model.avatarId);
                    }
                });
                mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ToastUtils.getInstance().showToastShort("index="+position);
                    }
                });
            }
        },3000);

        layoutUtils.getLoading().showLoading();
    }

    @Override
    public void initView() {
        title_view=(TitleView)findViewById(R.id.title_view);
        refreshLayout=(SmartRefreshLayout)findViewById(R.id.refreshLayout);

        new TitleView.TitleModel().setTitle("测试整合布局").setTxtRight("右边点击").setTitleClickListener(this).Builder(title_view);
        StatusBarUtil.setPaddingSmart(this, title_view);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_test_two;
    }

    @Override
    public void onclickView(View view) {

    }

    @Override
    public void handleMessage(Message msg) {

    }

    /**
     * 模拟数据
     */
    private Collection<TestTwoActivity.Model> loadModels() {
        return Arrays.asList(
                new TestTwoActivity.Model() {{
                    this.name = "但家香酥鸭";
                    this.nickname = "爱过那张脸";
                    this.imageId = R.mipmap.image_practice_repast_1;
                    this.avatarId = R.mipmap.image_avatar_1;
                }}, new TestTwoActivity.Model() {{
                    this.name = "香菇蒸鸟蛋";
                    this.nickname = "淑女算个鸟";
                    this.imageId = R.mipmap.image_practice_repast_2;
                    this.avatarId = R.mipmap.image_avatar_2;
                }}, new TestTwoActivity.Model() {{
                    this.name = "花溪牛肉粉";
                    this.nickname = "性感妩媚";
                    this.imageId = R.mipmap.image_practice_repast_3;
                    this.avatarId = R.mipmap.image_avatar_3;
                }}, new TestTwoActivity.Model() {{
                    this.name = "破酥包";
                    this.nickname = "一丝丝纯真";
                    this.imageId = R.mipmap.image_practice_repast_4;
                    this.avatarId = R.mipmap.image_avatar_4;
                }}, new TestTwoActivity.Model() {{
                    this.name = "盐菜饭";
                    this.nickname = "等着你回来";
                    this.imageId = R.mipmap.image_practice_repast_5;
                    this.avatarId = R.mipmap.image_avatar_5;
                }}, new TestTwoActivity.Model() {{
                    this.name = "米豆腐";
                    this.nickname = "宝宝树人";
                    this.imageId = R.mipmap.image_practice_repast_6;
                    this.avatarId = R.mipmap.image_avatar_6;
                }});
    }
    private class Model {
        int imageId;
        int avatarId;
        String name;
        String nickname;
    }
}
