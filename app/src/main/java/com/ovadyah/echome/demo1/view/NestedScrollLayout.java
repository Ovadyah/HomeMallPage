package com.ovadyah.echome.demo1.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.ovadyah.echome.demo1.listener.OnScrollTopListener;
import com.ovadyah.echome.demo1.port.AsyncGetPort;

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

    /**
     * 解决滚动过程中刷新完毕导致触摸事件丢失的问题
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            //解决子控件和父控件的滑动影响
            if (isScrollTop && getScrollVertically(0)){
                getParent().requestDisallowInterceptTouchEvent(false);
            } else {
                //当滑动recyclerView时，告知父控件不要拦截事件，交给子view处理
                getParent().requestDisallowInterceptTouchEvent(true);
            }
            return super.dispatchTouchEvent(ev);
        } catch (Exception e){
            return false;
        }
    }


    /**
     * 获取滚动方向
     * @param vertically
     * Value：0 顶部，1底部
     * @return
     */
    private boolean getScrollVertically(int vertically){
        boolean isScrollVertically = false;
        if (vertically == 0){
            if(canScrollVertically(-1)){
                isScrollVertically = false;
            }else {
                //滑动到顶部
                isScrollVertically = true;
            }
        } else if (vertically == 1){
            if(canScrollVertically(1)){
                isScrollVertically = false;
            }else {
                //滑动到底部
                isScrollVertically = true;
            }
        }
        return isScrollVertically;
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
