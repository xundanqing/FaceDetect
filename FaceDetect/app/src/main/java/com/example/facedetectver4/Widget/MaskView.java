package com.example.facedetectver4.Widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.facedetectver4.R;

import static com.example.facedetectver4.R.color.colorScanline;

public class MaskView  extends View{

    private  Rect     RectArea;
    private  Rect     CanvasRect;
    private  Paint    rectPaint;
    private  Context  context;
    public   MaskView(Context context) {
        super(context);
    }
    public   MaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public   MaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context  =  context;
    }

    public   MaskView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context =  context;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(CanvasRect  == null || RectArea == null){
             return;
        } else{

            DrawAlphRect(canvas);
            DrawScanRect(canvas);
            DrawRectLine(canvas);
            DrawText(canvas);
        }
    }
    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:在设置好扫描框之后调用方法进行绘制
     * @author xundanqing
     * @CreateDate: 2019/3/6 10:57
     */
    public  void   DrawView(Rect  CanvasRect, Rect RectArea){
        if(CanvasRect == null  ||  RectArea == null){
            return;
        }else{
            this.CanvasRect  =  CanvasRect;
            this.RectArea    =  RectArea;
            postInvalidate();
        }
    }
    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:绘制扫描框
     * @author xundanqing
     * @CreateDate: 2019/3/6 10:59
     */
    private  void  DrawAlphRect(Canvas canvas){

        /*Draw Top  Rect  左上右下*/
        int  Left;
        int  Top;
        int  Right;
        int  Bottm;
        /*Set  Alph  Color*/
        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectPaint.setColor(Color.BLACK);
        rectPaint.setStyle(Paint.Style.FILL);
        rectPaint.setAlpha(150);

        /*Draw  Top  Rect View*/
        Left  =  0;
        Top   =  0;
        Right =  CanvasRect.right;
        Bottm =  RectArea.top;
        canvas.drawRect(Left,Top, Right,Bottm, rectPaint);

        /*Draw  Left  Rect*/
        Left  =  0;
        Top   =  RectArea.top;
        Right =  RectArea.left;
        Bottm =  RectArea.bottom;
        canvas.drawRect(Left,Top, Right,Bottm, rectPaint);

        /*Draw  Bottom  Rect*/
        Left  = 0;
        Top   = RectArea.bottom;
        Right = CanvasRect.right;
        Bottm = CanvasRect.bottom;
        canvas.drawRect(Left,Top, Right,Bottm, rectPaint);

        /*Right  Rect View*/
        Left  = RectArea.right;
        Top   = RectArea.top;
        Right = CanvasRect.right;
        Bottm = RectArea.bottom;
        canvas.drawRect(Left,Top, Right,Bottm, rectPaint);

    }
    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:画框
     * @author xundanqing
     * @CreateDate: 2019/3/8 10:04
     */
    public   void  DrawRectLine(Canvas  canvas){
        /*Right  Rect View*/
        int  Left  = 0;
        int  Top   = 0;
        int  Right = CanvasRect.right;
        int  Bottm = CanvasRect.bottom;

        Paint  paint  = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        canvas.drawRect(Left,Top,Right,Bottm,paint);
    }
    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:绘制扫描区方框
     * @author xundanqing
     * @CreateDate: 2019/3/6 10:58
     */
    private   void  DrawScanRect(Canvas canvas){

        Paint paint =  new Paint();
        Path  path  =  new Path();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setColor(Color.WHITE);

        path.moveTo(RectArea.left,RectArea.top + RectArea.height()/8);
        path.lineTo(RectArea.left,RectArea.top);
        path.lineTo(RectArea.left+RectArea.width()/8,RectArea.top);

        path.moveTo(RectArea.left, RectArea.top + RectArea.height() /8);
        path.lineTo(RectArea.left, RectArea.top);
        path.lineTo(RectArea.left + RectArea.width()/8, RectArea.top);

        path.moveTo(RectArea.right - RectArea.width()/8, RectArea.top);
        path.lineTo(RectArea.right, RectArea.top);
        path.lineTo(RectArea.right, RectArea.top + RectArea.height()/8);

        path.moveTo(RectArea.right, RectArea.bottom - RectArea.height()/8);
        path.lineTo(RectArea.right, RectArea.bottom);
        path.lineTo(RectArea.right - RectArea.width()/8, RectArea.bottom);

        path.moveTo(RectArea.left + RectArea.width() / 8, RectArea.bottom);
        path.lineTo(RectArea.left, RectArea.bottom);
        path.lineTo(RectArea.left, RectArea.bottom - RectArea.height() / 8);
        canvas.drawPath(path, paint);
    }
    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:绘制文字
     * @author xundanqing
     * @CreateDate: 2019/3/12 10:04
     */
    private   void  DrawText(Canvas canvas){

        Paint paint =  new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setTextSize(20);
        paint.setColor(Color.WHITE);
        canvas.drawText(getResources().getString(R.string.scantext),CanvasRect.left + CanvasRect.width()/3 , CanvasRect.bottom-10, paint);
    }

}
