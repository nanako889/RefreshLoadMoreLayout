package com.qbw.customview.refreshloadmorelayout;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.qbw.customview.RefreshLoadMoreLayout;

/**
 * @author  qinbaowei
 * @createtime 2015/10/14 17:16
 */

public class TestViewActivity extends Activity implements RefreshLoadMoreLayout.CallBack {
    private RefreshLoadMoreLayout refreshLoadMoreLayout;

    private Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_view);


        refreshLoadMoreLayout = (RefreshLoadMoreLayout) findViewById(R.id.main_rlm);
        refreshLoadMoreLayout.init(new RefreshLoadMoreLayout.Config(this));

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TestViewActivity.this, "CLICK BUTTON", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        Toast.makeText(this, "onRefresh", Toast.LENGTH_SHORT).show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLoadMoreLayout.stopRefresh();
                Toast.makeText(TestViewActivity.this, "onRefresh finish", Toast.LENGTH_SHORT).show();
            }
        }, 3000);
    }

    @Override
    public void onLoadMore() {
        Toast.makeText(this, "onLoadMore", Toast.LENGTH_SHORT).show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLoadMoreLayout.stopLoadMore();
                Toast.makeText(TestViewActivity.this, "onLoadMore finish", Toast.LENGTH_SHORT).show();
            }
        }, 2000);
    }
}
