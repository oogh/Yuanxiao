package oogh.yuanxiao.bean;

/**
 * Created by oogh on 17-9-19.
 */

public class DashboardItem {
    public DashboardItem(int image, String title) {
        this.image = image;
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private int image;
    private String title;
}
