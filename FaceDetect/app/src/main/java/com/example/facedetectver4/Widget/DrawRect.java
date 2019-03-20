package com.example.facedetectver4.Widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.arcsoft.face.FaceInfo;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DrawRect extends View{

    private List<FaceInfo> faceInfos;
    private CopyOnWriteArrayList<FaceInfo> faceRectList = new CopyOnWriteArrayList<>();

    public DrawRect(Context context, List<FaceInfo> faceInfos) {
        super(context);
        this.faceInfos = faceInfos;
    }

    public DrawRect(Context context, AttributeSet attrs, int defStyleAttr, List<FaceInfo> faceInfos) {
        super(context, attrs, defStyleAttr);
        this.faceInfos = faceInfos;
    }

    public DrawRect(Context context, AttributeSet attrs, List<FaceInfo> faceInfos) {
        super(context, attrs);
        this.faceInfos = faceInfos;
    }

    public DrawRect(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, List<FaceInfo> faceInfos) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.faceInfos = faceInfos;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < faceRectList.size(); i++) {
            DrawFacwRect(canvas, faceRectList.get(i).getRect());
        }
    }

    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:在加入到方框队列后进行画框
     * @author xundanqing
     * @CreateDate: 2019/3/6 11:24
     */
    public    void  AddFaceinfoList(List<FaceInfo> list){
        if(list!=null) {
            faceRectList.addAll(list);
            postInvalidate();
        }
    }
    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:清空当前的人脸框信息并清除当前的界面方框
     * @author xundanqing
     * @CreateDate: 2019/3/6 11:25
     */
    public   void  ClearDrawinfo(){

        faceRectList.clear();
        postInvalidate();
    }
    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:画出人脸信息方框
     * @author xundanqing
     * @CreateDate: 2019/3/6 11:26
     */
    private      void  DrawFacwRect(Canvas canvas,Rect rect ){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(Color.YELLOW);
        Path mPath = new Path();

        mPath.moveTo(rect.left, rect.top + rect.height() / 4);
        mPath.lineTo(rect.left, rect.top);
        mPath.lineTo(rect.left + rect.width() / 4, rect.top);

        mPath.moveTo(rect.right - rect.width() / 4, rect.top);
        mPath.lineTo(rect.right, rect.top);
        mPath.lineTo(rect.right, rect.top + rect.height() / 4);

        mPath.moveTo(rect.right, rect.bottom - rect.height() / 4);
        mPath.lineTo(rect.right, rect.bottom);
        mPath.lineTo(rect.right - rect.width() / 4, rect.bottom);

        mPath.moveTo(rect.left + rect.width() / 4, rect.bottom);
        mPath.lineTo(rect.left, rect.bottom);
        mPath.lineTo(rect.left, rect.bottom - rect.height() / 4);
        canvas.drawPath(mPath, paint);
    }
}
