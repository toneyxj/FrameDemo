package com.xj.mainframe.corp;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.xj.mainframe.view.BaseView.XJImageView;

/**
 * 裁剪图片类，目前确认尺寸为825*1027
 * 主要只是maxtrix矩阵变换
 * 矩阵里面
 * <p>
 * [2]代表着X轴的平移，[5]Y轴的平移
 * [0]X轴的缩放,[4]Y轴的缩放，原始状态为1
 * [1]X轴切错,[3]Y轴切错
 * Created by xj on 2018/2/28.
 */
public class CorpImageView extends XJImageView {

    /**
     * 记录是拖拉照片模式还是放大缩小照片模式
     */
    private int mode = 0;// 初始状态
    /**
     * 拖拉照片模式
     */
    private static final int MODE_DRAG = 1;
    /**
     * 放大缩小照片模式
     */
    private static final int MODE_ZOOM = 2;
    /**
     * 最多放大倍数
     */
    private static final float MAX_ZOOM = 3.0f;
    /**
     * 最多缩小值
     */
    private static final float MIN_ZOOM = 0.2f;

    /**
     * 用于记录开始时候的坐标位置
     */
    private PointF startPoint = new PointF();
    /**
     * 用于记录拖拉图片移动的坐标位置
     */
    private Matrix matrix = new Matrix();
    /**
     * 用于记录图片要进行拖拉时候的坐标位置
     */
    private Matrix currentMatrix = new Matrix();
    private Matrix sourceMatrix = null;

    /**
     * 两个手指的开始距离
     */
    private float startDis;
    /**
     * 两个手指的中间点
     */
    private PointF midPoint;


    public CorpImageView(Context context) {
        super(context);
    }

    public CorpImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CorpImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        if (getDrawable() != null) {
            int width = getDrawable().getIntrinsicWidth();
            int height = getDrawable().getIntrinsicHeight();

            int ww = getWidth();
            int wh = getHeight();

            int top = (wh - height) / 2;
            int left = (ww - width) / 2;
            matrix.set(new Matrix());
            if (top <= 0) {
                top = 0;
            }
            if (left <= 0) {
                left = 0;
            }
            matrix.postTranslate(left, top);
            setImageMatrix(matrix);
        }
    }

    private boolean isup = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /** 通过与运算保留最后八位 MotionEvent.ACTION_MASK = 255 */
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            // 手指压下屏幕
            case MotionEvent.ACTION_DOWN:
                isup = false;
                mode = MODE_DRAG;
                // 记录ImageView当前的移动位置
                if (sourceMatrix == null) sourceMatrix = getImageMatrix();
                currentMatrix.set(getImageMatrix());
                startPoint.set(event.getX(), event.getY());
                break;
            // 手指在屏幕上移动，改事件会被不断触发
            case MotionEvent.ACTION_MOVE:
                // 拖拉图片
                if (mode == MODE_DRAG) {
                    float dx = event.getX() - startPoint.x; // 得到x轴的移动距离
                    float dy = event.getY() - startPoint.y; // 得到y轴的移动距离
                    // 在没有移动之前的位置上进行移动
                    matrix.set(currentMatrix);
                    matrix.postTranslate(dx, dy);
                }
                // 放大缩小图片
                else if (mode == MODE_ZOOM) {
                    float endDis = distance(event);// 结束距离
                    if (endDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                        float scale = endDis / startDis;// 得到缩放倍数
                        matrix.set(currentMatrix);
                        matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                    }
                }
                break;
            // 手指离开屏幕
            case MotionEvent.ACTION_UP:
                // 当触点离开屏幕，但是屏幕上还有触点(手指)
            case MotionEvent.ACTION_POINTER_UP:
                isup = true;
                mode = 0;
                break;
            // 当屏幕上已经有触点(手指)，再有一个触点压下屏幕
            case MotionEvent.ACTION_POINTER_DOWN:
                isup = false;
                lastDis = 0;
                mode = MODE_ZOOM;
                if (sourceMatrix == null) sourceMatrix = getImageMatrix();
                /** 计算两个手指间的距离 */
                startDis = distance(event);
                /** 计算两个手指间的中间点 */
                if (startDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                    midPoint = mid(event);
                    //记录当前ImageView的缩放倍数
                    currentMatrix.set(getImageMatrix());
                }
                break;
        }
        setImageMatrix(matrix);
        if (isup) matrixRecover();
        return true;
    }

    /**
     * matrix变换后进行恢复到边界值状态
     */
    private void matrixRecover() {
        float[] ff = new float[9];
        matrix.getValues(ff);
        boolean ischange = false;

        float scalX = ff[0];
        if (scalX > MAX_ZOOM) {
            ischange = true;
            float scale = MAX_ZOOM / scalX;// 得到缩放倍数
            matrix.postScale(scale, scale, midPoint.x, midPoint.y);
        } else if (scalX <MIN_ZOOM) {
            ischange = true;
            float scale = MIN_ZOOM / scalX;// 得到缩放倍数
            matrix.postScale(scale, scale, midPoint.x, midPoint.y);
        }
        if (ischange) {
            matrix.getValues(ff);
        }

        matrix.getValues(ff);
        float width = getDrawable().getIntrinsicWidth() * ff[0];
        float height = getDrawable().getIntrinsicHeight() * ff[4];
        float tanX = ff[2];
        float tanY = ff[5];

        float dy=0f;
        float dx=0f;

        float xmin=getWidth()*0.2f;
        float ymin=getHeight()*0.1f;

        if (tanX>(getWidth()-xmin)){
            ischange = true;
            dx=getWidth()-tanX-xmin;
        }else if ((width+tanX)<xmin){
            ischange = true;
            dx=Math.abs(width+tanX-xmin);
        }

        if (tanY>(getHeight()-ymin)){
            ischange = true;
            dy=getHeight()-tanY-ymin;
        }else if ((height+tanY)<ymin){
            ischange = true;
            dy=Math.abs(height+tanY-ymin);
        }
        if (ischange){
            matrix.postTranslate(dx, dy);
        }

        if (ischange) {
            setImageMatrix(matrix);
        }
    }

    public void resetImage() {
        setImageMatrix(new Matrix());
    }

    private float lastDis = 0;

    /**
     * 计算两个手指间的距离
     */
    private float distance(MotionEvent event) {
        try {
            float dx = event.getX(1) - event.getX(0);
            float dy = event.getY(1) - event.getY(0);
            /** 使用勾股定理返回两点之间的距离 */
            lastDis = (float) Math.sqrt(dx * dx + dy * dy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lastDis;
    }

    /**
     * 计算两个手指间的中间点
     */
    private PointF mid(MotionEvent event) {
        float midX = (event.getX(1) + event.getX(0)) / 2;
        float midY = (event.getY(1) + event.getY(0)) / 2;
        return new PointF(midX, midY);
    }

}
