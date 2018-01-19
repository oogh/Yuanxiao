package oogh.yuanxiao.bean;

/**
 * Created by oogh on 17-10-22.
 */

public class JobDetail {
    private String title;
    private String money;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    private String position = "上海";
    private String week;
    private String describe;
    private String deadline;


    public String getGood() {
        return good;
    }

    public void setGood(String good) {
        this.good = good;
    }

    public JobDetail(String title, String money, String position, String week, String describe,
                     String deadline, String good) {
        this.title = title;
        this.money = money;
        this.position = position;
        this.week = week;
        this.describe = describe;
        this.deadline = deadline;
        this.good = good;
    }

    private String good;


}
