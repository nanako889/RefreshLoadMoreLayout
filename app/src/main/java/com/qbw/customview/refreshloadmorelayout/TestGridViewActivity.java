package com.qbw.customview.refreshloadmorelayout;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.GridView;

import com.qbw.customview.RefreshLoadMoreLayout;


/**
 * Created by Bond on 2016/5/19.
 */
public class TestGridViewActivity extends Activity implements RefreshLoadMoreLayout.CallBack {
    protected GridView mGridview;
    protected RefreshLoadMoreLayout mRefreshloadmore;
    private TestListViewActivity.Adapter mAdapter;
    protected Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_test_gridview);
        initView();
    }

    private void initView() {
        mGridview = (GridView) findViewById(R.id.gridview);
        mGridview.setAdapter(mAdapter = new TestListViewActivity.Adapter(this));
        mRefreshloadmore = (RefreshLoadMoreLayout) findViewById(R.id.refreshloadmore);
        mRefreshloadmore.init(new RefreshLoadMoreLayout.Config(this));

    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.addItem(0, new String(SystemClock.uptimeMillis() + "refresh"));
                mRefreshloadmore.stopRefresh();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.addItem(new String(SystemClock.uptimeMillis() + "load"));
                mRefreshloadmore.stopLoadMore();
            }
        }, 1000);
    }


}
