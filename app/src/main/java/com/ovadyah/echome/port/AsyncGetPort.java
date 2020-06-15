package com.ovadyah.echome.port;

import com.ovadyah.echome.listener.OnScrollTopListener;

public class AsyncGetPort {
    private volatile static AsyncGetPort instance;
    /**
     * Returns AsyncGetPort
     */
    public static AsyncGetPort getInstance() {
        if (instance == null) {
            synchronized (AsyncGetPort.class) {
                if (instance == null) {
                    instance = new AsyncGetPort();
                }
            }
        }
        return instance;
    }

    protected AsyncGetPort() {
    }

    /**
     * 设置监听改变首页的Tab
     */
    public OnScrollTopListener mOnScrollTopListener;
    public void setOnScrollTopListener(OnScrollTopListener scrollTopListener){
        this.mOnScrollTopListener = scrollTopListener;
    }
    public void triggerOnScrollTopListener(boolean scrollTop){
        if (mOnScrollTopListener != null){
            mOnScrollTopListener.onScrollTop(scrollTop);
        }
    }



    public void destroyPort(){
        if (mOnScrollTopListener != null){
            mOnScrollTopListener = null;
        }
    }
}
