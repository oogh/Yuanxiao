package oogh.yuanxiao.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import oogh.yuanxiao.R;
import oogh.yuanxiao.fragment.AccountFragment;
import oogh.yuanxiao.fragment.DashboardFragment;
import oogh.yuanxiao.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity implements
        DashboardFragment.OnFragmentInteractionListener {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment(HomeFragment.newInstance());
                    return true;
                case R.id.navigation_dashboard:
                    replaceFragment(DashboardFragment.newInstance(null, null));
                    return true;
                case R.id.navigation_notifications:
                    replaceFragment(AccountFragment.newInstance(null, null));
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // 初始化时，首先将HomeFragment添加到FrameLayout容器中
        // 当需要替换是只需要调用replaceFragment(Fragment)方法即可
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, HomeFragment.newInstance())
                .commit();
    }


    /**
     * 切换Fragment
     *
     * @param fragment
     */
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
