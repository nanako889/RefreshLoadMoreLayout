package com.qbw.customview.refreshloadmorelayout;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.qbw.customview.RefreshLoadMoreLayout;
import com.qbw.log.XLog;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author qinbaowei
 * @createtime 2015/10/14 17:16
 */

public class TestViewActivity extends Activity implements RefreshLoadMoreLayout.CallBack {

    protected TextView mText;
    protected RefreshLoadMoreLayout mRefreshloadmore;
    private Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_view);
        initView();
    }

    @Override
    public void onRefresh() {
        XLog.v("onRefresh");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S");
        mText.setText(simpleDateFormat.format(new Date()));
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshloadmore.stopRefresh();
                XLog.v("onRefresh finish");
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        //view一般情况下只是需要刷新，不需要加载更多
    }

    private void initView() {
        mText = (TextView) findViewById(R.id.text);
        mRefreshloadmore = (RefreshLoadMoreLayout) findViewById(R.id.refreshloadmore);
        mRefreshloadmore.init(new RefreshLoadMoreLayout.Config(this).canLoadMore(false));
    }
}
