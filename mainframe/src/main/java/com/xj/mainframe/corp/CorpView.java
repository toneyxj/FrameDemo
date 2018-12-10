package com.xj.mainframe.corp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

import com.xj.mainframe.utils.DensityUtil;

/**
 * 裁剪图片显示框的造型,
 * Created by xj on 2018/12/5.
 */
public class CorpView extends View {
    public CorpView(Context context) {
        this(context, null);
    }

    public CorpView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CorpView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    //目前显示框的图像就只有一个方形
    private CorpModel model;

    public void setModel(CorpModel model) {
        if (model.getCorpWidth() == 0) {
            int w = DensityUtil.getScreenW(getContext());
            model.setCorpWidth((int) (w *0.7)).setCorpHeight((int) (w *0.7));
        }
        this.model = model;
        invalidate();
    }

    public CorpModel getModel() {
        return model;
    }

    /**
     * 四个参数
     * 参数1：截取宽宽度
     * 参数2：截取框高度
     * 参数三：截取初始点x
     * 参数4：截取初始点y
     * @return
     */
    public int[] getCrop() {
        int[] ff=new int[4];
        ff[0]=model.getCorpWidth();
        ff[1]=model.getCorpHeight();
        ff[2]=(getWidth()-ff[0])/2;
        ff[3]=(getHeight()-ff[1])/2;
        return ff;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPaint();
        if (model.getCorpWidth() == 0) return;
        int w = getWidth();
        int h = getHeight();
        if (model.getCorpWidth() > w) {
            model.setCorpWidth(w);
        }
        if (model.getCorpHeight() > h) {
            model.setCorpHeight(h);
        }
        Path path=getDrawPath();
        //图片进行反色
        /**
         * 设置View的离屏缓冲。在绘图的时候新建一个“层”，所有的操作都在该层而不会影响该层以外的图像
         * 必须设置，否则设置的PorterDuffXfermode会无效，具体原因不明
         */
        int sc = canvas.saveLayer(0, 0, w, h, paint, Canvas.ALL_SAVE_FLAG);
        canvas.drawARGB(150, 0, 0, 0);

        canvas.drawPath(path, paint);
        /**
         * 还原画布，与canvas.saveLayer配套使用
         */
        canvas.restoreToCount(sc);
        canvas.drawPath(path,bPaint);

    }

    /**
     * 获得绘制中间框的绘制路径
     * @return 返回绘制的path
     */
    public Path getDrawPath(){
        int w = getWidth();
        int h = getHeight();
        Path path = new Path();
        //计算显示框
        switch (model.getCorpStyle()) {
            case CorpStyle.SQUARE://方形
                int left = (w - model.getCorpWidth()) / 2;
                int right = left + model.getCorpWidth();
                int top = (h - model.getCorpHeight()) / 2;
                int bottom = top + model.getCorpHeight();
                path.addRect(left, top, right, bottom, Path.Direction.CCW);
                break;
            case CorpStyle.CIRCLE://圆形
                path.addCircle(w / 2, h / 2, model.getCorpWidth()/2, Path.Direction.CCW);
                break;
        }
        return path;
    }

    private Paint paint;
    private Paint bPaint;

    private void initPaint() {
        if (paint == null) {
            paint = new Paint();
            paint.setAntiAlias(true); // 去除锯齿
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.BLACK);
            Xfermode xFermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
            paint.setXfermode(xFermode);
        }
        if (bPaint==null){
            bPaint = new Paint();
            bPaint.setAntiAlias(true); // 去除锯齿
            bPaint.setStyle(Paint.Style.STROKE);
            bPaint.setStrokeWidth(DensityUtil.dip2px(getContext(),1));
            bPaint.setColor(Color.WHITE);
        }
    }

}
