package com.xj.framedemo.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.xj.framedemo.MainActivity;
import com.xj.framedemo.R;
import com.xj.framedemo.TestRequest;
import com.xj.framedemo.modelList.ModelUtils;
import com.xj.mainframe.adapter.BaseRecyclerAdapter;
import com.xj.mainframe.adapter.SmartViewHolder;
import com.xj.mainframe.base.BaseUtils;
import com.xj.mainframe.base.SmartLayoutUtils;
import com.xj.mainframe.configer.APPLog;
import com.xj.mainframe.configer.ToastUtils;
import com.xj.mainframe.eventBus.EventManger;
import com.xj.mainframe.listener.HandlerMessageInterface;
import com.xj.refuresh.SmartRefreshLayout;
import com.xj.refuresh.api.RefreshLayout;
import com.xj.refuresh.listener.OnRefreshLoadMoreListener;

import static com.xj.framedemo.modelList.ModelUtils.loadModels;

/**
 * Created by xj on 2018/10/29.
 */

public class TestFragment extends Fragment implements HandlerMessageInterface{
    private String Title;
    //可以根据这个修改item里面的内容
    private RecyclerView recyclerView;
    private BaseUtils.XJHander hander;

    public void setTitle(String title) {
        Title = title;
    }

    private SmartRefreshLayout refreshLayout;
    private SmartLayoutUtils layoutUtils;
    private BaseRecyclerAdapter<ModelUtils.Model> mAdapter;

    private TestRequest testRequest;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //拿到fragment的view，之后实例化recyclerView，然后创建适配器，再挂上recyclerView
        View view = inflater.inflate(R.layout.fragment, container, false);
        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.refreshLayout);
        hander=new BaseUtils.XJHander(this);
        layoutUtils=new SmartLayoutUtils(refreshLayout, new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                hander.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        APPLog.e("刷新二");
                        ToastUtils.getInstance().showToastShort(mAdapter.getCount());
                        if (mAdapter.getCount() > 12) {
                            Toast.makeText(getContext(), "数据全部加载完毕", Toast.LENGTH_SHORT).show();
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
                APPLog.e("刷新一");
                hander.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefresh();
                        refreshLayout.setNoMoreData(false);//恢复上拉状态
                    }
                }, 2000);

            }
        });
        recyclerView=layoutUtils.addRecycleView();
        layoutUtils.initListstyle();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter=new BaseRecyclerAdapter<ModelUtils.Model>(loadModels(), R.layout.listitem_practice_repast) {
            @Override
            protected void onBindViewHolder(SmartViewHolder holder, ModelUtils.Model model, int position) {
                holder.text(R.id.name, model.name);
                holder.text(R.id.nickname, model.nickname);
                holder.image(R.id.image, model.imageId);
                holder.image(R.id.avatar, model.avatarId);
            }
        });

        testRequest=new TestRequest(getContext(),layoutUtils.getLoading());

        hander.postDelayed(new Runnable() {
            @Override
            public void run() {
                testRequest.testRequest();
            }
        },3000);
        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                hander.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        EventManger.getInstance().notifiyCode(MainActivity.mainE,position);
                    }
                },3000);

            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hander.removeCallbacksAndMessages(null);
        APPLog.e("fragment-onDestroy",Title+"：onDestroy");
    }

    @Override
    public void handleMessage(Message msg) {

    }
}
