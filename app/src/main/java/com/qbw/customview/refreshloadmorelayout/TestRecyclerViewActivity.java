package com.qbw.customview.refreshloadmorelayout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qbw.customview.RefreshLoadMoreLayout;
import com.qbw.log.XLog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qinbaowei
 * @createtime 2015/10/14 17:57
 */

public class TestRecyclerViewActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentTest()).commit();
    }


    public static class FragmentTest extends Fragment implements RefreshLoadMoreLayout.CallBack {
        private RefreshLoadMoreLayout mRefreshLoadMoreLayout;
        private MyViewAdapter mAdapter;
        private Handler handler = new Handler();

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_test_recyclerview, container, false);
            mRefreshLoadMoreLayout = (RefreshLoadMoreLayout) view.findViewById(R.id.rlm);
            /**
             * canRefresh 是否下拉刷新
             * canLoadMore 是否上拉加载更多
             * autoLoadMore 自动加载更多（默认不自动加载更多）
             * showLastRefreshTime 是否显示上次刷新时间（默认不显示）
             * multiTask 下拉刷新上拉加载更多可同时进行（默认下拉刷新和上拉加载更多不能同时进行）
             */
            mRefreshLoadMoreLayout.init(new RefreshLoadMoreLayout.Config(this).canRefresh(true).canLoadMore(true).autoLoadMore().showLastRefreshTime(TestRecyclerViewActivity.class, "yyyy-MM-dd").multiTask());
            final RecyclerView recyclerView = (RecyclerView) mRefreshLoadMoreLayout.getContentView();
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return 0 == recyclerView.getAdapter().getItemViewType(position) ? 1 : 3;
                }
            });
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(mAdapter = new MyViewAdapter(getActivity()));
            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    outRect.left = 10;
                    outRect.top = 10;
                    outRect.right = 10;
                    outRect.bottom = 10;
                }
            });

            return view;
        }

        @Override
        public void onRefresh() {
            XLog.v("onRefresh");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int num = mAdapter.getFirstNumber();
                    List<Integer> dataList = new ArrayList<Integer>();
                    for (int i = 1; i > 0; i--) {
                        int _num = num;
                        _num += i;
                        dataList.add(_num);
                    }
                    mAdapter.addRefreshData(dataList);
                    mRefreshLoadMoreLayout.stopRefresh();
                    XLog.v("onRefresh finish");
                }
            }, 200);
        }

        @Override
        public void onLoadMore() {
            XLog.v("onLoadMore");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int num = mAdapter.getLastNumber();
                    List<Integer> dataList = new ArrayList<Integer>();
                    for (int i = 0; i < 10; i++) {
                        --num;
                        dataList.add(num);
                    }
                    mAdapter.addLoadData(dataList);
                    mRefreshLoadMoreLayout.stopLoadMoreNoData(mAdapter.getItemCount() >= 50);//依然可以上拉，显示没有更多数据
                }
            }, 1000);
        }

        private static class MyViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
            private Context mContext;

            private List<Integer> mDataList = new ArrayList<Integer>();

            public MyViewAdapter(Context context) {
                this.mContext = context;
            }

            public void addRefreshData(List<Integer> dataList) {
                this.mDataList.addAll(0, dataList);
                notifyDataSetChanged();
            }

            public void addLoadData(List<Integer> dataList) {
                this.mDataList.addAll(dataList);
                notifyDataSetChanged();
            }

            public int getFirstNumber() {
                return 0 == mDataList.size() ? 0 : mDataList.get(0);
            }

            public int getLastNumber() {
                return 0 == mDataList.size() ? 0 : mDataList.get(mDataList.size() - 1);
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                if (0 == viewType) {
                    View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_item, parent, false);
                    view.setBackgroundColor(Color.parseColor("#aabbcc"));
                    return new MyViewHolder(view);
                } else if (1 == viewType){
                    View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_item1, parent, false);
                    view.setBackgroundColor(Color.parseColor("#00bbcc"));
                    return new MyViewHolder(view);
                }
                return null;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                MyViewHolder holder1 = (MyViewHolder) holder;
                holder1.mTxt.setText(mDataList.get(position) + "");
            }

            @Override
            public int getItemCount() {
                return mDataList.size();
            }

            @Override
            public int getItemViewType(int position) {
                if (0 == position) {
                    return 1;
                }
                return 0;
            }
        }

        private static class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView mTxt;

            public MyViewHolder(View itemView) {
                super(itemView);
                mTxt = (TextView) itemView.findViewById(R.id.tv);
            }
        }
    }
}
