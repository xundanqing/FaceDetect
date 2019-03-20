package com.example.facedetectver4.Fragments;

import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.example.facedetectver4.R;
import com.example.facedetectver4.SurfaceView.CameraSurfaceview;
import com.example.facedetectver4.SurfaceView.CameraViewInterface;
import com.example.facedetectver4.Widget.MaskTextView;
import com.example.facedetectver4.Widget.MaskView;
import com.example.facedetectver4.Widget.ScanLine;
import com.example.facedetectver4.util.FaceDatabase;
import com.example.facedetectver4.util.FaceDetectHelper;

import java.util.ArrayList;
import java.util.List;

public  class  RegisterFragment extends  Fragment implements CameraViewInterface,View.OnClickListener{

    private  TextView           Scantextview;
    private  byte[]             imagebytes;
    private  int                imagewidth;
    private  int                imageheight;
    private  FaceFeature        facefeature;
    private  FaceEngine         faceEngine;
    private  ScanLine           scanLine;
    private  MaskView           maskView;
    private  Button             Buttonok;
    private  Button             Buttondel;
    private  FaceDetectHelper   facedetecthelper;
    private  CameraSurfaceview  cameraSurfaceview;
    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:绘制界面中关闭所有控件为不可点击并关闭
     * @author xundanqing
     * @CreateDate: 2019/3/7 17:05
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View     view = inflater.inflate(R.layout.register, container, false);
        cameraSurfaceview  =  view.findViewById(R.id.CameraSurfaceView);
        cameraSurfaceview.SurfaceViewSetInterface(this);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        maskView      =  view.findViewById(R.id.Maskview);
        scanLine      =  view.findViewById(R.id.Scanline);
        Buttonok      =  view.findViewById(R.id.Buttonok);
        Buttondel     =  view.findViewById(R.id.Buttondel);
        Scantextview  =  view.findViewById(R.id.scanresult);
        if(faceEngine == null){
            faceEngine    =    new FaceEngine();
        }
        if(facedetecthelper == null){
            facedetecthelper = new FaceDetectHelper(faceEngine,getContext());
        }
        facedetecthelper.FaceEngineInit();
        Buttondel.setOnClickListener(this);
        Buttonok.setOnClickListener(this);
        imagebytes = null;
        facefeature= null;
        Scantextview.setText(R.string.scaning);
        return   view;
    }
    /*
     * @Titl: 
     * @Param
     * @Return: 
     * @Description:cameraSurfaceView  绘制成功后调整回调函数
     * @author xundanqing
     * @CreateDate: 2019/3/7 9:18
     */
    @Override
    public void onCameraOpened(Camera camera, SurfaceView CameraRect) {

        Rect  SurfaceRect =   new Rect(CameraRect.getLeft(),
                CameraRect.getTop(),
                CameraRect.getRight(),
                CameraRect.getBottom());

        Rect  ScanRect    =   new Rect(CameraRect.getLeft() + CameraRect.getWidth()*1/5 ,
                CameraRect.getTop()    + CameraRect.getHeight()*1/5,
                CameraRect.getRight()  - CameraRect.getWidth()*1/5,
                CameraRect.getBottom() -CameraRect.getHeight()*1/5);

        maskView.DrawView(SurfaceRect,ScanRect);
        scanLine.ScanLineViewLocation(ScanRect.left,ScanRect.top,ScanRect.width());
        scanLine.ScanLineViewAnition (0,0,0,ScanRect.bottom - ScanRect.top -2*15) ;
    }
    /*
     * @Titl:
     * @Param
     * @Return:a
     * @Description:Camera 数据预览
     * @author xundanqing
     * @CreateDate: 2019/3/7 9:19
     */
    @Override
    public void onCameraPreview(byte[] bytes, Camera camera, SurfaceView surfaceView) {

        List<FaceInfo>       faceInfoList =  new ArrayList<>();
        List<FaceFeature> faceFeatureList =  new ArrayList<>();
        if(facedetecthelper.FaceExtraFeature(bytes,camera.getParameters().getPreviewSize(),
                                                                   faceInfoList,faceFeatureList)){
            imagebytes  =  bytes;
            imagewidth  =  camera.getParameters().getPreviewSize().width;
            imageheight =  camera.getParameters().getPreviewSize().height;
            facefeature =  faceFeatureList.get(0);
            Scantextview.setText(R.string.scanok);
        }
    }
    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:确定和取消按键回调函数
     * @author xundanqing
     * @CreateDate: 2019/3/12 16:51
     */
    @Override
    public  void  onClick(View v){
        Toast.makeText(getContext(),"SAVE  OK",Toast.LENGTH_SHORT).show();

        switch (v.getId()){

            case R.id.Buttonok:{
                if(facefeature!=null && imagebytes !=null){
                    FaceDatabase facedatabase =  FaceDatabase.getinstance(getContext());
                    facedatabase.FaceRegister(facefeature,"xundanqing",imagebytes,imagewidth,imageheight);
                }
                facefeature = null;
                imagebytes  = null;
                Toast.makeText(getContext(),"SAVE  OK",Toast.LENGTH_SHORT).show();
                break;
            }

            case R.id.Buttondel:{



                break;
            }
        }
    }
}
