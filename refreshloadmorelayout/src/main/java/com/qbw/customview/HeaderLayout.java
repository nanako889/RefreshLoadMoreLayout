package com.qbw.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qbw.log.XLog;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author  qinbaowei
 * @createtime 2015/10/10 09:52
 */
class HeaderLayout extends FrameLayout {
    private View mVHeader;
    private View mVHeaderContent;
    private ImageView mArrowImg;
    private ProgressBar mProgressBar;
    private TextView mTitleTxt;
    private TextView mTipTimeTxt;
    private TextView mTimeTxt;

    private int mStatus = -1;

    private String mDateFormat = "";

    /**
     * this value must be set
     */
    private String mKeyLastUpdateTime = "";

    private boolean mShowLastRefreshTime = false;

    /**
     * the time of rll_arrow rotate
     */
    private final int ARROW_ROTATION_DURATION = 200;


    private ValueAnimator mArrowRotationAnim;


    private final int DURATION_PER_10_PIXEL = 15;

    private ValueAnimator mHeightAnim;

    private int mHeaderHeight;

    private RefreshLoadMoreLayout.CallBack mCallBack;

    public HeaderLayout(Context context) {
        super(context);
        initView(context);
    }

    public HeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.rll_header_view, this, false);
        addView(view);
        mVHeader = view.findViewById(R.id.header_ll_root);
        mVHeaderContent = view.findViewById(R.id.header_rl_content);
        mArrowImg = (ImageView) view.findViewById(R.id.header_iv_arrow);
        mProgressBar = (ProgressBar) view.findViewById(R.id.header_pb_arrow);
        mTitleTxt = (TextView) view.findViewById(R.id.header_tv_title);
        mTipTimeTxt = (TextView) view.findViewById(R.id.header_tv_tip_time);
        mTimeTxt = (TextView) view.findViewById(R.id.header_tv_time);
        setStatus(Status.NORMAL);
    }

    public int getHeaderHeight() {
        return mHeaderHeight;
    }

    public void setHeaderHeight(int height) {
        if (height < 0) {
            height = 0;
        }
        mHeaderHeight = height;
        LayoutParams params = (LayoutParams) mVHeader.getLayoutParams();
        params.height = mHeaderHeight;
        mVHeader.setLayoutParams(params);
    }

    private void setHeightAnim(final int toHeight) {
        if (null != mHeightAnim && mHeightAnim.isRunning()) {
            mHeightAnim.cancel();
        }
        int duration = Math.abs(getHeaderHeight() - toHeight) / 10 * DURATION_PER_10_PIXEL;
        if (0 == duration) {
            setHeaderHeight(toHeight);//can't get real height immediately when set height
            if (0 == toHeight) {
                setStatusValue(Status.NORMAL);
            } else if (getHeaderContentHeight() == toHeight) {
                setStatusValue(Status.REFRESH);
            }
        } else {
            mHeightAnim = ValueAnimator.ofInt(getHeaderHeight(), toHeight);
            mHeightAnim.setDuration(duration);
            mHeightAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            mHeightAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    final Integer animValue = (Integer) valueAnimator.getAnimatedValue();
                    setHeaderHeight(animValue);
                    if (animValue == toHeight) {//animator finised
                        if (0 == toHeight) {
                            setStatusValue(Status.NORMAL);
                        } else if (getHeaderContentHeight() == toHeight) {
                            setStatusValue(Status.REFRESH);
                        }
                    }
                }
            });
            mHeightAnim.start();
        }
    }

    public int getHeaderContentHeight() {
        return mVHeaderContent.getMeasuredHeight();
    }

    private void setStatusValue(int status) {
        this.mStatus = status;
    }

    public void setStatus(int status) {
        if (this.mStatus == status) {
            return;
        }

        int oldStatus = this.mStatus;
        this.mStatus = status;

        switch (this.mStatus) {
            case Status.NORMAL:
                onNormalStatus(oldStatus);
                break;
            case Status.PULL_DOWN:
                onPullDownStatus(oldStatus);
                break;
            case Status.CAN_RELEASE:
                onCanRefreshStatus();
                break;
            case Status.REFRESH:
                onRefreshStatus();
                break;
            //below will reset mStatus value
            case Status.BACK_NORMAL:
                onBackNormalStatus();
                break;
            case Status.BACK_REFRESH:
                onBackRefreshStatus();
                break;
            case Status.AUTO_REFRESH:
                onAutoRefreshStatus();
                break;
            default:
                break;
        }
    }


    private void onNormalStatus(int oldStatus) {
        onPullDownStatus(oldStatus);
    }


    private void onPullDownStatus(int oldStatus) {
        mProgressBar.setVisibility(View.GONE);
        mArrowImg.setVisibility(View.VISIBLE);
        mTitleTxt.setText(R.string.rll_header_hint_normal);
        String lastUpdateTime = getLastUpdateTime();
        if (TextUtils.isEmpty(lastUpdateTime)) {
            mTipTimeTxt.setVisibility(View.GONE);
            mTimeTxt.setVisibility(View.GONE);
        } else {
            mTipTimeTxt.setVisibility(View.VISIBLE);
            mTimeTxt.setVisibility(View.VISIBLE);
            mTimeTxt.setText(lastUpdateTime);
        }
        switch (oldStatus) {
            case Status.NORMAL:
                mArrowImg.setRotation(0);
                break;
            case Status.CAN_RELEASE:
                rotateArrow(-180, 0);
                break;
            default:
                break;
        }
    }

    private void onCanRefreshStatus() {
        mTitleTxt.setText(R.string.rll_header_hint_ready);
        rotateArrow(0, -180);
    }

    private void rotateArrow(float from, float to) {
        if (null != mArrowRotationAnim && mArrowRotationAnim.isRunning()) {
            mArrowRotationAnim.cancel();
        }
        mArrowImg.setRotation(from);
        mArrowRotationAnim = ValueAnimator.ofFloat(from, to);
        mArrowRotationAnim.setDuration(ARROW_ROTATION_DURATION);
        mArrowRotationAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float angel = (Float) animation.getAnimatedValue();
                mArrowImg.setRotation(angel);
            }
        });
        mArrowRotationAnim.start();
    }

    private void onRefreshStatus() {
        mTitleTxt.setText(R.string.rll_header_hint_loading);
        mArrowImg.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        saveLastUpdateTime(System.currentTimeMillis());
        setHeightAnim(getHeaderContentHeight());
        if (null != getCallBack()) {
            getCallBack().onRefresh();
        }
    }

    private void onBackNormalStatus() {
        setHeightAnim(0);
    }

    private void onBackRefreshStatus() {
        setHeightAnim(getHeaderContentHeight());
    }

    private void onAutoRefreshStatus() {
        String lastUpdateTime = getLastUpdateTime();
        if (TextUtils.isEmpty(lastUpdateTime)) {
            mTipTimeTxt.setVisibility(View.GONE);
            mTimeTxt.setVisibility(View.GONE);
        } else {
            mTipTimeTxt.setVisibility(View.VISIBLE);
            mTimeTxt.setVisibility(View.VISIBLE);
            mTimeTxt.setText(lastUpdateTime);
        }
        onRefreshStatus();
    }

    private final long MINUTE_PER = 60 * 1000;
    private final long HOUR_PER = 60 * MINUTE_PER;
    private final long DAY_PER = 24 * HOUR_PER;

    private String getLastUpdateTime() {
        long lastUpdateTime = loadLastUpdateTime();
        if (INVALID_LAST_UPDATE_TIME == lastUpdateTime || !isShowLastRefreshTime()) {
            return "";
        }
        Date lastUpdateDate = new Date(lastUpdateTime);
        long nowTime = System.currentTimeMillis();
        if (TextUtils.isEmpty(getDateFormat())) {
            long disTime = nowTime - lastUpdateTime;
            long days = disTime / DAY_PER;
            long hours = disTime / HOUR_PER;
            long minutes = disTime / MINUTE_PER;
            if (0 == days) {
                if (0 == hours) {
                    if (0 == minutes) {
                        return getContext().getString(R.string.rll_header_time_justnow);
                    } else {
                        return minutes + getContext().getString(R.string.rll_header_time_minutes);
                    }
                } else {
                    return hours + getContext().getString(R.string.rll_header_time_hours);
                }
            } else {
                return days + getContext().getString(R.string.rll_header_time_days);
            }
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getDateFormat());
            return simpleDateFormat.format(lastUpdateDate);
        }
    }

    private final String PREFERENCE_NAME = "RefreshLoadMoreLayout_HeaderLayout";
    private final int INVALID_LAST_UPDATE_TIME = -1;

    private void saveLastUpdateTime(long time) {
        SharedPreferences preferences = getContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        preferences.edit().putLong(getKeyLastUpdateTime(), time).commit();
    }

    private long loadLastUpdateTime() {
        SharedPreferences preferences = getContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return preferences.getLong(getKeyLastUpdateTime(), INVALID_LAST_UPDATE_TIME);
    }

    public int getStatus() {
        return mStatus;
    }

    public String getDateFormat() {
        return mDateFormat;
    }

    public void setDateFormat(String dateFormat) {
        if (!TextUtils.isEmpty(dateFormat)) {
            this.mDateFormat = dateFormat;
        }
        XLog.v(this.mDateFormat);
    }

    public boolean isShowLastRefreshTime() {
        return mShowLastRefreshTime;
    }

    public void setIsShowLastRefreshTime(boolean isShowLastRefreshTime) {
        this.mShowLastRefreshTime = isShowLastRefreshTime;
    }

    public String getKeyLastUpdateTime() {
        return mKeyLastUpdateTime;
    }

    public void setKeyLastUpdateTime(String keyLastUpdateTime) {
        XLog.v(mKeyLastUpdateTime);
        mKeyLastUpdateTime = keyLastUpdateTime;
    }

    public static class Status {

        public static final int NORMAL = 0;

        public static final int PULL_DOWN = 1;

        public static final int CAN_RELEASE = 2;

        public static final int REFRESH = 3;

        public static final int BACK_NORMAL = 4;

        public static final int BACK_REFRESH = 5;

        public static final int AUTO_REFRESH = 6;
    }

    public RefreshLoadMoreLayout.CallBack getCallBack() {
        return mCallBack;
    }

    public void setCallBack(RefreshLoadMoreLayout.CallBack callBack) {
        this.mCallBack = callBack;
    }

    public boolean isRefreshing() {
        return mStatus == Status.REFRESH || mStatus == Status.BACK_REFRESH || mStatus == Status.AUTO_REFRESH;
    }
}
