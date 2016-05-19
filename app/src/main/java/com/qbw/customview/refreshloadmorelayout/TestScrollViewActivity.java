package com.qbw.customview.refreshloadmorelayout;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.qbw.customview.RefreshLoadMoreLayout;
import com.qbw.log.XLog;


public class TestScrollViewActivity extends Activity implements RefreshLoadMoreLayout.CallBack {
    protected TextView mText;
    protected RefreshLoadMoreLayout mRefreshloadmore;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scrollview);
        initView();
        mRefreshloadmore.startAutoRefresh();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onRefresh() {
        XLog.v("onRefresh");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //mRefreshloadmore.stopRefresh();
                mRefreshloadmore.stopRefresh(false);

                XLog.v("onRefresh finish");
            }
        }, 3000);
    }

    @Override
    public void onLoadMore() {
        XLog.v("onLoadMore");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshloadmore.stopLoadMore();
                XLog.v("onLoadMore finish");
            }
        }, 2000);
    }

    private void initView() {
        mText = (TextView) findViewById(R.id.text);
        mRefreshloadmore = (RefreshLoadMoreLayout) findViewById(R.id.refreshloadmore);
        mRefreshloadmore.init(new RefreshLoadMoreLayout.Config(this));
    }
}
