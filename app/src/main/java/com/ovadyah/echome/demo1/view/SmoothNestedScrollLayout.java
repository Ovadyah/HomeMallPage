package com.ovadyah.echome.demo1.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent2;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.ovadyah.echome.R;
import com.ovadyah.echome.demo1.port.AsyncGetPort;

import java.util.ArrayList;

public class SmoothNestedScrollLayout extends LinearLayout implements NestedScrollingParent2 {
    private static final String TAG = "NestedScrollLayout";
    private float mFirstY;
    private NestedScrollingParentHelper mParentHelper;
    private int mTopViewId;
    private int mInnerOffsetId;
    private int mContentViewId;

    private final ArrayList<View> mInnerOffsetView = new ArrayList<>(1);

    private final ArrayList<View> mTopStableView = new ArrayList<>(1);

    private View mContentView;

    private int mTopScrollHeight;

    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;
    private int mMaximumVelocity, mMinimumVelocity;

    private float mLastY;
    private boolean mDragging;
    private View mTarget;
    private int mScrollType = ViewCompat.TYPE_TOUCH;
    private View mInsetTargetScrollView;
    private int mTargetLastY;
    private OnScrollControlDelegate mScrollControlDelegate;
    private onScrollListener mScrollListener;
    private boolean mInflated;
    private int mOuterCoverTopHeight;
    private int mOuterCoverBottomHeight;
    private int mInnerOffsetHeight;
    /**
     * 是否滚动到顶部
     * 默认为true
     */
    private boolean isScrollTop = true;

    public SmoothNestedScrollLayout(Context context) {
        super(context);
        init(context, null);
    }

    public SmoothNestedScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SmoothNestedScrollLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        setOrientation(LinearLayout.VERTICAL);

        mScroller = new OverScroller(context);
        ViewConfiguration config = ViewConfiguration.get(context);
        mTouchSlop = config.getScaledTouchSlop();
        mMaximumVelocity = config.getScaledMaximumFlingVelocity();
        mMinimumVelocity = config.getScaledMinimumFlingVelocity();
        mParentHelper = new NestedScrollingParentHelper(this);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SmoothNestedScrollLayout);
            mTopViewId = a.getResourceId(R.styleable.SmoothNestedScrollLayout_nest_scroll_top_view, 0);
            mInnerOffsetId = a.getResourceId(R.styleable.SmoothNestedScrollLayout_nest_scroll_inner_header, 0);
            mContentViewId = a.getResourceId(R.styleable.SmoothNestedScrollLayout_nest_scroll_content, 0);
            a.recycle();
        }
    }

    @Override
    public int getNestedScrollAxes() {
        Log.e(TAG, "getNestedScrollAxes");
        return mParentHelper.getNestedScrollAxes();
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return onStartNestedScroll(child, target, nestedScrollAxes, ViewCompat.TYPE_TOUCH);
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        Log.d(TAG, "onNestedScrollAccepted");
        mParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View target) {
        Log.d(TAG, "onStopNestedScroll");
        mTarget = null;
        mParentHelper.onStopNestedScroll(target);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.d(TAG, "onNestedScroll");
    }

    // NestedScrollingParent2 interfaces
    @Override
    public boolean onStartNestedScroll(@NonNull View view, @NonNull View target, int axes, int type) {
        Log.d(TAG, "Start Nested Scroll " + type);
        mTarget = target;
        mScrollType = type;
        clearSelfFling();
        return true;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int nestedScrollAxes, int type) {
        Log.d(TAG, "accept Start Nested Scroll " + type);
        mTarget = target;
        mScrollType = type;
        clearSelfFling();
        mParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes, type);
    }

    @Override
    public void onStopNestedScroll(@NonNull View view, int type) {
        Log.d(TAG, "onStopNestedScroll " + type);
        if (mScrollType == type) {
            mTarget = null;
        }
        mParentHelper.onStopNestedScroll(view, type);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed, int type) {
        if (type == ViewCompat.TYPE_TOUCH) {
            Log.d(TAG, "onNestedScroll ignore touch " + type);
            return;
        }
        if (dyUnconsumed == 0) {
            Log.d(TAG, "onNestedScroll ignore dy = 0");
            return;
        }
        int consumedY = consumeY(target, dyUnconsumed);
        if (consumedY != 0) {
            Log.d(TAG, "onNestedScroll type " + type + " consumed " + consumedY);
            scrollBy(0, consumedY);
        } else {
            // 这步很必要，这里表示子view和父view都不消耗这个fling了，必须让子view停掉冗余的fling。否者这一次
            // fling会自然慢慢结束，这个过程又没有真实的滚动。这期间如果再次触发一次别的scroll，就会导致同时有
            // 两个scroll，引起滑动冲突。同理在这期间，我们在on touch里也做了这样的检测判断，确保不会发生冲突。
            // 理论上，在onTouchEvent里做同样的处理已经足够避免冲突，但这里仍保留以结束掉fling避免浪费CPU。
            Log.d(TAG, "onNestedScroll stop it type " + type);
            ViewCompat.stopNestedScroll(target, type);
        }
    }

    // 第一代方法
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        onNestedPreScroll(target, dx, dy, consumed, ViewCompat.TYPE_TOUCH);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed, int type) {

        int consumedY = consumeY(target, dy);
        if (consumedY != 0) {
            Log.d(TAG, "onNestedPreScroll type " + type + " consumed " + consumedY);
            scrollBy(0, consumedY);
            consumed[1] = consumedY;
            return;
        }
        Log.d(TAG, "onNestedPreScroll unconsumed type " + type);

    }

    protected int consumeY(View target, int dy) {
        // dy 代表滑动的距离，是正是负代表滑动方向。
        // 读代码 RecyclerView和ScrollView发现，dy = mLastY - currentY，是用前一次减这一次活动的deltaY
        // 也就是手指从上往下滑，currentY 肯定大于 mLastY，所以 dy < 0 是从上往下滑动。
        if (dy > 0) {
            // 手指从下往上滑动，如果top还没有完全隐藏，则必须消耗dy以隐藏top，执行 scroll hide top动作
            // getScrollY()会返回控件坐标向上偏移量，正数表示向上偏移，负数表示向下偏移
            int sY = getScrollY();
            if (sY < getTopScrollHeight()) {
                return Math.min(getTopScrollHeight() - sY, dy);
            }
        } else if (dy < 0) {
            // 从上往下滑，scroll Y >=0 标识 Y 向下已有偏移，计算list是否到顶
            int sY = getScrollY();
            if (sY > 0 && !target.canScrollVertically(-1)) {
                return Math.max(dy, -sY);//这里的值都是负数，取绝对值小的，所以要用max
            }
        }
        return 0;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        // 第二步，处理fling的嵌套滑动
//        if (velocityY > 0) {// 手指从下向上滑动
//            if (getScrollY() < mTopScrollHeight) {
//                // 当前View还没滑到顶
//                fling((int) velocityY);
//                return true;
//            }
//        } else if (velocityY < 0) {// 手指从上向下滑动, 内容是scroll up。
//            // 1，getScrollY > 0 表示top header有向上滑出去
//            // 2，判断子view是否可以scroll up，如果不scroll up，则可以让parent scroll up
//            if (getScrollY() > 0 && !ViewCompat.canScrollVertically(target, -1)){
//                fling((int) velocityY);
//                return true;
//            }
//        }
//        return false;
        /* 第三步，处理fling更牛逼更顺畅的办法，使用 NestedScrollingParent2 在新的onNestedPreScroll处理
         * 所以以上第二步注释掉了
         */

        return super.onNestedPreFling(target, velocityX, velocityY);
    }

    public SmoothNestedScrollLayout setContentView(View view) {
        this.mContentView = view;
        return this;
    }

    public SmoothNestedScrollLayout setContentViewId(int id) {
        this.mContentViewId = id;
        if (id > 0 && mInflated) {
            mContentView = findViewById(id);
        }
        return this;
    }

    public SmoothNestedScrollLayout setTopView(View topView) {
        if (topView != null) {
            mTopStableView.add(topView);
        } else {
            mTopStableView.clear();
        }
        return this;
    }

    public SmoothNestedScrollLayout setTopViewId(int topId) {
        mTopViewId = topId;
        if (topId > 0 && mInflated) {
            View v = findViewById(topId);
            if (v != null) {
                mTopStableView.add(v);
            }
        }
        return this;
    }

    public SmoothNestedScrollLayout setInnerOffsetView(View view) {
        if (!mInnerOffsetView.contains(view)) {
            mInnerOffsetView.add(view);
        }
        return this;
    }

    private int calculateInnerOffsetHeight() {
        int h = 0;
        for (View v : mInnerOffsetView) {
            h += v.getMeasuredHeight();
        }
        mInnerOffsetHeight = h;
        return h;
    }

    private int getInnerOffsetHeight() {
        return mInnerOffsetHeight;
    }

    public SmoothNestedScrollLayout setOuterCoverTopMargin(int height) {
        if (mOuterCoverTopHeight != height) {
            mOuterCoverTopHeight = height;
            requestLayout();
        }
        return this;
    }

    public SmoothNestedScrollLayout setOuterCoverBottomMargin(int height) {
        if (mOuterCoverBottomHeight != height) {
            mOuterCoverBottomHeight = height;
            requestLayout();
        }
        return this;
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        float x= ev.getX();
        float y = ev.getY();

        switch (action) {
            case MotionEvent.ACTION_MOVE: {
                if (mDragging) {
                    return true;
                }

                float curryY = ev.getY();
                if (curryY > mTopScrollHeight + mInnerOffsetHeight - getScrollY()) {
                    return false;
                }
                final float dy = Math.abs(mLastY - ev.getY());
                if (dy > mTouchSlop ) {
                    // 只有手指滑动距离大于阈值时，才会开始拦截
                    // Start scrolling!
                    getParent().requestDisallowInterceptTouchEvent(true);
                    return true;
                }
                break;
            }
            case MotionEvent.ACTION_DOWN:
                clearAllFling();
                mLastY = ev.getY();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mDragging = false;
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTrackerIfNotExists();
        mVelocityTracker.addMovement(event);
        int action = event.getAction();
        float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                clearAllFling();
                mLastY = y;
                mFirstY = y;
                return true;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastY;
                if (!mDragging && Math.abs(mLastY - y) >  ViewConfiguration.getTouchSlop()) {
                    mDragging = true;
                }

                if (mDragging) {
                    scrollBy(0, (int) -dy);
                }

                mLastY = y;
                break;
            case MotionEvent.ACTION_CANCEL:
                mDragging = false;
                recycleVelocityTracker();
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_UP:
                mDragging = false;
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int velocityY = (int) mVelocityTracker.getYVelocity();
                if (Math.abs(velocityY) > mMinimumVelocity) {
                    fling(-velocityY);
                }
                recycleVelocityTracker();
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure height:" + MeasureSpec.getSize(heightMeasureSpec));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mTopStableView.size() > 0) {
            mTopScrollHeight = 0;
            for (View stableView : mTopStableView) {
                int measureMode = (stableView.getMeasuredHeightAndState() & MEASURED_STATE_MASK);
                if (measureMode != MeasureSpec.EXACTLY && measureMode != MeasureSpec.UNSPECIFIED) {
                    // measureMode 可能是 MeasureSpec.AT_MOST 或 View.MEASURED_STATE_TOO_SMALL
                    //不限制顶部的高度，这样是考虑到顶部的view自身是不可滚动，所以必须有多大都要设置给size。
                    stableView.measure(widthMeasureSpec,
                            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                }
                mTopScrollHeight += stableView.getMeasuredHeight();
            }
            if (mContentView != null) {
                int innerOffsetHeight = calculateInnerOffsetHeight();
                int contentMH = mContentView.getMeasuredHeight();
                Log.d(TAG, "onMeasure content height:" + contentMH +
                        " top:" + mTopScrollHeight +
                        " total:" + getMeasuredHeight() + " inner:" + innerOffsetHeight +
                        " outer:" + mOuterCoverTopHeight);
                // 修改params.height的方式
//                ViewGroup.LayoutParams params = mContentView.getLayoutParams();
//                params.height = getMeasuredHeight() - innerOffsetHeight;
                //设置params后，需重新调用super.onMeasure();
                int contentH = getMeasuredHeight() - innerOffsetHeight - mOuterCoverTopHeight;
                if (contentH > mOuterCoverBottomHeight) {
                    contentH -= mOuterCoverBottomHeight;
                }
                mContentView.measure(widthMeasureSpec,
                        MeasureSpec.makeMeasureSpec(contentH, MeasureSpec.AT_MOST));
                contentMH = mContentView.getMeasuredHeight();
                Log.d(TAG, "onMeasure content adjusted height:" + contentMH);
            }
            setMeasuredDimension(getMeasuredWidth(),
                    mTopScrollHeight + getMeasuredHeight());
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mInflated = true;
        if (mTopViewId > 0) {
            setTopViewId(mTopViewId);
        }
        if (mInnerOffsetId > 0) {
            mInnerOffsetView.add(findViewById(mInnerOffsetId));
        }
        if (mContentViewId > 0) {
            mContentView = findViewById(mContentViewId);
        }
    }

    public int getTopScrollHeight() {
        return mTopScrollHeight - mOuterCoverTopHeight;
    }

    public void fling(int velocityY) {
        mTargetLastY = 0;
        // 第三步-二小步，fling调整最大Y偏移，以便把fling传递给nestedScrollingChild。
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, Integer.MAX_VALUE);
        invalidate();
    }

    public void setScrollChild(View view) {
        this.mInsetTargetScrollView = view;
    }

    private View findChildScrollView(int dy) {
        View delegateView = mContentView;
        if (delegateView != null && delegateView.canScrollVertically(dy)) {
            return delegateView;
        }
        delegateView = mInsetTargetScrollView;
        if (delegateView != null
                && delegateView.canScrollVertically(dy)) {
            return delegateView;
        }
        if (mScrollControlDelegate != null) {
            delegateView = mScrollControlDelegate.getScrollChildView();
            if (delegateView != null && delegateView.canScrollVertically(dy)) {
                return delegateView;
            }
        }
        return null;
    }

    protected void clearSelfFling() {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
        mTargetLastY = 0;
    }

    protected void clearAllFling() {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
        mTargetLastY = 0;
        if (mTarget != null) {
            Log.d(TAG, "force stop nest scroll from child");
            ViewCompat.stopNestedScroll(mTarget, mScrollType);
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        int topScrollHeight = getTopScrollHeight();
        if (y > topScrollHeight) {
            super.scrollTo(x, topScrollHeight);
            y -= topScrollHeight;
            int dy = y - mTargetLastY;
            View scrollView = findChildScrollView(dy);
            if (scrollView != null) {
                scrollView.scrollBy(x, dy);
                mTargetLastY += dy;
                Log.d(TAG, "scrollTo transfer " + y);
            } else {
                Log.d(TAG, "scrollTo but container view can not scroll");
                clearSelfFling();
            }
            return;
        }
        if (y < 0) {
            y = 0;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            Log.d(TAG, "computeScroll to " + mScroller.getCurrY());
            scrollTo(0, mScroller.getCurrY());
            invalidate();
            return;
        }
        Log.d(TAG, "computeScroll end");

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (isScrollTop != (t == 0)){
            isScrollTop = t == 0;
            AsyncGetPort.getInstance().triggerOnScrollTopListener(t == 0);
        }
        if (mScrollListener != null) {
            mScrollListener.onNestScrolling(t - oldt, t);
        }
    }

    public SmoothNestedScrollLayout setControlDelegate(OnScrollControlDelegate c) {
        mScrollControlDelegate = c;
        return this;
    }

    public SmoothNestedScrollLayout setScrollListener(onScrollListener l) {
        mScrollListener = l;
        return this;
    }


    public interface OnScrollControlDelegate {
        View getScrollChildView();
    }

    public interface onScrollListener {
        void onNestScrolling(int dy, int scrollY);
    }
}
