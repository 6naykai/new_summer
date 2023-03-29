package com.nbu.util;

/**
 * 工具类，用来包装处理结果
 * tag   是附带信息
 * data  是数理后的数据
 * state 表示处理成功状态
 */

public class Result {
    private String tag;
    private Object data;
    private Boolean state;

    public Result(String tag) {
        this.tag = tag;
    }

    public Result(Object data) {
        this.data = data;
    }

    public Result(Boolean state) {
        this.state = state;
    }

    public Result(String tag, Object data) {
        this.tag = tag;
        this.data = data;
    }

    public Result(String tag, Boolean state) {
        this.tag = tag;
        this.state = state;
    }

    public Result(Object data, Boolean state) {
        this.data = data;
        this.state = state;
    }

    public Result(String tag, Object data, Boolean state) {
        this.tag = tag;
        this.data = data;
        this.state = state;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getTag() {
        return tag;
    }

    public Object getData() {
        return data;
    }

    public Boolean getState() {
        return state;
    }

    @Override
    public String toString() {
        return "Result{" +
                "tag='" + tag + '\'' +
                ", data=" + data +
                ", state=" + state +
                '}';
    }
}
