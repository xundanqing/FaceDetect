package com.example.facedetectver4.SurfaceView;

import android.hardware.Camera;
import android.view.SurfaceView;

public interface CameraViewInterface {
    public   void  onCameraOpened   (Camera camera, SurfaceView surfaceView);
    public   void  onCameraPreview  (byte[]  bytes, Camera camera,SurfaceView surfaceView);
}
