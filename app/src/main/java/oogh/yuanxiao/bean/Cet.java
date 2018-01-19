package oogh.yuanxiao.bean;

/**
 * Created by oogh on 17-10-23.
 */

public class Cet {
    private String listening;
    private String writing;
    private String reading;
    private String sum;

    @Override
    public String toString() {
        return "Cet{" +
                "listening='" + listening + '\'' +
                ", writing='" + writing + '\'' +
                ", reading='" + reading + '\'' +
                ", sum='" + sum + '\'' +
                '}';
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public Cet() {
    }

    public Cet(String listening, String writing, String reading, String sum) {

        this.listening = listening;
        this.writing = writing;
        this.reading = reading;
        this.sum = sum;
    }

    public String getListening() {
        return listening;
    }

    public void setListening(String listening) {
        this.listening = listening;
    }

    public String getWriting() {
        return writing;
    }

    public void setWriting(String writing) {
        this.writing = writing;
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }
}
