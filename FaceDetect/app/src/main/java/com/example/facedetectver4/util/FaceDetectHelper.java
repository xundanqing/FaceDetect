package com.example.facedetectver4.util;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.widget.Toast;

import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.VersionInfo;

import java.util.List;

import static com.example.facedetectver4.util.DETECT_ENUM.FACE_DETECEND;
import static com.example.facedetectver4.util.DETECT_ENUM.FACE_DETECTING;
import static com.example.facedetectver4.util.DETECT_ENUM.FACE_ENGERR;
import static com.example.facedetectver4.util.DETECT_ENUM.FACE_ENGERROK;


enum   DETECT_ENUM{
    FACE_ENGERR,      /*初始化失败*/
    FACE_ENGERROK,    /*初始化成功*/
    FACE_DETECTING,   /*正在检测*/
    FACE_DETECEND,    /*检测结束*/
};

public class FaceDetectHelper {

    private   Camera      camera;
    private   Context     context;
    private   FaceEngine  faceEngine;
    private   Boolean     FaceEngineState;
    private   String      TAG   = "FaceDetectHelper";

    private   DETECT_ENUM   FACE_STATE;

    public    FaceDetectHelper(FaceEngine engine, Context context) {
       this.faceEngine =  engine;
       this.context    =  context;
    }

   /*
    * @Titl:
    * @Param
    * @Return:
    * @Description:申请引擎初始化
    * @author xundanqing
    * @CreateDate: 2019/3/3 21:18
    */
   public  void  FaceEngineInit(){
       int afCode = faceEngine.init(context, FaceEngine.ASF_DETECT_MODE_VIDEO, FaceEngine.ASF_OP_0_ONLY,
               16, 20, FaceEngine.ASF_FACE_RECOGNITION| FaceEngine.ASF_FACE_DETECT | FaceEngine.ASF_AGE | FaceEngine.ASF_FACE3DANGLE | FaceEngine.ASF_GENDER | FaceEngine.ASF_LIVENESS);

       if (afCode != ErrorInfo.MOK) {
           Toast.makeText(context,"Face  Engine  Init  Error" + afCode, Toast.LENGTH_SHORT).show();
           FACE_STATE =   FACE_ENGERR;
           return;
       }
       FACE_STATE =  FACE_ENGERROK;
       return;
   }

   /*
    * @Titl:
    * @Param
    * @Return:
    * @Description:检测活体人脸信息
    * @author xundanqing
    * @CreateDate: 2019/3/3 21:18
    */
   public Boolean  FaceDetectView(byte[]  bytes , List<FaceInfo> faceInfoList , List<FaceFeature> faceFeatureList){

       Camera.Size size = camera.getParameters().getPreviewSize();
       int code = faceEngine.detectFaces(bytes, size.width, size.height, FaceEngine.CP_PAF_NV21, faceInfoList);
       if(code !=  ErrorInfo.MOK ){
           Log.d("Face  Detect","Face Detect  error" + code);
           return  false ;
       }else{
           if(faceInfoList.size()== 0){
               Log.d("Face Detect","Face Detect  no face");
               return  false;
           }
       }

       /*
       int  iReturn = faceEngine.process(bytes,size.width, size.height, FaceEngine.CP_PAF_NV21, faceInfoList);
       if(iReturn != ErrorInfo.MOK){
           Log.d("face Detect","Face  Detect Process  return :" +  iReturn);
           return  false;
       }
       */

       for (int i = 0; i < faceInfoList.size(); i++) {
           FaceFeature faceFeature  =  new FaceFeature();
           int  iresult = faceEngine.extractFaceFeature(bytes, size.width, size.height, FaceEngine.CP_PAF_NV21,
                                                                                faceInfoList.get(i), faceFeature);
           if(iresult == ErrorInfo.MOK){
               Log.d("Register","Register  mok");
               faceFeatureList.add(faceFeature);
           }else{
               Log.d("Register","Register  merror" + iresult);
               faceFeatureList.add(null);
           }
       }

       /*
       List<AgeInfo>       ageInfoList = new ArrayList<>();
       List<GenderInfo>    genderInfoList = new ArrayList<>();
       List<Face3DAngle>   face3DAngleList = new ArrayList<>();
       List<LivenessInfo> faceLivenessInfoList = new ArrayList<>();
       int ageCode =    faceEngine.getAge(ageInfoList);
       int genderCode = faceEngine.getGender(genderInfoList);
       int face3DAngleCode = faceEngine.getFace3DAngle(face3DAngleList);
       int livenessCode =    faceEngine.getLiveness(faceLivenessInfoList);
       if ((ageCode | genderCode | face3DAngleCode | livenessCode) != ErrorInfo.MOK) {
           Log.d("Face detect","return  -3");
           return  false;
       }
       */
       return   true;
    }

    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:提起人体特征值
     * @author xundanqing
     * @CreateDate: 2019/3/3 21:18
     */
    public   boolean  FaceExtraFeature(byte[]  bytes , Camera.Size  size,List<FaceInfo> faceInfoList , List<FaceFeature> faceFeatureList){

        if(faceFeatureList == null ||  faceInfoList == null||bytes==null){
            return false;
        }

        if(FACE_STATE  == FACE_ENGERR ||FACE_STATE == FACE_DETECTING){
            return  false;
        }
        FACE_STATE =  FACE_DETECTING;
        int code =  faceEngine.detectFaces(bytes, size.width, size.height, FaceEngine.CP_PAF_NV21, faceInfoList);
        if(code !=  ErrorInfo.MOK ){
            FACE_STATE =  FACE_DETECEND;
            return  false ;
        }else{
            if(faceInfoList.size()== 0){
                Log.d("FaceDetect","Detect  noface");
                FACE_STATE =  FACE_DETECEND;
                return  false;
            }
        }
        FaceFeature faceFeature  =  new FaceFeature();
        int  iresult = faceEngine.extractFaceFeature(bytes, size.width, size.height, FaceEngine.CP_PAF_NV21,
                faceInfoList.get(0), faceFeature);

        if(iresult != ErrorInfo.MOK){
            Log.d("Face  Detect","Face  Exture  code" + iresult);
        }
        faceFeatureList.add(faceFeature);
        FACE_STATE =  FACE_DETECEND;
        return true;
    }
}
