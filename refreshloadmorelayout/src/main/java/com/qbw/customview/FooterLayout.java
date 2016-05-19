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
    private View mVFooter;
    private int mFooterHeight;

    private View mVFooterContent;

    private TextView mTitleTxt;
    private ProgressBar mProgressBar;

    private int mStatus = -1;

    /**
     * normal，footer height
     */
    private int mNormalStatusHeight = 0;

    /**
     * time when change 'DURATION_PER_10_PIXEL'
     */
    private final int DURATION_PER_10_PIXEL = 15;

    /**
     * animator about header's height chnange
     */
    private ValueAnimator mHeightAnim;

    /**
     * mStatus callback
     */
    private RefreshLoadMoreLayout.CallBack mCallBack;

    /**
     * 没有更多数据了
     */
    private boolean mNoMoreData = false;

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
                mFooterHeight = getHeight();
            }
        });
        View view = LayoutInflater.from(context).inflate(R.layout.rll_footer_view, this, false);
        addView(view);
        mVFooter = view.findViewById(R.id.footer_fl_root);
        mVFooterContent = view.findViewById(R.id.footer_ll_content);
        mTitleTxt = (TextView) view.findViewById(R.id.footer_tv_title);
        mProgressBar = (ProgressBar) view.findViewById(R.id.footer_pb);
        setStatus(Status.NORMAL);
    }

    public void setStatus(int status) {
        if (this.mStatus == status) {
            if (Status.LOAD == this.mStatus) {//自动加载滑动到最底部的时候，高度要还原至'load'状态的高度
                onLoadStatusNoCallBack();
            }
            return;
        }

        int oldStatus = this.mStatus;
        this.mStatus = status;

        switch (this.mStatus) {
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
            //below will reset mStatus value
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
        this.mNoMoreData = noMoreData;
    }

    private void onBackLoadStatus() {
        if (caseNoMoreData()) {
            setHeightAnim(0);
            return;
        }
        setHeightAnim(getFooterContentHeight());
    }


    private void onBackNormalStatus() {
        if (caseNoMoreData()) {
            setHeightAnim(0);
            return;
        }
        mTitleTxt.setText(R.string.rll_footer_hint_normal);
        mProgressBar.setVisibility(View.GONE);
        setHeightAnim(getNormalStatusHeight());
    }

    private void onLoadStatus() {
        if (caseNoMoreData()) {
            setHeightAnim(0);
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
        mTitleTxt.setText(R.string.rll_footer_hint_loading);
        mProgressBar.setVisibility(View.VISIBLE);
        setHeightAnim(getFooterContentHeight());
    }

    private void onCanReleaseStatus() {
        if (caseNoMoreData()) {
            return;
        }
        mTitleTxt.setText(R.string.rll_footer_hint_ready);
        mProgressBar.setVisibility(View.GONE);
    }


    private void onPullUpStatus() {
        if (caseNoMoreData()) {
            return;
        }
        mTitleTxt.setText(R.string.rll_footer_hint_normal);
        mProgressBar.setVisibility(View.GONE);
    }


    private void onNormalStatus() {
        onPullUpStatus();
    }

    private boolean caseNoMoreData() {
        if (mNoMoreData) {
            mTitleTxt.setText(R.string.rll_footer_no_more_data);
            mProgressBar.setVisibility(View.GONE);
        }
        return mNoMoreData;
    }

    /**
     * @param toHeight
     */
    private void setHeightAnim(final int toHeight) {
        if (null != mHeightAnim && mHeightAnim.isRunning()) {
            mHeightAnim.cancel();
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
            mHeightAnim = ValueAnimator.ofInt(getFooterHeight(), toHeight);
            mHeightAnim.setDuration(duration);
            mHeightAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            mHeightAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
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
            mHeightAnim.start();
        }
    }

    public int getFooterHeight() {
        return mFooterHeight;
    }

    public int getFooterContentHeight() {
        return mVFooterContent.getMeasuredHeight();
    }

    public void setFooterHeight(int height) {
        if (height < 0) {
            height = 0;
        }
        mFooterHeight = height;
        LayoutParams params = (LayoutParams) mVFooter.getLayoutParams();
        params.height = mFooterHeight;
        mVFooter.setLayoutParams(params);
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatusValue(int status) {
        this.mStatus = status;
    }

    public RefreshLoadMoreLayout.CallBack getCallBack() {
        return mCallBack;
    }

    public void setCallBack(RefreshLoadMoreLayout.CallBack callBack) {
        this.mCallBack = callBack;
    }

    public int getNormalStatusHeight() {
        return mNormalStatusHeight;
    }

    public void setNormalStatusHeight(int normalStatusHeight) {
        this.mNormalStatusHeight = normalStatusHeight;
    }

    public static class Status {

        public static final int NORMAL = 0;

        public static final int PULL_UP = 1;

        public static final int CAN_RELEASE = 2;

        public static final int LOAD = 3;

        public static final int BACK_LOAD = 4;

        public static final int BACK_NORMAL = 5;
    }

    public boolean isLoadingMore() {
        return mStatus == Status.LOAD || mStatus == Status.BACK_LOAD;
    }
}
