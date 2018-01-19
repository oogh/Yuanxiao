package oogh.yuanxiao.bean;

import java.io.Serializable;

/**
 * Created by oogh on 17-10-23.
 */

public class Notice implements Serializable {

    private String imageUri;

    @Override
    public String toString() {
        return "Notice{" +
                "imageUri='" + imageUri + '\'' +
                ", tag='" + tag + '\'' +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", href='" + href + '\'' +
                '}';
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Notice(String imageUri, String tag, String title, String date, String href) {

        this.imageUri = imageUri;
        this.tag = tag;
        this.title = title;
        this.date = date;
        this.href = href;
    }

    private String tag;
    private String title;
    private String date;
    private String href;

    public Notice() {
    }

}
