package com.qbw.customview.refreshloadmorelayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.qbw.log.XLog;

/**
 * @author  qinbaowei
 * @createtime 2015/10/14 17:14
 */

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XLog.setEnabled(true);
    }

    public void testViewClick(View view) {
        startActivity(new Intent(this, TestViewActivity.class));
    }

    public void testScrollViewClick(View view) {
        startActivity(new Intent(this, TestScrollViewActivity.class));
    }

    public void testRecyclerViewClick(View view) {
        startActivity(new Intent(this, TestRecyclerViewActivity.class));
    }

    public void testListViewClick(View view) {
        startActivity(new Intent(this, TestListViewActivity.class));
    }

    public void testGridViewClick(View view) {
        startActivity(new Intent(this, TestGridViewActivity.class));
    }
}
