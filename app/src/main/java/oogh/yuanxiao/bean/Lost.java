package oogh.yuanxiao.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by oogh on 17-10-14.
 */

public class Lost extends BmobObject {
    private String title;
    private String describe;
    private String phone;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Lost(String title, String describe, String phone) {

        this.title = title;
        this.describe = describe;
        this.phone = phone;
    }

    public Lost() {

    }

}
