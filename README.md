# RefreshLoadMoreLayout

支持下拉刷新,上拉加载更多,上拉自动加载更多.


<com.qbw.customview.RefreshLoadMoreLayout

        android:id="@+id/refreshloadmore"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:rll_bg=""
        app:rll_footer_height=""
        app:rll_footer_hint_loading=""
        app:rll_footer_hint_normal=""
        app:rll_footer_hint_ready=""
        app:rll_footer_progress_size=""
        app:rll_header_height=""
        app:rll_header_hint_loading=""
        app:rll_header_hint_normal=""
        app:rll_header_hint_ready=""
        app:rll_header_progress=""
        app:rll_header_progress_size=""
        app:rll_textcolor="">

        <View/>
        或者<ScrollView/>
        或者<ListView/>
        或者<GridView/>
        或者<RecyclerView/>

</com.qbw.customview.RefreshLoadMoreLayout>

如果不配置自定义属性的值，那么RefreshLoadMoreLayout将会使用默认值。

![image](https://raw.githubusercontent.com/qbaowei/RefreshLoadMoreLayout/master/screenshots/RefreshLoadMoreLayout.gif)


# 注意事项


1.RefreshLoadMoreLayout工程默认使用的RecyclerView版本为23.0.1，如果你需要使用低版本的RecyclerView，那么

        compile('替换为你使用的RecyclerView版本'){
            force = true
        }
        
        但是不建议使用低版本！


# Download


Gradle:


compile 'com.qbw.customview:refreshloadmorelayout:2.3.0'

# 2.3.0


1.可以在xml中配置属性值

2.增加setIgnoreTouchEvent


# 2.2.0


1.增加stopRefreshNoMoreData


# 2.1.0


1.重载stopRefresh，增加参数，noMoreData（刷新时数据少于一页的时候需要，默认false）和delay（延迟隐藏刷新view，默认0）


# 2.0.2


1.修复只有一个item且item高度大于屏幕高度，导致下拉和上拉无法使用的


# Author:


qbaowei@qq.com

