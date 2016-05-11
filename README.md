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

将drawable,values里面的配置文件拷贝到自己的工程中,替换掉对应的文件,可自定义箭头,进度条风格以及文字大小,颜色.

![image](https://raw.githubusercontent.com/qbaowei/RefreshLoadMoreLayout/master/screenshots/RefreshLoadMoreLayout.gif)


# Download


Gradle:

(需要导入RecyclerView依赖库)

compile 'com.qbw.customview:refreshloadmorelayout:1.0.2'

# Author:


qbaowei@qq.com

