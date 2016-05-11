package com.qbw.customview.refreshloadmorelayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import com.ui.View.TitleLayout;

/**
 * @author  qinbaowei
 * @createtime 2015/10/14 17:14
 */

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
