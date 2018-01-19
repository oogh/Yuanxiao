package oogh.yuanxiao.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import oogh.yuanxiao.R;
import oogh.yuanxiao.adapter.ViewPagerAdapter;

public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getSimpleName();

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ViewPagerAdapter mAdapter;
    private List<Fragment> mFragments;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragments = new ArrayList<>();
        mFragments.add(FoundFragment.newInstance());
        mFragments.add(LostFragment.newInstance());
        /*
         问题：Fragment嵌套ViewPager+2个Fragment，切换Fragment后内部Fragment显示空白
         解决：http://blog.csdn.net/taoolee/article/details/50633619
         执行：将getFragmentManger()改成getChildFragmentManger()即可
         */
        mAdapter = new ViewPagerAdapter(getChildFragmentManager(), mFragments);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mViewPager = (ViewPager) root.findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) root.findViewById(R.id.tab_layout);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar_home);
        toolbar.setTitle("首页");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        return root;
    }


}
