package com.tao.customviewlearndemo.nview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tao.customviewlearndemo.R;

/**
 * Created by SDT14324 on 2018/1/17.
 */

public class SwitchView extends RelativeLayout {
    private String TAG = "SwitchView";
    private ImageView mSwitchOut;
    private ImageView mSwitchIn;
    private ImageView mIvArrow;

    private int mAngle = 0;

    private Drawable[] outArray = new Drawable[2];
    private Drawable[] inArray = new Drawable[2];
    private int curMode;
    private boolean mEnabled = false;

    private int width;
    private int height;
    public SwitchView(Context context) {
        this(context,null);
//        Log.i(TAG,"SwitchView 1");
    }

    public SwitchView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
//        Log.i(TAG,"SwitchView 2");
    }

    public SwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        Log.i(TAG,"SwitchView 3");
        View view = LayoutInflater.from(context).inflate(R.layout.view_switch,this);
        mSwitchOut = view.findViewById(R.id.switch_out);
        mSwitchIn = view.findViewById(R.id.switch_in);
        mIvArrow = view.findViewById(R.id.switch_arrow);
        initDrawable();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        Log.i(TAG,"onMeasure width = "+width+", height = "+height);
    }

    private void initDrawable() {
        outArray[0] = getResources().getDrawable(R.drawable.switch_off);
        outArray[1] = getResources().getDrawable(R.drawable.switch_on);
        inArray[0] = getResources().getDrawable(R.drawable.switch_off2);
        inArray[1] = getResources().getDrawable(R.drawable.switch_on2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            {
                float x = event.getX();
                float y = event.getY();
                Log.i(TAG,"x = "+x+", y = "+y);
                if(x > width || y > height){
                    setDisableMode();
                }else {
                    if(!mEnabled){
                        setEnableMode();
                    }else {
                        switchMode();
                    }
                    return true;
                }
            }
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setDisableMode(){
        mEnabled = false;
        mSwitchOut.setVisibility(View.INVISIBLE);
        mSwitchIn.setVisibility(View.VISIBLE);
        mIvArrow.clearAnimation();
        mIvArrow.setVisibility(View.INVISIBLE);
    }
    private void setEnableMode() {
        mEnabled = true;
        mSwitchOut.setVisibility(View.VISIBLE);
        mSwitchIn.setVisibility(View.INVISIBLE);

        if(curMode == 1){
            rotateAngle(360);
        }else {
            rotateAngle(180);
        }
    }
    private void rotateAngle(float angle) {
        if(mIvArrow != null ){
            RotateAnimation animation = new RotateAnimation(angle,angle,
                    Animation.RELATIVE_TO_SELF,0.5f,
                    Animation.RELATIVE_TO_SELF,0.5f);
            animation.setFillAfter(true);
            mIvArrow.startAnimation(animation);
        }
    }

    private void rotateArrow(float from, float to) {
        if(mIvArrow != null ){
            RotateAnimation animation = new RotateAnimation(from,to,
                    Animation.RELATIVE_TO_SELF,0.5f,
                    Animation.RELATIVE_TO_SELF,0.5f);
            animation.setFillAfter(true);
            animation.setDuration(50);
            mIvArrow.startAnimation(animation);
        }
    }

    private void switchMode() {
        curMode = (curMode + 1) % 2;
        if(mEnabled){
            mSwitchOut.setImageDrawable(outArray[curMode]);
            mSwitchIn.setImageDrawable(inArray[curMode]);
            if(curMode == 1){
                rotateArrow(180,360);
            }else {
                rotateArrow(0,180);
            }


        }else {
            mSwitchOut.setImageDrawable(outArray[curMode]);
            mSwitchIn.setImageDrawable(inArray[curMode]);
        }
    }
}