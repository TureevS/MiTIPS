package sample;

public class inputData {
    private String sign;
    private int time;
    private String value;

    public inputData(String sign, int time, String value){
        this.sign = sign;
        this.time = time;
        this.value = value;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
