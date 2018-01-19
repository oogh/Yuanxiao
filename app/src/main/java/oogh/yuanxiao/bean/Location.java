package oogh.yuanxiao.bean;

/**
 * Created by oogh on 17-10-22.
 */

public class Location {
    private String tag;
    private String describe;
    private double lat;
    private double lng;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "Location{" +
                "tag='" + tag + '\'' +
                ", describe='" + describe + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }

    public Location(String tag, String describe, double lat, double lng) {
        this.tag = tag;
        this.describe = describe;
        this.lat = lat;
        this.lng = lng;
    }
}
