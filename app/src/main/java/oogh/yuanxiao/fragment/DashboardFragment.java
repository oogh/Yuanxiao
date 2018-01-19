package oogh.yuanxiao.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import oogh.yuanxiao.R;
import oogh.yuanxiao.activity.CetActivity;
import oogh.yuanxiao.activity.JobActivity;
import oogh.yuanxiao.activity.MapActivity;
import oogh.yuanxiao.activity.NoticeActivity;
import oogh.yuanxiao.activity.PoiActivity;
import oogh.yuanxiao.adapter.DashboardItemAdapter;
import oogh.yuanxiao.bean.DashboardItem;
import oogh.yuanxiao.utils.BannerImageLoader;

public class DashboardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView mDashboardRecyclerView;
    private DashboardItemAdapter mAdapter;
    private List<DashboardItem> mDataSet;
    private int[] mImages = {
            R.drawable.ic_dashboard_community, R.drawable.ic_dashboard_job,
            R.drawable.ic_dashboard_map, R.drawable.ic_dashboard_near_me,
            R.drawable.ic_dashboard_cet, R.drawable.ic_dashboard_more};
    private String[] mTitles = {"通知公告", "兼职/工作", "校园地图", "我的周边", "四六级", "更多"};

    public DashboardFragment() {
    }

    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        // TODO: Dashboard页面中GridLayout菜单数据部分
        mDataSet = new ArrayList<>();
        for (int i = 0; i < mImages.length; i++) {
            DashboardItem item = new DashboardItem(mImages[i], mTitles[i]);
            mDataSet.add(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mDashboardRecyclerView = (RecyclerView) root.findViewById(R.id.rv_dashboard);
        mDashboardRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mAdapter = new DashboardItemAdapter(mDataSet);
        mAdapter.setOnDashboardItemClickListener(new DashboardItemAdapter.OnDashboardItemClickListener() {
            @Override
            public void onDashboardItemClicked(View v, int position) {
                switch (position) {
                    case 0: // 通知公告
                        toActivity(NoticeActivity.class);
                        break;
                    case 1: // 兼职工作
                        toActivity(JobActivity.class);
                        break;
                    case 2: // 地图
                        toActivity(MapActivity.class);
                        break;
                    case 3: // 我的周边
                        toActivity(PoiActivity.class);
                        break;
                    case 4: // 四六级
                        toActivity(CetActivity.class);
                        break;
                    case 5: // 更多...
                        break;
                    default:
                        break;
                }
            }
        });
        mDashboardRecyclerView.setAdapter(mAdapter);
        setUpBanner(root);
        setUpToolbar(root);
        return root;
    }

    /**
     * 跳转至其他Activity
     *
     * @param clz
     */
    private void toActivity(Class<?> clz) {
        startActivity(new Intent(getActivity(), clz));
    }

    private void setUpBanner(View root) {
        String[] imageUrls = getResources().getStringArray(R.array.imageUrls);
        List<?> images = new ArrayList(Arrays.asList(imageUrls));
        Banner banner = (Banner) root.findViewById(R.id.banner);
        banner.setImageLoader(new BannerImageLoader());
        banner.setImages(images);
        banner.start();
    }

    private void setUpToolbar(View root) {
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar_dashboard);
        toolbar.setTitle("上海第二工业大学");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
