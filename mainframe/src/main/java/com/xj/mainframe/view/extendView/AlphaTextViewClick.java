package com.xj.mainframe.view.extendView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自带点击文字颜色变化的textview
 * Created by Administrator on 2016/12/29.
 */
public class AlphaTextViewClick extends AlphaTextview implements View.OnClickListener {
    public AlphaTextViewClick(Context context) {
        super(context);
        init();
    }

    public AlphaTextViewClick(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AlphaTextViewClick(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
