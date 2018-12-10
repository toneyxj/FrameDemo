package com.xj.mainframe.corp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.xj.mainframe.utils.ImageLoadUtils;

import static com.xj.mainframe.utils.ImageLoadUtils.saveImageFile;

/**
 * 图片剪切布局layout
 * Created by xj on 2018/12/5.
 */
public class CorpLayout extends FrameLayout {
    public CorpLayout(Context context) {
        this(context, null);
    }

    public CorpLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CorpLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addView();
    }

    private CorpView corpView;
    private CorpImageView corpImageView;

    private void addView() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        corpView = new CorpView(getContext());
        corpView.setLayoutParams(params);


        corpImageView = new CorpImageView(getContext());
        corpImageView.setLayoutParams(params);
        corpImageView.setScaleType(ImageView.ScaleType.MATRIX);

        addView(corpImageView);
        addView(corpView);
    }

    /**
     * 图片剪切的基本参数设置
     *
     * @param model 图参数model
     * @param path  背景图片地址
     */
    public void setCorpValue(final CorpModel model, final String path) {
        corpView.setModel(model);
        corpImageView.loadImage(path);
    }

    /**
     * 获得剪切的图片保存后的地址
     */
    public String getCorpBitmap() {
        String savePath = "";
        try {
            corpImageView.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(corpImageView.getDrawingCache());
            corpImageView.setDrawingCacheEnabled(false);
            int[] fl = corpView.getCrop();
            Bitmap bmp = Bitmap.createBitmap(bitmap, fl[2], fl[3], fl[0], fl[1]);
            ImageLoadUtils.recycleBitmap(bitmap);

            Paint paint = new Paint();
            paint.setAntiAlias(true); // 去除锯齿
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.BLACK);
            Xfermode xFermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
            paint.setXfermode(xFermode);

            Canvas canvas = new Canvas(bmp);
            canvas.drawPath(corpView.getDrawPath(), paint);

            if (corpView.getModel().getSavePath().equals("")) {
                savePath = saveImageFile(bmp);
            } else {
                savePath = ImageLoadUtils.saveImageFile(bmp, corpView.getModel().getSavePath());
                ImageLoadUtils.recycleBitmap(bmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return savePath;
    }
}
