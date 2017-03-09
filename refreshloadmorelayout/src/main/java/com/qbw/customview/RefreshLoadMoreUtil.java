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
            return isContentToItem(recyclerView, true, 0, Type.TOP);
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
            return isContentToItem(recyclerView,
                                   false,
                                   recyclerView.getAdapter().getItemCount() - 1,
                                   Type.BOTTOM);
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

    public static boolean isContentToItem(RecyclerView recyclerView,
                                          boolean isFirst,
                                          int targetIndex,
                                          Type type) {
        int adapItemCount = recyclerView.getAdapter().getItemCount();
        if (targetIndex < 0 || targetIndex >= adapItemCount) {
            if (XLog.isEnabled()) XLog.w("Invalid targetIndex = %d", targetIndex);
            return false;
        } else {
            if (XLog.isEnabled()) XLog.v("targetIndex = %d", targetIndex);
        }
        int[] poss = getRecyclerViewFirstCompleteVisiblePos(recyclerView, true, isFirst);
        boolean match = false;
        for (int p : poss) {
            if (XLog.isEnabled()) XLog.v("complete visible position = %d", p);
            if (targetIndex == p) {
                match = true;
                break;
            } else if (RecyclerView.NO_POSITION == p) {//NO_POSITION,比如第一个view高度很高,这种情况就没有完全显示的item
                if (type == Type.TOP && 0 == targetIndex) {
                    RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(
                            targetIndex);
                    if (null == viewHolder) {
                        if (XLog.isEnabled()) XLog.v("null == viewHolder");
                        match = false;
                    } else {
                        int topMargin = recyclerView.getLayoutManager()
                                                    .getTopDecorationHeight(viewHolder.itemView);
                        int top = viewHolder.itemView.getTop();
                        if (XLog.isEnabled())
                            XLog.v("first item view top = %d, topMargin = %d", top, topMargin);
                        match = 0 == top - topMargin;
                        if (match) {
                            break;
                        }
                    }
                } else if (type == Type.BOTTOM && adapItemCount - 1 == targetIndex) {
                    RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(
                            targetIndex);
                    if (null == viewHolder) {
                        if (XLog.isEnabled()) XLog.v("null == viewHolder");
                        match = false;
                    } else {
                        int bottomMargin = recyclerView.getLayoutManager()
                                                       .getBottomDecorationHeight(viewHolder.itemView);
                        int bottom = viewHolder.itemView.getBottom();
                        int rheight = recyclerView.getHeight();
                        if (XLog.isEnabled()) XLog.v(
                                "bottom item view bottom = %d, bottomMargin = %d, recyclerview height = %d",
                                bottom,
                                bottomMargin,
                                rheight);
                        match = rheight == bottom + bottomMargin;
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
     * @param recyclerView
     * @param isFirst      true,第一个;false,最后一个
     * @return
     */
    public static int[] getRecyclerViewFirstCompleteVisiblePos(RecyclerView recyclerView,
                                                               boolean isComplete,
                                                               boolean isFirst) {
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

    private enum Type {
        TOP,
        BOTTOM,
        OTHER
    }
}
