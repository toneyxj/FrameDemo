package com.xj.mainframe.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xj.mainframe.R;
import com.xj.mainframe.listener.ClickItemListener;
import com.xj.mainframe.view.BaseView.XJImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/4/26.
 */
public class ADViewpagerAdapter extends PagerAdapter {
    private List<String> images;
    private LayoutInflater inflater;
    private ClickItemListener listener;

    /**
     * 轮播图是适配器
     *
     * @param images   图片路径集合
     * @param context  上下文
     * @param listener 监听点击事件
     */
    public ADViewpagerAdapter(List<String> images, Context context, ClickItemListener listener) {
        this.images = images;
        this.listener=listener;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }


    @Override
    public int getCount() {
        if (images.size()==1)
            return 1;
        return Integer.MAX_VALUE;
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        position = position % images.size();// 获得当前位置并计算
        View imageLayout = inflater.inflate(R.layout.listview_item_pic, view,
                false);
        final XJImageView imageView = (XJImageView) imageLayout
                .findViewById(R.id.image_pager);
        if (listener != null) {
            imageView.setTag(position);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {// 点击进行商品的展示
                        listener.clickItem(v.getTag());
                }
            });
        }
        imageView.loadImage(images.get(position));
        view.addView(imageLayout); // 将图片增加到ViewPager
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
