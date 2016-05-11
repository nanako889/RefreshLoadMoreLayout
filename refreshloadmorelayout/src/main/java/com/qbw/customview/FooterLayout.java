package com.qbw.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author qinbaowei
 * @createtime 2015/10/13 13:04
 */

class FooterLayout extends FrameLayout {
    private View vFooter;
    private int footerHeight;

    private View vFooterContent;

    private TextView tvTitle;
    private ProgressBar progressBar;

    private int status = -1;

    /**
     * normal，footer height
     */
    private int normalStatusHeight = 0;

    /**
     * normal，text default text
     */
    private int resHintNormalText = R.string.rll_footer_hint_normal;

    /**
     * time when change 'DURATION_PER_10_PIXEL'
     */
    private final int DURATION_PER_10_PIXEL = 15;

    /**
     * animator about header's height chnange
     */
    private ValueAnimator animFooterHeight;

    /**
     * status callback
     */
    private RefreshLoadMoreLayout.CallBack callBack;

    /**
     * 没有更多数据了
     */
    private boolean noMoreData = false;

    public FooterLayout(Context context) {
        super(context);
        initView(context);
    }

    public FooterLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                footerHeight = getHeight();
            }
        });
        View view = LayoutInflater.from(context).inflate(R.layout.rll_footer_view, this, false);
        addView(view);
        vFooter = view.findViewById(R.id.footer_fl_root);
        vFooterContent = view.findViewById(R.id.footer_ll_content);
        tvTitle = (TextView) view.findViewById(R.id.footer_tv_title);
        progressBar = (ProgressBar) view.findViewById(R.id.footer_pb);
        setStatus(Status.NORMAL);
    }

    public void setStatus(int status) {
        if (this.status == status) {
            if (Status.LOAD == this.status) {//自动加载滑动到最底部的时候，高度要还原至'load'状态的高度
                onLoadStatusNoCallBack();
            }
            return;
        }

        int oldStatus = this.status;
        this.status = status;

        switch (this.status) {
            case Status.NORMAL:
                onNormalStatus();
                break;
            case Status.PULL_UP:
                onPullUpStatus();
                break;
            case Status.CAN_RELEASE:
                onCanReleaseStatus();
                break;
            case Status.LOAD:
                onLoadStatus();
                break;
            //below will reset status value
            case Status.BACK_NORMAL:
                onBackNormalStatus();
                break;
            case Status.BACK_LOAD:
                onBackLoadStatus();
                break;
            default:
                break;
        }
    }

    public void setNoMoreData(boolean noMoreData) {
        this.noMoreData = noMoreData;
    }

    private void onBackLoadStatus() {
        if (caseNoMoreData()) {
            setAnimFooterHeight(0);
            return;
        }
        setAnimFooterHeight(getFooterContentHeight());
    }


    private void onBackNormalStatus() {
        if (caseNoMoreData()) {
            setAnimFooterHeight(0);
            return;
        }
        tvTitle.setText(getResHintNormalText());
        progressBar.setVisibility(View.GONE);
        setAnimFooterHeight(getNormalStatusHeight());
    }

    private void onLoadStatus() {
        if (caseNoMoreData()) {
            setAnimFooterHeight(0);
            return;
        }
        onLoadStatusNoCallBack();
        if (null != getCallBack()) {
            getCallBack().onLoadMore();
        }
    }

    /**
     * 只能被‘onLoadStatus’调用
     */
    private void onLoadStatusNoCallBack() {
        tvTitle.setText(R.string.rll_footer_hint_loading);
        progressBar.setVisibility(View.VISIBLE);
        setAnimFooterHeight(getFooterContentHeight());
    }

    private void onCanReleaseStatus() {
        if (caseNoMoreData()) {
            return;
        }
        tvTitle.setText(R.string.rll_footer_hint_ready);
        progressBar.setVisibility(View.GONE);
    }


    private void onPullUpStatus() {
        if (caseNoMoreData()) {
            return;
        }
        tvTitle.setText(getResHintNormalText());
        progressBar.setVisibility(View.GONE);
    }


    private void onNormalStatus() {
        onPullUpStatus();
    }

    private boolean caseNoMoreData() {
        if (noMoreData) {
            tvTitle.setText(R.string.rll_footer_no_more_data);
            progressBar.setVisibility(View.GONE);
        }
        return noMoreData;
    }

    /**
     * @param toHeight
     */
    private void setAnimFooterHeight(final int toHeight) {
        if (null != animFooterHeight && animFooterHeight.isRunning()) {
            animFooterHeight.cancel();
        }
        int duration = Math.abs(getFooterHeight() - toHeight) / 10 * DURATION_PER_10_PIXEL;
        if (0 == duration) {
            setFooterHeight(toHeight);//can't get real height immediately when set height
            if (0 == toHeight) {
                setStatusValue(Status.NORMAL);
            } else if (getFooterContentHeight() == toHeight) {
                if (Status.BACK_NORMAL == getStatus()) {
                    setStatusValue(Status.NORMAL);
                } else {
                    setStatusValue(Status.LOAD);
                }
            }
        } else {
            animFooterHeight = ValueAnimator.ofInt(getFooterHeight(), toHeight);
            animFooterHeight.setDuration(duration);
            animFooterHeight.setInterpolator(new AccelerateDecelerateInterpolator());
            animFooterHeight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    final Integer animValue = (Integer) valueAnimator.getAnimatedValue();
                    setFooterHeight(animValue);
                    if (animValue == toHeight) {//animator finished
                        if (0 == toHeight) {
                            setStatusValue(Status.NORMAL);
                        } else if (getFooterContentHeight() == toHeight) {
                            if (Status.BACK_NORMAL == getStatus()) {
                                setStatusValue(Status.NORMAL);
                            } else {
                                setStatusValue(Status.LOAD);
                            }
                        }
                    }
                }
            });
            animFooterHeight.start();
        }
    }

    public int getFooterHeight() {
        return footerHeight;
    }

    public int getFooterContentHeight() {
        return vFooterContent.getMeasuredHeight();
    }

    public void setFooterHeight(int height) {
        if (height < 0) {
            height = 0;
        }
        footerHeight = height;
        LayoutParams params = (LayoutParams) vFooter.getLayoutParams();
        params.height = footerHeight;
        vFooter.setLayoutParams(params);
    }

    public int getStatus() {
        return status;
    }

    public void setStatusValue(int status) {
        this.status = status;
    }

    public RefreshLoadMoreLayout.CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(RefreshLoadMoreLayout.CallBack callBack) {
        this.callBack = callBack;
    }

    public int getNormalStatusHeight() {
        return normalStatusHeight;
    }

    public void setNormalStatusHeight(int normalStatusHeight) {
        this.normalStatusHeight = normalStatusHeight;
    }

    public int getResHintNormalText() {
        return resHintNormalText;
    }

    public void setResHintNormalText(int resHintNormalText) {
        this.resHintNormalText = resHintNormalText;
    }

    public static class Status {

        public static final int NORMAL = 0;

        public static final int PULL_UP = 1;

        public static final int CAN_RELEASE = 2;

        public static final int LOAD = 3;

        public static final int BACK_LOAD = 4;

        public static final int BACK_NORMAL = 5;
    }
}
