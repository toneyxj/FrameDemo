package com.xj.mainframe.view.otherView;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xj.mainframe.R;
import com.xj.mainframe.adapter.ADViewpagerAdapter;
import com.xj.mainframe.listener.ClickItemListener;
import com.xj.mainframe.utils.DensityUtil;
import com.xj.mainframe.utils.TimerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 广告轮播器
 * Created by Administrator on 2016/4/26.
 */
public class ADViewpager extends RelativeLayout implements ViewPager.OnPageChangeListener ,TimerUtils.TimeListener {
    /**
     * 轮播容器
     */
    private ViewPager pager_pager;
    /**
     * 导航提示容器
     */
    private RelativeLayout linear_layout;
    /**
     * 显示轮播图路径
     */
    private List<String> filePaths;
    /**
     * 点击轮播图片监听
     */
    private ClickItemListener listener;
    /**
     * 上下文
     */
    private Context context;
    /**
     * 是否实现轮播
     */
    private boolean IsPlay = true;
    /**
     * 当前轮播时间
     */
    private int time = 7;
    /**
     * 点的高度
     */
    private int pointSize = 20;
    /**
     * 点之间的距离
     */
    private int pointDistance =20;
    /**
     * 原点显示的image
     */
    private ImageView pointImage;//原点的image图片
    /**
     * 显示最大尺寸
     */
    private int maxSize=0;
    private TimerUtils timerUtils;


    /**
     * 初始化设置图片适配器显示数据
     *
     * @param values 网络图片数据集
     * @param IsPlay    是否轮播
     * @param listener  监听点击图片事件
     */
    public void setinitlize( boolean IsPlay, ClickItemListener listener,String... values) {
        List<String> list=new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            list.add(values[i]);
        }
        setinitlize(list,IsPlay,listener);
    }
    public void setinitlize(List<String> filePaths, boolean IsPlay, ClickItemListener listener) {
        this.filePaths = filePaths;
        this.listener = listener;
        this.IsPlay = IsPlay;

        initIndicator();
        pager_pager.setAdapter(new ADViewpagerAdapter(filePaths, context,
                listener));
        if (filePaths.size() > 1)
            pager_pager.setCurrentItem(filePaths.size() * 100);
    }

    /**
     * 开启轮播计时
     */
    private void startAd(){
        if (timerUtils==null){
            timerUtils=new TimerUtils(TimerUtils.TimerE.DOWN,time,0,this);
        }
        timerUtils.setStartTime(time);
        timerUtils.startTimer();
    }

    @Override
    public void cuttentTime(int time) {

    }

    @Override
    public void TimeEnd() {
        pager_pager.setCurrentItem(pager_pager.getCurrentItem()+1, true);
        startAd();
    }

    public ADViewpager(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ADViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        pointSize= DensityUtil.getScreenW(context)/45;
        pointDistance=DensityUtil.getScreenW(context)/45;

        View view = LayoutInflater.from(context).inflate(R.layout.include_viewpager,
                this);
        pager_pager = (ViewPager) view.findViewById(R.id.pager_pager);
        linear_layout = (RelativeLayout) view.findViewById(R.id.linear_layout);
        pager_pager.setOnPageChangeListener(this);
    }

    private int InPosition;

    /**
     * 初始化引导图标 动态创建多个小圆点，然后组装到线性布局里
     */
    private void initIndicator() {
        ImageView imgView;
        InPosition = filePaths.size();
        maxSize=(pointSize + pointDistance) * filePaths.size() - pointDistance;

        LayoutParams params = (LayoutParams) linear_layout.getLayoutParams();
        params.width =maxSize ;
        linear_layout.setLayoutParams(params);
        for (int i = 0; i < InPosition; i++) {
            imgView = new ImageView(context);
            LayoutParams params_linear = new LayoutParams(
                    pointSize, pointSize);
            int maleft = (pointSize + pointDistance) * i;
            params_linear.setMargins(maleft, 0, 0, 0);
            imgView.setLayoutParams(params_linear);
            imgView.setBackgroundResource(R.drawable.ad_no_select);
            linear_layout.addView(imgView);
        }
        createPoint();
        if (IsPlay && InPosition > 1)
           startAd();

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //设置自动滚动时间为0
        if (timerUtils!=null)
        timerUtils.setStartTime(time);
        int pageSize = pointSize + pointDistance;
        if (positionOffset > 0){
        moveto((int) ((pageSize*(position%filePaths.size()))+pageSize*positionOffset));
        }
    }

    @Override
    public void onPageSelected(int position) {
    }


    @Override
    public void onPageScrollStateChanged(int state) {
        if (ViewPager.SCROLL_STATE_IDLE==state){
            int pageSize = pointSize + pointDistance;
            moveto((int) ((pageSize*(pager_pager.getCurrentItem()%filePaths.size()))));
        }
    }

    private void moveto(int x) {
        LayoutParams layoutParams = (LayoutParams) pointImage
                .getLayoutParams();
        layoutParams.leftMargin = x;
        pointImage.setLayoutParams(layoutParams);
    }

    /**
     * 创建点
     */
    private void createPoint() {
        if (pointImage != null) return;
        if (InPosition > 0) {
            pointImage = new ImageView(context);
            LayoutParams params_linear = new LayoutParams(
                    pointSize, pointSize);
            params_linear.setMargins(0, 0, 0, 0);
            pointImage.setLayoutParams(params_linear);
            pointImage.setBackgroundResource(R.drawable.ad_yes_select);
            linear_layout.addView(pointImage);
        }
    }

    /**
     * 销毁轮播图
     */
    public  void destoryAD(){
//        if (timerUtils!=null)timerUtils.stopTimer();
//        pager_pager.removeAllViews();
//        linear_layout.removeAllViews();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (timerUtils!=null)timerUtils.stopTimer();
    }
}
