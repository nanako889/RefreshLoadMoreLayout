package com.ui.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qbw.customview.refreshloadmorelayout.R;

/**
 * @author Bond.Qin
 * @createtime 2016/01/29 17:15
 * @description
 */


public class TitleLayout extends RelativeLayout {
    private TextView titleTxt;

    public TitleLayout(Context context) {
        super(context);
        init(context);
    }

    public TitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        setDefaultValue(context, attrs);
    }

    public TitleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        setDefaultValue(context, attrs);
    }

    private void setDefaultValue(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleLayout);
        titleTxt.setText(typedArray.getString(R.styleable.TitleLayout_titleText));
        titleTxt.setTextColor(typedArray.getColor(R.styleable.TitleLayout_titleColor, 00000000));
        typedArray.recycle();
    }

    private void init(Context context) {
        titleTxt = new TextView(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        addView(titleTxt, layoutParams);
    }

    public void setTitle(String title) {
        titleTxt.setText(title);
    }
}
