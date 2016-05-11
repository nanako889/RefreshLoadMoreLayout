package com.qbw.customview.refreshloadmorelayout;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.qbw.customview.RefreshLoadMoreLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  qinbaowei
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
        private RefreshLoadMoreLayout refreshLoadMoreLayout;
        private RecyclerView recyclerView;
        private MyViewAdapter myViewAdapter;
        private Handler handler = new Handler();


        private TextView textView;

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_test_recyclerview, container, false);
            textView = (TextView) view.findViewById(R.id.tv);
            refreshLoadMoreLayout = (RefreshLoadMoreLayout) view.findViewById(R.id.rlm);
            refreshLoadMoreLayout.init(new RefreshLoadMoreLayout.Config(this).autoLoadMore(true));
            recyclerView = (RecyclerView) refreshLoadMoreLayout.getContentView();
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return 0 == recyclerView.getAdapter().getItemViewType(position) ? 1 : 3;
                }
            });
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(myViewAdapter = new MyViewAdapter(getActivity()));
            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    outRect.left = 30;
                    outRect.top = 30;
                    outRect.right = 30;
                    outRect.bottom = 30;
                }
            });

            return view;
        }

        @Override
        public void onDetach() {
            super.onDetach();
            Log.i("------------------", "onDetach");
        }

        @Override
        public void onRefresh() {
            Toast.makeText(getActivity(), "onRefresh", Toast.LENGTH_SHORT).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int num = myViewAdapter.getFirstNumber();
                    List<Integer> dataList = new ArrayList<Integer>();
                    for (int i = 10; i > 0; i--) {
                        int _num = num;
                        _num += i;
                        dataList.add(_num);
                    }
                    myViewAdapter.addRefreshData(dataList);
                    refreshLoadMoreLayout.stopRefresh(500);
                    Toast.makeText(getActivity(), "onRefresh finish", Toast.LENGTH_SHORT).show();
                }
            }, 200);
        }

        @Override
        public void onLoadMore() {
            Toast.makeText(getActivity(), "onLoadMore", Toast.LENGTH_SHORT).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int num = myViewAdapter.getLastNumber();
                    List<Integer> dataList = new ArrayList<Integer>();
                    for (int i = 0; i < 10; i++) {
                        --num;
                        dataList.add(num);
                    }
                    myViewAdapter.addLoadData(dataList);
                    refreshLoadMoreLayout.stopLoadMore(myViewAdapter.getItemCount() >= 100);
                    if (null != getActivity()) {
                        Toast.makeText(getActivity(), "onLoadMore finish", Toast.LENGTH_SHORT).show();
                    }
                }
            }, 5000);
        }

        private static class MyViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
            private Context context;

            private List<Integer> dataList = new ArrayList<Integer>();

            public MyViewAdapter(Context context) {
                this.context = context;
            }

            public void addRefreshData(List<Integer> dataList) {
                this.dataList.addAll(0, dataList);
                notifyDataSetChanged();
            }

            public void addLoadData(List<Integer> dataList) {
                this.dataList.addAll(dataList);
                notifyDataSetChanged();
            }

            public int getFirstNumber() {
                return 0 == dataList.size() ? 0 : dataList.get(0);
            }

            public int getLastNumber() {
                return 0 == dataList.size() ? 0 : dataList.get(dataList.size() - 1);
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                if (0 == viewType) {
                    View view = LayoutInflater.from(context).inflate(R.layout.adapter_item, parent, false);
                    return new MyViewHolder(view);
                }
                return null;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                if (0 == getItemViewType(position)) {
                    MyViewHolder holder1 = (MyViewHolder) holder;
                    holder1.tv.setText(dataList.get(position) + "");
                } else if (1 == getItemViewType(position)) {

                }
            }

            @Override
            public int getItemCount() {
                return dataList.size();
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }
        }

        private static class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tv;

            public MyViewHolder(View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.tv);
            }
        }

        private static class MyViewHolder1 extends RecyclerView.ViewHolder {
            public View rootView;

            public MyViewHolder1(View itemView) {
                super(itemView);
                rootView = itemView;
            }
        }
    }
}
