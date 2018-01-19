package oogh.yuanxiao.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by oogh on 17-9-21.
 */

public class Account extends BmobUser {
    private String school;
    private String phone;
    private BmobFile avatar;

    public Account() {
        super();
    }

    public Account(BmobFile avatar, String username, String email, String school, String phone, String passwrod) {
        this.avatar = avatar;
        super.setUsername(username);
        super.setEmail(email);
        this.school = school;
        this.phone = phone;
        super.setPassword(passwrod);
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BmobFile getAvatar() {
        return avatar;
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar = avatar;
    }


}
