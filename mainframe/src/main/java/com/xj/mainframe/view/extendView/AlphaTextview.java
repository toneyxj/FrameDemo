package com.xj.mainframe.view.extendView;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.xj.mainframe.view.BaseView.XJTextView;
import com.xj.mainframe.view.listener.ViewAlphaInterface;
import com.xj.mainframe.view.utils.ViewGroupAlphaUtil;

/**
 * Created by Administrator on 2016/10/18.
 */
public class AlphaTextview extends XJTextView {
    private ViewAlphaInterface viewAlpha;
    public AlphaTextview(Context context) {
        this(context,null);
    }

    public AlphaTextview(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AlphaTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewAlpha=new ViewGroupAlphaUtil(this);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        viewAlpha.onLayout(changed);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        viewAlpha.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean is=super.onTouchEvent(event);
        viewAlpha.onTouchEvent(event,is);
        return is;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        viewAlpha.onDestory();
    }

}
