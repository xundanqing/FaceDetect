package com.example.facedetectver4.util;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Environment;
import android.util.Log;

import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceSimilar;

import org.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FaceDatabase {

    private  FaceEngine faceEngine;
    private  boolean    FaceCompareState;
    private  String TAG   =  "FaceDatabase";
    private  static  FaceDatabase        faceDatabase = null;
    private  static  List<FaceFeature>   faceFeatureInfoList =  new ArrayList<>();
    private  String  EnvironmentPath =   Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();


    /*Save  Faceinfo  File  Path*/
    private   String  SAVE_IMG_DIR     = EnvironmentPath + File.separator + "Image";
    private   String  SAVE_FEATURE_DIR = EnvironmentPath + File.separator + "Feature";
    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:获得数据库存放单例
     * @author xundanqing
     * @CreateDate: 2019/3/3 23:57
     */
    public static FaceDatabase  getinstance(Context context){

        if(faceDatabase ==  null){
            faceDatabase  =  new  FaceDatabase(context);
        }
        return   faceDatabase;
    }

    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:Database Construct
     * @author xundanqing
     * @CreateDate: 2019/3/3 23:57
     */
    private  FaceDatabase(Context  context){
        FaceDatabaseInit(context);
    }
    /*
     * @Titl:
     * @Param
     * @Return: 姓名，camera,SIZE,脸部特征值
     * @Description:注册FaceDatabase
     * @author xundanqing
     * @CreateDate: 2019/3/3 23:58
     */
    public   void  FaceRegister(FaceFeature faceFeature, String UserName, byte[] bytes ,
                                int Width, int  Height){

        FileOutputStream fosFeature = null;
        FileOutputStream fosImage   = null;

        /*Save  Feature  File*/
        try {
            fosFeature = new FileOutputStream(SAVE_FEATURE_DIR + File.separator + UserName +".Feature" );
            fosFeature.write(faceFeature.getFeatureData());
            fosFeature.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(bytes != null) {
            File ImgFile         =  new File(SAVE_IMG_DIR + File.separator +UserName + ".jpg");
            try {
                fosImage          = new FileOutputStream(ImgFile);
                YuvImage yuvImage = new YuvImage(bytes, ImageFormat.NV21, Width,Height, null);
                yuvImage.compressToJpeg(new Rect(0,0,Width,Height),70, fosImage);
                fosImage.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        faceFeatureInfoList.add(faceFeature);
    }
    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:数据库初始化
     * @author xundanqing
     * @CreateDate: 2019/3/3 23:58
     */
    public   void  FaceDatabaseInit(Context  context){

        if(faceEngine == null){
            faceEngine =  new FaceEngine();
        }

        int engineCode = faceEngine.init(context, FaceEngine.ASF_DETECT_MODE_IMAGE, FaceEngine.ASF_OP_0_HIGHER_EXT, 16, 1, FaceEngine.ASF_FACE_RECOGNITION | FaceEngine.ASF_FACE_DETECT);
        if(engineCode != ErrorInfo.MOK){
            Log.d(TAG,"Active  error");
            return  ;
        }

        File featureDir = new File(SAVE_FEATURE_DIR);
        if(!featureDir.exists()){
            Log.d(TAG,SAVE_FEATURE_DIR);
            featureDir.mkdirs();
        }else{
            Log.d(TAG,SAVE_FEATURE_DIR);
        }

        if(featureDir.exists()){
            Log.d(TAG,SAVE_FEATURE_DIR);
            return;
        }

        File Imgdir = new File(SAVE_IMG_DIR);
        if(!Imgdir.exists()){
            Imgdir.mkdirs();
        }

        if(Imgdir.exists()&&!Imgdir.isDirectory()){
               Log.d("FaceDatabase","Image file not  exist");
               return;
        }

        File[]  files   =  featureDir.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        for (File file : files)
            try {
                FileInputStream fis = new FileInputStream(file);
                byte[] feature = new byte[FaceFeature.FEATURE_SIZE];
                fis.read(feature);
                fis.close();
                faceFeatureInfoList.add(new FaceFeature(feature));
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:获得脸部注册数量
     * @author xundanqing
     * @CreateDate: 2019/3/3 23:59
     */
    public int GetFaceNum(Context context) {
        synchronized (this) {
            if (context == null) {
                return 0;
            }
            File featureFileDir = new File(SAVE_FEATURE_DIR);
            int featureCount = 0;
            if (featureFileDir.exists() && featureFileDir.isDirectory()) {
                String[] featureFiles = featureFileDir.list();
                featureCount = featureFiles == null ? 0 : featureFiles.length;
            }
            int imageCount = 0;
            File imgFileDir = new File(SAVE_IMG_DIR);
            if (imgFileDir.exists() && imgFileDir.isDirectory()) {
                String[] imageFiles = imgFileDir.list();
                imageCount = imageFiles == null ? 0 : imageFiles.length;
            }
            return featureCount > imageCount ? imageCount : featureCount;
        }
    }
    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:轮训对比
     * @author xundanqing
     * @CreateDate: 2019/3/4 0:00
     */
    public  String  FaceTraversalCompare(FaceFeature faceFeature ){

        float maxSimilar    =  0;
        int maxSimilarIndex = -1;

        if (faceEngine == null || FaceCompareState || faceFeature == null || faceFeatureInfoList == null
                               || faceFeatureInfoList.size() == 0) {
            return null;
        }

        FaceCompareState = true;
        FaceFeature tempFaceFeature = new FaceFeature();
        FaceSimilar faceSimilar     = new FaceSimilar();
        for (int i = 0; i < faceFeatureInfoList.size(); i++) {
            tempFaceFeature.setFeatureData(faceFeatureInfoList.get(i).getFeatureData());
            faceEngine.compareFaceFeature(faceFeature, tempFaceFeature, faceSimilar);
            if (faceSimilar.getScore() > maxSimilar) {
                maxSimilar = faceSimilar.getScore();
                maxSimilarIndex = i;
            }
        }
        FaceCompareState = false;
        if (maxSimilarIndex != -1) {
            Log.d("FaceTraversalCompare","Compare  ok");
            return "xundanqing";
        }
        return  null;
    }

    private boolean delete(String delFile) {
        File file = new File(delFile);
        if (!file.exists()) {
            return false;
        } else {
            deleteSingleFile(delFile);
        }
        return   true;
    }
    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:清空数据库
     * @author xundanqing
     * @CreateDate: 2019/3/4 0:00
     */
    private boolean deleteSingleFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:删除输几局库文件
     * @author xundanqing
     * @CreateDate: 2019/3/4 0:00
     */
    public   void  FaceDataBaseRemove(String UserName){

        delete(SAVE_IMG_DIR + File.separator +UserName + ".jpg");
        delete(SAVE_FEATURE_DIR + File.separator + UserName + ".Feature");
    }
    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:注册用户信息数据字符
     * @author xundanqing
     * @CreateDate: 2019/3/4 14:29
     */
    public  void  UserInfoRegister(String  Username,String Address,String Telnum,String  Id){
        Map<String ,String> map1 =new HashMap<>();
        map1.put("姓名",Username);
        map1.put("住址",Address);
        map1.put("电话",Telnum);
        map1.put("证件",Id);
        JSONArray array3 =new JSONArray();
        array3.put(map1);
        Log.d("UserInfoRegister",array3.toString());

    }
}
