package oogh.yuanxiao.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import oogh.yuanxiao.R;
import oogh.yuanxiao.activity.AddFoundActivity;
import oogh.yuanxiao.activity.DetailFoundActivity;
import oogh.yuanxiao.adapter.FoundRecyclerViewAdapter;
import oogh.yuanxiao.bean.Found;

public class FoundFragment extends Fragment {
    private static final String TAG = FoundFragment.class.getSimpleName();

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

    private Context mContext;
    private FoundRecyclerViewAdapter mAdapter;
    private FloatingActionButton mFloatingActionButton;

    public FoundFragment() {
    }

    public static LostFragment newInstance() {
        LostFragment fragment = new LostFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mAdapter = new FoundRecyclerViewAdapter();
        mAdapter.setOnItemClickListener(new FoundRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Found l) {
                Intent intent = new Intent(getActivity(), DetailFoundActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("objectid", l.getObjectId());
                bundle.putString("title", l.getTitle());
                bundle.putString("time", l.getCreatedAt());
                bundle.putString("phone", l.getPhone());
                bundle.putString("describe", l.getDescribe());
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
        loadData();
    }

    /**
     * 从Bmob后端加载数据
     */
    private void loadData() {
        BmobQuery<Found> query = new BmobQuery<>();
        //按照时间降序
        query.order("-createdAt");
        query.findObjects(new FindListener<Found>() {
            @Override
            public void done(List<Found> list, BmobException e) {
                if (e == null) {
                    Log.i(TAG, "list.size = " + list.size());
                    mAdapter.setDataset(list);
                } else {
                    Toast.makeText(mContext, "Lost数据获取失败...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_found, container, false);
        mRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.layout_refresh_found);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                mRefreshLayout.setRefreshing(false);
            }
        });
        mRecyclerView = (RecyclerView) root.findViewById(R.id.rv_found);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mFloatingActionButton = (FloatingActionButton) root.findViewById(R.id.fab_found);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddFoundActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadData();
    }

}
