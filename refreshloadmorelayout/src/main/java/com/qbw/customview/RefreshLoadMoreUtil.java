package com.qbw.customview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ScrollView;

import com.qbw.log.XLog;

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
            if (0 == recyclerView.getLayoutManager().getItemCount()) {
                return true;
            }
            return isContentToItem(recyclerView, true, 0);
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
            if (0 == recyclerView.getLayoutManager().getItemCount()) {
                return false;
            }
            return isContentToItem(recyclerView, false, recyclerView.getAdapter().getItemCount() - 1);
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

//    /**
//     * @param recyclerView
//     * @return
//     */
//    public static boolean isContentNearBottom(RecyclerView recyclerView) {
//        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//        if (0 == layoutManager.getItemCount()) {
//            return false;
//        }
//        if (layoutManager instanceof LinearLayoutManager || layoutManager instanceof GridLayoutManager) {
//            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
//            if (linearLayoutManager.getItemCount() - 1 == linearLayoutManager.findLastVisibleItemPosition()) {
//                return true;
//            }
//        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
//            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
//            int[] positions = staggeredGridLayoutManager.findLastVisibleItemPositions(null);
//            boolean b = false;
//            for (int p : positions) {
//                if (staggeredGridLayoutManager.getItemCount() - 1 == p) {
//                    b = true;
//                    break;
//                }
//            }
//            return b;
//        }
//        return false;
//    }
//
//    /**
//     * @param recyclerView
//     * @return
//     */
//    public static boolean isContentNearTop(RecyclerView recyclerView) {
//        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//        if (0 == layoutManager.getItemCount()) {
//            return false;
//        }
//        if (layoutManager instanceof LinearLayoutManager || layoutManager instanceof GridLayoutManager) {
//            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
//            if (0 == linearLayoutManager.findFirstVisibleItemPosition()) {
//                return true;
//            }
//        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
//            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
//            int[] positions = staggeredGridLayoutManager.findFirstVisibleItemPositions(null);
//            boolean b = false;
//            for (int p : positions) {
//                if (0 == p) {
//                    b = true;
//                    break;
//                }
//            }
//            return b;
//        }
//        return false;
//    }

    public static boolean isContentToItem(RecyclerView recyclerView, boolean isFirst, int targetIndex) {
        int adapItemCount = recyclerView.getAdapter().getItemCount();
        if (targetIndex < 0 || targetIndex >= adapItemCount) {
            XLog.e("Invalid targetIndex = %d", targetIndex);
            return false;
        } else {
            XLog.i("targetIndex = %d", targetIndex);
        }
        int[] poss = getRecyclerViewFirstCompleteVisiblePos(recyclerView, true, isFirst);
        boolean match = false;
        for (int p : poss) {
            XLog.v("first complete visible position = %d", p);
            if (targetIndex == p) {
                match = true;
                break;
            } else if (RecyclerView.NO_POSITION == p) {//NO_POSITION,比如第一个view高度很高,这种情况就没有完全显示的item
                if (0 == targetIndex) {
                    RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(targetIndex);
                    if (null == viewHolder) {
                        XLog.v("null == viewHolder");
                        match = false;
                    } else {
                        int top = viewHolder.itemView.getTop();
                        XLog.v("first item view top = %d", top);
                        match = 0 == top;
                        if (match) {
                            break;
                        }
                    }
                } else if (adapItemCount - 1 == targetIndex) {
                    RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(targetIndex);
                    if (null == viewHolder) {
                        XLog.v("null == viewHolder");
                        match = false;
                    } else {
                        int bottom = viewHolder.itemView.getBottom();
                        XLog.v("bottom item view top = %d", bottom);
                        match = recyclerView.getHeight() == bottom;
                        if (match) {
                            break;
                        }
                    }
                } else {
                    match = false;
                }
            }
        }
        return match;
    }

    /**
     *
     * @param recyclerView
     * @param isFirst true,第一个;false,最后一个
     * @return
     */
    public static int[] getRecyclerViewFirstCompleteVisiblePos(RecyclerView recyclerView, boolean isComplete, boolean isFirst) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager || layoutManager instanceof GridLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            if (isComplete) {
                if (isFirst) {
                    return new int[]{linearLayoutManager.findFirstCompletelyVisibleItemPosition()};
                } else {
                    return new int[]{linearLayoutManager.findLastCompletelyVisibleItemPosition()};
                }
            } else {
                if (isFirst) {
                    return new int[]{linearLayoutManager.findFirstVisibleItemPosition()};
                } else {
                    return new int[]{linearLayoutManager.findLastVisibleItemPosition()};
                }
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            if (isComplete) {
                if (isFirst) {
                    return staggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(null);
                } else {
                    return staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(null);
                }
            } else {
                if (isFirst) {
                    return staggeredGridLayoutManager.findFirstVisibleItemPositions(null);
                } else {
                    return staggeredGridLayoutManager.findLastVisibleItemPositions(null);
                }
            }
        }
        return null;
    }
}
