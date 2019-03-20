package com.example.facedetectver4.SurfaceView;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

import static android.media.MediaRecorder.VideoSource.CAMERA;


public class CameraSurfaceview extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private   Camera                 camera;
    private   SurfaceHolder          surfaceHolder;
    private   CameraViewInterface    cameraViewInterface;

    public CameraSurfaceview(Context context) {
        super(context);
        surfaceHolder =  getHolder();
        surfaceHolder.addCallback(this);
    }

    public CameraSurfaceview(Context context, AttributeSet attrs) {
        super(context, attrs);
        surfaceHolder =  getHolder();
        surfaceHolder.addCallback(this);
    }

    public CameraSurfaceview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        surfaceHolder =  getHolder();
        surfaceHolder.addCallback(this);
    }

    public CameraSurfaceview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        surfaceHolder =  getHolder();
        surfaceHolder.addCallback(this);
    }
    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:设置CameraSurFaceView Camera相关回调
     * @author xundanqing
     * @CreateDate: 2019/3/1 15:34
     */
    public  void  SurfaceViewSetInterface(CameraViewInterface CallBack){
        if(CallBack!=null){
            cameraViewInterface =  CallBack;
        }
    }
    /*
    * @Titl:
    * @Param
    * @Return:
    * @Description:SurFaceView 创建成功回调添加绘制成功后回调接口onGlobalLayout
    * @author xundanqing
    * @CreateDate: 2019/3/1 15:14
    */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if(camera == null){
            camera = Camera.open();
            if(camera == null){
                Log.d("Camera","Camera  open  error");
                return;
            }
        }
        try
        {
            camera.setPreviewDisplay(this.getHolder());
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.setPreviewCallback(this);
        if(cameraViewInterface!=null){
            cameraViewInterface.onCameraOpened(camera,this);
        }
        Point point = new Point(this.getMeasuredWidth(),this.getMeasuredHeight());
        camera.startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }
    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:SurfaceView 销毁后释放SurFaceView中的Camera
     * @author xundanqing
     * @CreateDate: 2019/3/1 15:16
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if(camera != null){
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
        }
        camera = null;
    }
    /*
    * @Titl:
    * @Param
    * @Return:
    * @Description:外部的设置SurfaceView 的预览回调
    * @author xundanqing
    * @CreateDate: 2019/3/1 15:19
    */
    @Override
    public void onPreviewFrame(byte[] var1, Camera var2){
        if(cameraViewInterface!=null){
            cameraViewInterface.onCameraPreview(var1,var2,this);
        }
    }
    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:获得SURface中的Camera
     * @author xundanqing
     * @CreateDate: 2019/3/12 15:44
     */
    public   Camera  SurfaceCamera(){
        return  camera;
    }
}

