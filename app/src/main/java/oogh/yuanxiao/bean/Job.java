package oogh.yuanxiao.bean;

import java.io.Serializable;

/**
 * Created by oogh on 17-10-18.
 */

public class Job implements Serializable {
    private String title;
    private String logoUri;
    private String company_describe;
    private String extra;
    private String detailUrl;

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }


    public Job() {
    }

    @Override
    public String toString() {
        return "Job{" +
                "title='" + title + '\'' +
                ", logoUri='" + logoUri + '\'' +
                ", company_describe='" + company_describe + '\'' +
                ", extra='" + extra + '\'' +
                '}';
    }

    public Job(String title, String logoUri, String company_describe, String extra) {
        this.title = title;
        this.logoUri = logoUri;
        this.company_describe = company_describe;
        this.extra = extra;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogoUri() {
        return logoUri;
    }

    public void setLogoUri(String logoUri) {
        this.logoUri = logoUri;
    }

    public String getCompany_describe() {
        return company_describe;
    }

    public void setCompany_describe(String company_describe) {
        this.company_describe = company_describe;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
