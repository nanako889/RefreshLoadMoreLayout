package com.qbw.customview.rlm;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.qbw.log.XLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bond on 2017/03/08 17:44
 * you can contact me at qbaowei@qq.com
 */


public class RlmScrollView extends ScrollView {

    private List<OnScrollListener> mOnScrollListeners = new ArrayList<>();

    public RlmScrollView(Context context) {
        super(context);
    }

    public RlmScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RlmScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (XLog.isEnabled()) XLog.v("l=%d, t=%d, oldl=%d, oldt=%d", l, t, oldl, oldt);
        notifiOnScrollListener(l, t, oldl, oldt);
    }

    public boolean addOnScrollListener(OnScrollListener onScrollListener) {
        return mOnScrollListeners.add(onScrollListener);
    }

    public boolean removeOnScrollListener(OnScrollListener onScrollListener) {
        return mOnScrollListeners.remove(onScrollListener);
    }

    private void notifiOnScrollListener(int l, int t, int oldl, int oldt) {
        for (OnScrollListener scrollListener : mOnScrollListeners) {
            scrollListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    public interface OnScrollListener {
        void onScrollChanged(ScrollView scrollView, int l, int t, int oldl, int oldt);
    }
}
