package com.qbw.customview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ScrollView;

/**
 * @author qinbaowei
 * @createtime 2015/10/27 14:03
 */

public class RefreshLoadMoreUtil {
    /**
     * @param view
     * @return
     */
    public static boolean isContentToTop(View view) {
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (0 == layoutManager.getItemCount()) {
                return true;
            }
            if (layoutManager instanceof LinearLayoutManager || layoutManager instanceof GridLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                if (0 == linearLayoutManager.findFirstCompletelyVisibleItemPosition()) {
                    return true;
                }
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                int[] positions = staggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(null);
                boolean b = false;
                for (int p : positions) {
                    if (0 == p) {
                        b = true;
                        break;
                    }
                }
                return b;
            }
        } else if (view instanceof ScrollView) {
            ScrollView scrollView = (ScrollView) view;
            if (0 == scrollView.getScrollY()) {
                return true;
            }
        } else if (view instanceof AbsListView) {
            AbsListView listView = (AbsListView) view;
            if (0 == listView.getCount()) {
                return true;
            }
            if (0 == listView.getFirstVisiblePosition()) {//此时没有完全显示
                View firstVisibleItemView = listView.getChildAt(0);
                if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                    return true;
                }
            }
        } else if (view instanceof View) {
            return true;
        }
        return false;
    }

    /**
     * @param view
     * @return
     */
    public static boolean isContentToBottom(View view) {
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (0 == layoutManager.getItemCount()) {
                return false;
            }
            if (layoutManager instanceof LinearLayoutManager || layoutManager instanceof GridLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                if (linearLayoutManager.getItemCount() - 1 == linearLayoutManager.findLastCompletelyVisibleItemPosition()) {
                    return true;
                }
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                int[] positions = staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(null);
                boolean b = false;
                for (int p : positions) {
                    if (staggeredGridLayoutManager.getItemCount() - 1 == p) {
                        b = true;
                        break;
                    }
                }
                return b;
            }
        } else if (view instanceof ScrollView) {
            ScrollView scrollView = (ScrollView) view;
            if (scrollView.getScrollY() + scrollView.getHeight() >= scrollView.getMeasuredHeight()) {
                return true;
            }
        } else if (view instanceof AbsListView) {
            AbsListView listView = (AbsListView) view;
            if (0 == listView.getCount()) {
                return false;
            }
            if (listView.getLastVisiblePosition() == listView.getCount() - 1) {//没有完全显示
                View lastVisibleItemView = listView.getChildAt(listView.getChildCount() - 1);
                if (lastVisibleItemView != null && lastVisibleItemView.getBottom() <= listView.getHeight()) {
                    return true;
                }
            }
        } else if (view instanceof View) {
            return true;
        }
        return false;
    }

    /**
     * @param recyclerView
     * @return
     */
    public static boolean isContentNearBottom(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (0 == layoutManager.getItemCount()) {
            return false;
        }
        if (layoutManager instanceof LinearLayoutManager || layoutManager instanceof GridLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            if (linearLayoutManager.getItemCount() - 1 == linearLayoutManager.findLastVisibleItemPosition()) {
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int[] positions = staggeredGridLayoutManager.findLastVisibleItemPositions(null);
            boolean b = false;
            for (int p : positions) {
                if (staggeredGridLayoutManager.getItemCount() - 1 == p) {
                    b = true;
                    break;
                }
            }
            return b;
        }
        return false;
    }

    /**
     * @param recyclerView
     * @return
     */
    public static boolean isContentNearTop(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (0 == layoutManager.getItemCount()) {
            return false;
        }
        if (layoutManager instanceof LinearLayoutManager || layoutManager instanceof GridLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            if (0 == linearLayoutManager.findFirstVisibleItemPosition()) {
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int[] positions = staggeredGridLayoutManager.findFirstVisibleItemPositions(null);
            boolean b = false;
            for (int p : positions) {
                if (0 == p) {
                    b = true;
                    break;
                }
            }
            return b;
        }
        return false;
    }
}
