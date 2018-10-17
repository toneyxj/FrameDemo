package com.xj.mainframe.view.extendView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.xj.mainframe.view.BaseView.XJImageView;

/**
 * Created by Administrator on 2016/10/20.
 */
public class AlphaImageView extends XJImageView {

    public AlphaImageView(Context context) {
        super(context);
    }

    public AlphaImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlphaImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    private boolean isFirst=true;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isFirst) {
                if (getDrawable()!=null){
                    getDrawable().setAlpha(150);
                }else if (getBackground()!=null){
                    getBackground().setAlpha(150);
                }
                    isFirst=false;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (getDrawable()!=null){
                    getDrawable().setAlpha(255);
                }else if (getBackground()!=null){
                    getBackground().setAlpha(255);
                }
                isFirst=true;
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
