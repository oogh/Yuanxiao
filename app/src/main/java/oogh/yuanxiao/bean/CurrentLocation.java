package oogh.yuanxiao.bean;

import com.baidu.mapapi.model.LatLng;

/**
 * TODO: 单例设计模式，存储我的位置信息
 * 未使用，后续程序优化时再修改
 */

public class CurrentLocation {
    private CurrentLocation() {
    }

    public static CurrentLocation getInstance() {
        return mLocation != null ? mLocation : new CurrentLocation();
    }

    private LatLng location;
    private static CurrentLocation mLocation;
}
