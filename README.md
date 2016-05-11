# RefreshLoadMoreLayout

支持下拉刷新,上拉加载更多,上拉自动加载更多.


<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.qbw.customview.RefreshLoadMoreLayout
        android:id="@+id/rlm"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <android.support.v7.widget.RecyclerView
            android:layout_width="match_parent"
            android:layout_height="match_parent" />

    </com.qbw.customview.RefreshLoadMoreLayout>
</FrameLayout>



![image](https://raw.githubusercontent.com/qbaowei/RefreshLoadMoreLayout/master/screenshots/RefreshLoadMoreLayout.gif)


# Download


Gradle:

(需要导入RecyclerView依赖库)

compile 'com.qbw.customview:refreshloadmorelayout:1.0.2'

# Author:


qbaowei@qq.com

