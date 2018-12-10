package com.xj.mainframe.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.xj.applypermission.Acp;
import com.xj.applypermission.AcpListener;
import com.xj.applypermission.AcpOptions;
import com.xj.mainframe.configer.APPLog;
import com.xj.mainframe.configer.LimitManager;
import com.xj.mainframe.configer.ToastUtils;
import com.xj.mainframe.corp.CorpActivity;
import com.xj.mainframe.corp.CorpModel;
import com.xj.mainframe.dialog.AlertDialog;
import com.xj.mainframe.eventBus.EventManger;
import com.xj.mainframe.eventBus.EventObserver;
import com.xj.mainframe.listener.AlertInterface;
import com.xj.mainframe.listener.CameraBackListener;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.xj.mainframe.BaseApplication.context;

/**
 * 相机工具类
 * Created by xj on 2018/12/5.
 */

public class CameraUtils implements AlertInterface, EventObserver {

    /**
     * 图片剪切方式
     */
    public enum CremeType {
        NO,//不见切
        Cut//剪切
    }

    private Activity activity;
    private CameraBackListener listener;
    /**
     * 拍照路径
     */
    // 拍照临时图片
    private String mTempPhotoPath;
    /**
     * 屏幕宽度
     */
    private int sizeW = 0;
    /**
     * 剪切比列
     */
    private int cutRatioW = 0;
    /**
     * 裁剪宽度比列
     */
    private int cutRatioH = 0;
    /**
     * 设置剪切类型
     */
    private CremeType type = CremeType.Cut;

    public CameraUtils(@NonNull Activity activity, CameraBackListener listener) {
        this.activity = activity;
        this.listener = listener;
        mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.png";

        sizeW = DensityUtil.getScreenW(activity);
        cutRatioW = (int) (sizeW * 0.5);
        cutRatioH = (int) (sizeW * 0.5);

        EventManger.getInstance().registerObserver(CorpActivity.CORP_CODE,this);
    }

    /**
     * 设置剪切宽高
     *
     * @param cutRatioW
     */
    public CameraUtils setCutRatio(int cutRatioW, int cutRatioH) {
        this.cutRatioW = cutRatioW;
        this.cutRatioH = cutRatioH;
        return this;
    }

    public CameraUtils setType(CremeType type) {
        this.type = type;
        return this;
    }

    public void startCamera() {
        new AlertDialog(activity, 1001)
                .setTitle("提示")
                .setCancelable(true)
                .setMsg("请选择图片获取方式")
                .setNegativeButton("相册")
                .setPositiveButton("拍照")
                .setAlertInterface(this)
                .show();
    }


    @Override
    public boolean onNegative(int code) {
        //相册
        Acp.getInstance(context).request(new AcpOptions.Builder().setPermissions(LimitManager.READ_AND_WRITE_EXTERNAL_STORAGE).build(), new AcpListener() {
            @Override
            public void onGranted() {
                Intent intent4 = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent4.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        StringUtils.IMAGE_FORMAT);
                activity.startActivityForResult(intent4, StringUtils.PHOTO_ALBUM);
            }

            @Override
            public void onDenied(List<String> permissions) {
                ToastUtils.getInstance().showToastShort("未获得SD卡读取权限，无法查看本地图片");
            }
        });
        return true;
    }

    @Override
    public boolean onPositive(int code) {
        //拍照
        Acp.getInstance(context).request(new AcpOptions.Builder().setPermissions(LimitManager.CAMERA).build(), new AcpListener() {
            @Override
            public void onGranted() {
                File file = new File(mTempPhotoPath);
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent1.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(file));
                activity.startActivityForResult(intent1, StringUtils.PHOTOH_TAKE);
            }

            @Override
            public void onDenied(List<String> permissions) {
                ToastUtils.getInstance().showToastShort("未获得拍照权限，无法完成拍照");
            }
        });

        return true;
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    private void startCropActivity(Uri uri) {
        String path = GetPathFromUri4kitkat.getPath(context, uri);
        APPLog.e("startCropActivity-path", path);
        CorpModel model = new CorpModel();
        model.setCorpWidth(cutRatioW).setCorpHeight(cutRatioH);
        CorpActivity.startCorpActivity(activity, model, path);
    }

    /**
     * 进入activity返回类型交给本类托管
     *
     * @param requestCode 请求值
     * @param resultCode  返回值
     * @param data        返回数据集
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case StringUtils.PHOTO_ALBUM:// 相册
                if (type == CremeType.NO) {
                    String path = GetPathFromUri4kitkat.getPath(context, data.getData());

                    setListener(path);
                } else if (type == CremeType.Cut) {
                    if (data != null) {
                        startCropActivity(data.getData());
                    }
                }
                break;
            case StringUtils.PHOTOH_TAKE:// 拍照
                /**
                 * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
                 */
                int degree = ImageLoadUtils.readPictureDegree(mTempPhotoPath);
                if (degree != 0) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeFile(mTempPhotoPath);
                        bitmap = ImageLoadUtils.rotaingImageView(degree, bitmap);
                        ImageLoadUtils.saveImageFile(bitmap, mTempPhotoPath);
                        if (bitmap != null && !bitmap.isRecycled()) bitmap.recycle();
                    } catch (IOException e) {
                        ToastUtils.getInstance().showToastShort("图片处理异常");
                        return;
                    }
                }
                if (type == CremeType.NO) {
                    setListener(mTempPhotoPath);
                } else if (type == CremeType.Cut) {
                    File picture = new File(mTempPhotoPath);
                    if (picture.exists()) {
                        startCropActivity(Uri.fromFile(picture));
                    }
                }
                break;
            default:
                break;
        }
    }

    private void setListener(String path) {
        if (listener == null) return;
        if (new File(path).exists()) {
            listener.onCameraBack(path);
        } else {
            ToastUtils.getInstance().showToastShort("图片不存在");
        }
    }

    @Override
    public void eventUpdate(int code, Object data) {
        if (code == CorpActivity.CORP_CODE) {
            if (data instanceof String) {
                String value = data.toString();
                if (!value.equals(""))
                    setListener(value);
                else ToastUtils.getInstance().showToastShort("剪切图片出错！！");
            }
        }
    }
    public void ondetory(){
        EventManger.getInstance().removeObserver(CorpActivity.CORP_CODE);
    }

}
