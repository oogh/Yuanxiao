package oogh.yuanxiao.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import oogh.yuanxiao.R;
import oogh.yuanxiao.bean.Location;

public class MapActivity extends AppCompatActivity {
    private static final String TAG = MapActivity.class.getSimpleName();

    private MapView mMapView;

    private BaiduMap mBaiduMap;
    private LocationClient mLocClient;

    private double mCurrentLat; // 纬度
    private double mCurrentLon; // 经度
    private float mCurrentAccracy; // 定位精确度

    private MyLocationData mMyLocationData; // 我的位置数据
    private boolean mFirstLocated = true; // 首次定位？

    private BDAbstractLocationListener mLocationListener = new LocationListener();

    private List<Location> mLocations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initView();

        /*
            定位步骤：
            1. 初始化LocationClient
            2. 配置LocationClientOption
            3. 实现BDAbstractLocationListener接口
            4. 开始定位
         */

        mBaiduMap = mMapView.getMap();
        mBaiduMap.showMapPoi(false);

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

        makeOverlay();

    }

    /**
     * 添加标记图层
     */
    private void makeOverlay() {
        try (InputStream in = getResources().getAssets().open("locations.json")) {
            for (Location loc : readJsonFromStream(in)) {
                LatLng point = new LatLng(loc.getLat(), loc.getLng());
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_place);
                Log.i(TAG, "makeOverlay: bitmap == null?" + (bitmap == null));
                OverlayOptions iconOption = new MarkerOptions()
                        .position(point)
                        .icon(bitmap);
                //定义文字所显示的坐标点
                OverlayOptions textOption = new TextOptions()
//                        .bgColor(0xAAFFFF00)
                        .fontSize(24)
                        .fontColor(Color.RED)
                        .text(loc.getDescribe())
                        .rotate(10)
                        .position(point);
                mBaiduMap.addOverlay(textOption);
                mBaiduMap.addOverlay(iconOption);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 从stream中读取json
     *
     * @param in
     */
    private List<Location> readJsonFromStream(InputStream in) throws IOException {
        try (JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"))) {
            return readLocationArray(reader);
        }
    }

    private List<Location> readLocationArray(JsonReader reader) throws IOException {
        List<Location> locations = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            locations.add(readLocation(reader));
        }
        reader.endArray();
        return locations;
    }

    /**
     * 读取Location
     *
     * @param reader
     * @return
     */
    private Location readLocation(JsonReader reader) throws IOException {
        String tag = null;
        String describe = null;
        double lat = -1;
        double lng = -1;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if ("tag".equals(name)) {
                tag = reader.nextString();
            } else if ("describe".equals(name)) {
                describe = reader.nextString();
            } else if ("lat".equals(name)) {
                lat = reader.nextDouble();
            } else if ("lng".equals(name)) {
                lng = reader.nextDouble();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Location(tag, describe, lat, lng);
    }


    private void initView() {
        initToolbar();
        mMapView = (MapView) findViewById(R.id.view_map_school);
        mMapView.showZoomControls(false);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_map);
        toolbar.setTitle("校内地图");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                LatLng ll = new LatLng(mCurrentLat, mCurrentLon);
                MapStatus status = new MapStatus.Builder()
                        .target(ll)
                        .zoom(18.0f) // 缩放级别
                        .build();
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(status));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.map_normal: // 普通地图
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
            case R.id.map_satellite:// 卫星地图
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
//            case R.id.map_none:// 空白地图
//                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);
//                break;
            default:
                break;
        }
        return true;
    }
}
