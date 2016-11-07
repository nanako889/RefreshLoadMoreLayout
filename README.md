# RefreshLoadMoreLayout

支持下拉刷新,上拉加载更多,上拉自动加载更多.


<com.qbw.customview.RefreshLoadMoreLayout
        android:id="@+id/refreshloadmore"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <View/>
        或者<ScrollView/>
        或者<ListView/>
        或者<GridView/>
        或者<RecyclerView/>

</com.qbw.customview.RefreshLoadMoreLayout>


将drawable,values里面的配置文件拷贝到自己的工程中,替换掉对应的文件,可自定义箭头,进度条风格以及文字大小,颜色.

![image](https://raw.githubusercontent.com/qbaowei/RefreshLoadMoreLayout/master/screenshots/RefreshLoadMoreLayout.gif)


# Download


Gradle:


compile 'com.qbw.customview:refreshloadmorelayout:2.0.2'


# 2.1.0


1.重载stopRefresh，增加参数，noMoreData（刷新时数据少于一页的时候需要，默认false）和delay（延迟隐藏刷新view，默认0）


# 2.0.2


1.修复只有一个item且item高度大于屏幕高度，导致下拉和上拉无法使用的


# Author:


qbaowei@qq.com

