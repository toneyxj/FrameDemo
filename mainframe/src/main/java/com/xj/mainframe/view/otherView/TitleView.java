package com.xj.mainframe.view.otherView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.xj.mainframe.R;
import com.xj.mainframe.listener.XJOnClickListener;
import com.xj.mainframe.view.extendView.AlphaImageView;
import com.xj.mainframe.view.extendView.AlphaTextview;
import com.xj.mainframe.view.listener.TitleInterface;

/**
 * 标题栏数据集合
 * Created by xj on 2018/10/25.
 */
public class TitleView extends RelativeLayout {

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private AlphaImageView btn_left;
    private AlphaTextview header_title;
    private AlphaTextview btn_sright;
    private AlphaImageView btn_right;
    private View header_line;

    private TitleModel titleModel;

    private void initView(Context context) {
        View.inflate(context, R.layout.title_layout, this);
        //初始化标题栏控件
        btn_left = (AlphaImageView) findViewById(R.id.btn_left);
        header_title = (AlphaTextview) findViewById(R.id.header_title);
        btn_sright = (AlphaTextview) findViewById(R.id.btn_sright);
        btn_right = (AlphaImageView) findViewById(R.id.btn_right);
        header_line = (View) findViewById(R.id.header_line);

        btn_left.setOnClickListener(listener);
        btn_sright.setOnClickListener(listener);
        btn_right.setOnClickListener(listener);
    }


    public void setTitleModel(TitleModel titleModel) {
        this.titleModel = titleModel;
        //控件隐藏与处理
        btn_left.setVisibility(titleModel.backShow ? VISIBLE : GONE);
        header_title.setVisibility(titleModel.titleShow ? VISIBLE : GONE);
        header_line.setVisibility(titleModel.lineShow ? VISIBLE : GONE);

        //赋值处理
        header_title.setTextV(titleModel.title);
        btn_sright.setText(titleModel.txtRight);
        if (titleModel.Rightimg > 0)
            btn_right.setImageResource(titleModel.Rightimg);

        if (titleModel.clickTitle){
            header_title.setOnClickListener(listener);
        }

    }

    private XJOnClickListener listener=new XJOnClickListener() {
        @Override
        public void onclickView(View view) {
            TitleInterface titleClickListener=titleModel.titleClickListener;
            if (titleClickListener==null)return;
            int i = view.getId();
            if (i == R.id.btn_left) {
                titleClickListener.onClickBack();
            } else if (i == R.id.header_title) {
                titleClickListener.onClickTitle();
            } else if (i == R.id.btn_sright) {
                titleClickListener.onClickRTxt();
            } else if (i == R.id.btn_right) {
                titleClickListener.onClickImg();
            }
        }
    };

    public static class TitleModel {
        /**
         * 返回按钮是否显示
         */
        private boolean backShow = true;
        /**
         * 标题是否显示
         */
        private boolean titleShow = true;
        /**
         * 是否显示分割线
         */
        private boolean lineShow = true;
        /**
         * 标题是否添加点击效果
         */
        private boolean clickTitle=false;

        /**
         * 标题名称
         */
        private String title;
        /**
         * 右边显示文字名称
         */
        private String txtRight;
        /**
         * 右边显示图标
         */
        private int Rightimg = 0;
        private TitleInterface titleClickListener;

        public TitleModel setBackShow(boolean backShow) {
            this.backShow = backShow;
            return this;
        }

        public TitleModel setTitleShow(boolean titleShow) {
            this.titleShow = titleShow;
            return this;
        }
        public TitleModel setClickTitle(boolean clickTitle) {
            this.clickTitle = clickTitle;
            return this;
        }

        public TitleModel setLineShow(boolean lineShow) {
            this.lineShow = lineShow;
            return this;
        }

        public TitleModel setTitle(String title) {
            this.title = title;
            return this;
        }

        public TitleModel setTxtRight(String txtRight) {
            this.txtRight = txtRight;
            return this;
        }

        public TitleModel setRightimg(int rightimg) {
            Rightimg = rightimg;
            return this;
        }

        public TitleModel setTitleClickListener(TitleInterface titleClickListener) {
            this.titleClickListener = titleClickListener;
            return this;
        }

        public void Builder(TitleView titleView) {
            titleView.setTitleModel(this);
        }
    }
}
