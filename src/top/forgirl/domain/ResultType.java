package top.forgirl.domain;

public enum ResultType {
    SUCCESS(2000, "SUCCESS"),
    FAILED(4000, "FAILED"),
    PARAM_ERROR(4001, "PARAM ERROR"),
    NON_LOGIN(4002, "PLEASE LOGIN FIRST");

    private int code;
    private String msg;

    ResultType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
