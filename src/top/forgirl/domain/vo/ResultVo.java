package top.forgirl.domain.vo;

import top.forgirl.domain.ResultType;

public class ResultVo<T> {

    private int code;
    private String msg;
    private T data;

    private ResultVo(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResultVo(ResultType resultType, T data) {
        this(resultType.getCode(), resultType.getMsg(), data);
    }

    public ResultVo(T data) {
        this(ResultType.SUCCESS, data);
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
