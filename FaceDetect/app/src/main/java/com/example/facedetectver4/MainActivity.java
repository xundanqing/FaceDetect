package com.example.facedetectver4;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.example.facedetectver4.Fragments.RegisterFragment;
import com.example.facedetectver4.util.Configutil;

public class MainActivity extends AppCompatActivity {

    private        FaceEngine faceEngine;
    private int    PERMISSON_ACKSUCESS  =  0X01;

    String  []     neededPermissions    = {
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if(PermisssonRequest()){
           if(FaceEngineInit()){
               SwitchDefaultPage();
           }else {
               AlertExit();
           }
        }
    }
    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:申请权限若不成功，在回调函数中运行时权限确定进行引擎激活
     * @author xundanqing
     * @CreateDate: 2019/3/2 11:32
     */
    public    boolean   PermisssonRequest(){
        boolean allGranted = true;
        for (String neededPermission : neededPermissions) {
            allGranted &= ContextCompat.checkSelfPermission(this.getApplicationContext(), neededPermission) == PackageManager.PERMISSION_GRANTED;
        }
        return allGranted;
    }
    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:运行时权限查看
     * @author xundanqing
     * @CreateDate: 2019/3/2 11:33
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        boolean isAllGranted = true;
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSON_ACKSUCESS) {
            for (int grantResult : grantResults) {
                isAllGranted &= (grantResult == PackageManager.PERMISSION_GRANTED);
            }
            if(isAllGranted){
               if(FaceEngineInit()){
                   SwitchDefaultPage();
               }
            }else{
                Log.d("MainActivity","Register  Fragment  error");
                AlertExit();
            }
        }
    }
    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:识别引擎初始化
     * @author xundanqing
     * @CreateDate: 2019/3/12 15:56
     */
    public   boolean   FaceEngineInit(){

        Configutil    config   = Configutil.getintance();
        faceEngine    =  new FaceEngine();
        int  activecode   =   faceEngine.active(this,config.getDevid(),config.getAppid());
        if(activecode == ErrorInfo.MOK || activecode== ErrorInfo.MERR_ASF_ALREADY_ACTIVATED){
            return   true;
        }
        return   false;
    }
    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:切换到指定的碎片显示中
     * @author xundanqing
     * @CreateDate: 2019/3/12 15:58
     */
    public   void  SwitchDefaultPage(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        RegisterFragment    prefFragment    = new RegisterFragment();
        transaction.add(R.id.MainLine, prefFragment);
        transaction.commit();
    }
    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:在不具备条件的情况下退出
     * @author xundanqing
     * @CreateDate: 2019/3/12 15:59
     */
    public   void  AlertExit(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("请检查网络连接或Licence");
        dialog.setIcon(R.drawable.activierror);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                finish();
            }
        });
        dialog.show();

    }
}
