package com.ovadyah.echome.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.ovadyah.echome.listener.OnScrollTopListener;
import com.ovadyah.echome.port.AsyncGetPort;

/**
 * @Created Ovadyah
 * @Describe: 拦截NestedScrollView
 * @GitHub: https://github.com/SiberiaDante
 */
public class NestedScrollLayout extends NestedScrollView implements OnScrollTopListener {

    private static final String TAG = "NestedScrollLayout";
    private boolean isNeedScroll = true;
    private float xDistance, yDistance, xLast, yLast;
    private int scaledTouchSlop;
    /**
     * 默认是在顶部状态
     */
    private  boolean isScrollTop = true;
    public NestedScrollLayout(Context context) {
        super(context, null);
        initLayout();
    }

    public NestedScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initLayout();
    }

    public NestedScrollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout();
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    private void initLayout() {
        AsyncGetPort.getInstance().setOnScrollTopListener(this);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                int orientation = getOrientationValue(curX - xLast, curY - yLast);
                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;
                if ( 'b' == orientation && isScrollTop){
                    getParent().requestDisallowInterceptTouchEvent(true);
                    return true;
                } else {
                    return !(xDistance >= yDistance || yDistance < scaledTouchSlop) && isNeedScroll;
                }


        }
        return super.onInterceptTouchEvent(ev);
    }


    /*
    该方法用来处理NestedScrollView是否拦截滑动事件
     */
    public void setNeedScroll(boolean isNeedScroll) {
        this.isNeedScroll = isNeedScroll;
    }

    private int getOrientationValue(float dx, float dy) {
        if (Math.abs(dx)>Math.abs(dy)){
            //X轴移动
            return dx>0?'r':'l';//右,左
        }else{
            //Y轴移动
            return dy>0?'b':'t';//下//上
        }
    }

    @Override
    public void onScrollTop(boolean scrollTop) {
        this.isScrollTop = scrollTop;
    }
}
