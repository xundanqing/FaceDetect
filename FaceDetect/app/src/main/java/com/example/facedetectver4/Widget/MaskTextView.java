package com.example.facedetectver4.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;


public class MaskTextView extends  TextView {

    public MaskTextView(Context context) {
        super(context);
    }

    public MaskTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaskTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MaskTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    /*
     * @Titl:
     * @Param
     * @Return:
     * @Description:动态调节TextView位置
     * @author xundanqing
     * @CreateDate: 2019/3/7 10:13
     */
    public  void  TextViewLocation(int left, int top) {
        ViewGroup.MarginLayoutParams margin9 = new ViewGroup.MarginLayoutParams(this.getLayoutParams());
        margin9.setMargins(left, top, 0, 0);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(margin9);
        this.setLayoutParams(layoutParams);
    }

}
