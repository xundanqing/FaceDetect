package com.example.facedetectver4.Widget;











import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;


public class ScanLine  extends ImageView {

    private  int   ScanlineWidth;
    private  Rect  ScanLineRect;

    public   ScanLine(Context context) {
            super(context);
        }

    public   ScanLine(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

    public   ScanLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public   ScanLine(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:重新定义扫描线的宽度信息和位置信息
     * @author xundanqing
     * @CreateDate: 2019/3/6 11:13
     */
    public  void  ScanLineViewLocation(int left, int top, int  ScanLineWidth) {
        ViewGroup.MarginLayoutParams margin9 = new ViewGroup.MarginLayoutParams(this.getLayoutParams());
        margin9.setMargins(left, top, 0, 0);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(margin9);
        layoutParams.width = ScanLineWidth;
        this.setLayoutParams(layoutParams);
    }
    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:设置扫描线的涌运动范围
     * @author xundanqing
     * @CreateDate: 2019/3/6 11:14
     */
    public    void  ScanLineViewAnition(int xfrom,int  xend,int  yfrom,int  yend){
        Animation verticalAnimation = new TranslateAnimation(xfrom, xend, yfrom,yend);
        verticalAnimation.setDuration(3000);
        verticalAnimation.setRepeatCount(Animation.INFINITE);
        this.setAnimation(verticalAnimation);
        verticalAnimation.startNow();
    }
    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:调节对话框的位置信息和扫描范围
     * @author xundanqing
     * @CreateDate: 2019/3/6 11:18
     */
    public   void ScanlineAdjust(Rect  DrawRect){
        int  Width =  DrawRect.width() - 20;
        ScanLineViewLocation(DrawRect.left + 10,DrawRect.top+10,Width);
        Log.d("Scanline  WIDTH: " , " "+  Width);
        ScanLineViewAnition(0,0,10,DrawRect.width()-10);
    }
}
