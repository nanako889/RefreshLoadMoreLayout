package com.qbw.customview.refreshloadmorelayout;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.qbw.customview.RefreshLoadMoreLayout;


public class TestScrollViewActivity extends Activity implements RefreshLoadMoreLayout.CallBack {
    private RefreshLoadMoreLayout refreshLoadMoreLayout;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scrollview);
        refreshLoadMoreLayout = (RefreshLoadMoreLayout) findViewById(R.id.main_rlm);
        refreshLoadMoreLayout.init(new RefreshLoadMoreLayout.Config(this));
    }

    public void onButton1Click(View view) {
        Toast.makeText(this, "onButton1Click", Toast.LENGTH_SHORT).show();
    }

    public void onButton2Click(View view) {
        Toast.makeText(this, "onButton2Click", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("", "onStart");
        refreshLoadMoreLayout.startAutoRefresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onRefresh() {
        Toast.makeText(this, "onRefresh", Toast.LENGTH_SHORT).show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLoadMoreLayout.stopRefresh();
                refreshLoadMoreLayout.setIsCanRefresh(false);
                Toast.makeText(TestScrollViewActivity.this, "onRefresh finish", Toast.LENGTH_SHORT).show();
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
                refreshLoadMoreLayout.setIsCanLoadMore(false);
                Toast.makeText(TestScrollViewActivity.this, "onLoadMore finish", Toast.LENGTH_SHORT).show();
            }
        }, 2000);
    }
}
