package com.xj.mainframe.corp;

import java.io.Serializable;

/**
 * 图片剪切设置model
 * Created by xj on 2018/12/5.
 */
public class CorpModel implements Serializable {
    /**
     * 图片剪切风格
     */
    private  int corpStyle=CorpStyle.SQUARE;
    /**
     * 图片剪切显示框的宽
     */
    private int corpWidth=0;
    /**
     * 图片剪切显示的高
     */
    private int corpHeight=0;
    private String savePath="";


    public CorpModel setCorpWidth(int corpWidth) {
        this.corpWidth = corpWidth;
        return this;
    }

    public CorpModel setCorpHeight(int corpHeight) {
        this.corpHeight = corpHeight;
        return this;
    }

    public CorpModel setCorpStyle(int corpStyle) {
        this.corpStyle = corpStyle;
        return this;
    }
    public CorpModel setSavePath(String savePath) {
        this.savePath = savePath;
        return this;
    }
    public String getSavePath() {
        return savePath;
    }

    public int getCorpStyle() {
        return corpStyle;
    }

    public int getCorpWidth() {
        return corpWidth;
    }

    public int getCorpHeight() {
        return corpHeight;
    }

}
