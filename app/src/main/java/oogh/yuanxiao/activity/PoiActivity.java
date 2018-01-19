package oogh.yuanxiao.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.baidu.mapapi.utils.poi.BaiduMapPoiSearch;
import com.baidu.mapapi.utils.poi.PoiParaOption;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.List;

import oogh.yuanxiao.R;
import oogh.yuanxiao.adapter.PoiRecyclerViewAdapter;

public class PoiActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener {
    private static final String TAG = PoiActivity.class.getSimpleName();

    private MapView mMapView;

    private BaiduMap mBaiduMap;
    private LocationClient mLocClient;

    private double mCurrentLat; // 纬度
    private double mCurrentLon; // 经度
    private float mCurrentAccracy; // 定位精确度

    private MyLocationData mMyLocationData; // 我的位置数据
    private boolean mFirstLocated = true; // 首次定位？

    private BDAbstractLocationListener mLocationListener = new LocationListener();

    private MaterialSearchBar mSearchBar;
    private PoiSearch mPoiSearch;
    private LatLng mLatLng;

    private ConstraintLayout mConstraintLayout;

    private List<PoiInfo> mPoiInfos;

    private RecyclerView mRecyclerView;
    private PoiRecyclerViewAdapter mAdapter = new PoiRecyclerViewAdapter();

    private ProgressBar mProgressBar;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);

        initView();

        /*
            定位步骤：
            1. 初始化LocationClient
            2. 配置LocationClientOption
            3. 实现BDAbstractLocationListener接口
            4. 开始定位
         */

        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        mLocClient = new LocationClient(getApplicationContext());
        mLocClient.registerLocationListener(mLocationListener);
        LocationClientOption options = new LocationClientOption();
        options.setOpenGps(true);
        options.setCoorType("bd09ll"); // 坐标系类型
        options.setScanSpan(1000); // 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        mLocClient.setLocOption(options);

        // 4. 开始定位
        mLocClient.start();

    }

    private void initView() {
        initToolbar();
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.layout_constraint_poi);
        mMapView = (MapView) findViewById(R.id.view_map_poi);
        mMapView.showZoomControls(false);

        mSearchBar = (MaterialSearchBar) findViewById(R.id.sb_poi);
        mSearchBar.setHint("搜索周边...");
        mSearchBar.setOnSearchActionListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_poi_result);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_poi_search);


    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_poi);
        toolbar.setTitle("我的周边");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * 定位监听器
     */
    public class LocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null || mMapView == null) {
                return;
            }
            mCurrentLat = bdLocation.getLatitude();
            mCurrentLon = bdLocation.getLongitude();
            mCurrentAccracy = bdLocation.getRadius();

            mMyLocationData = new MyLocationData.Builder()
                    .accuracy(15)
                    .latitude(mCurrentLat)
                    .longitude(mCurrentLon)
                    .build();

            mBaiduMap.setMyLocationData(mMyLocationData);

                /*
                    首次定位，跳转至当前位置，并设置缩放级别
                 */
            if (mFirstLocated) {
                mFirstLocated = !mFirstLocated;
                mLatLng = new LatLng(mCurrentLat, mCurrentLon);
                MapStatus status = new MapStatus.Builder()
                        .target(mLatLng)
                        .zoom(17.8556f) // 缩放级别
                        .build();
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(status));
            }
        }
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        Log.i(TAG, "开始检索...");
        mProgressBar.setVisibility(View.VISIBLE);
        mSearchBar.disableSearch();

        /*
            POI检索
            1. 创建PoiSearch对象
            2. 创建PoiSearch监听器
            3. 设置PoiSearch监听器
            4. 发起检索请求
            5. 释放POI检索实例
         */
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);

        PoiNearbySearchOption nearbySearchOption
                = new PoiNearbySearchOption()
                .location(mLatLng)
                .radius(3000)
                .pageNum(1)
                .keyword(mSearchBar.getText().toString());
        mPoiSearch.searchNearby(nearbySearchOption);
        mPoiSearch.destroy();
    }

    @Override
    public void onButtonClicked(int buttonCode) {
    }

    /**
     * Poi检索监听器
     */
    OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
        public void onGetPoiResult(PoiResult result) {
            Log.i(TAG, "onGetPoiResult: 检索完成...");

            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Log.i(TAG, "onGetPoiResult: 检索失败...");
                return;
            }

            mBaiduMap.clear();
            PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result);
            overlay.addToMap();
            overlay.zoomToSpan();

            mPoiInfos = result.getAllPoi();
            mAdapter.setDataset(mPoiInfos);
            mAdapter.setOnItemClickListener(new PoiRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, PoiInfo info) {
                    openPoiDetailByBaiduMap(info);
                }
            });
            mProgressBar.setVisibility(View.GONE);
        }

        public void onGetPoiDetailResult(PoiDetailResult result) {
            //获取Place详情页检索结果
            Log.i(TAG, "onGetPoiDetailResult: ...");
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
            Log.i(TAG, "onGetPoiResult: ...");
            mProgressBar.setVisibility(View.GONE);

        }
    };

    private class MyPoiOverlay extends PoiOverlay {
        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            Log.i(TAG, "onPoiClick: index = " + index);
            final PoiInfo info = mPoiInfos.get(index);
            //创建InfoWindow展示的view
            Button button = new Button(getApplicationContext());
            button.setText(info.name);
            button.setBackgroundResource(R.drawable.popup);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPoiDetailByBaiduMap(info);
                }
            });
            LatLng ll = info.location;
            InfoWindow mInfoWindow = new InfoWindow(button, ll, -47);
            mBaiduMap.showInfoWindow(mInfoWindow);
            return true;
        }
    }

    private void openPoiDetailByBaiduMap(PoiInfo info) {
        PoiParaOption para = new PoiParaOption().uid(info.uid); // 天安门

        try {
            BaiduMapPoiSearch.openBaiduMapPoiDetialsPage(para, PoiActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
            showDialog();
        }
    }

    /**
     * 提示未安装百度地图app或app版本过低
     */
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(PoiActivity.this);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.poi_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.type_list:
                showAsList();
                break;
            case R.id.type_map:
                showAsMap();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 列表的形式显示详情
     */
    private void showAsList() {
        mMapView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * 地图标注的形式显示详情
     */
    private void showAsMap() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mMapView.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mLocClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

}
