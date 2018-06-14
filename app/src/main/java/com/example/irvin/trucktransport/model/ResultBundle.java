package com.example.irvin.trucktransport.model;

/**
 * Created by IvanX on 16.4.2017..
 */

public class ResultBundle {
    Object result;
    boolean isSuccess;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public ResultBundle(Object result, boolean isSuccess) {
        super();
        this.result = result;
        this.isSuccess = isSuccess;
    }

    public ResultBundle(boolean isSuccess) {
        super();
        this.isSuccess = isSuccess;
    }

    public ResultBundle(Object result) {
        super();
        this.result = result;
    }

    public ResultBundle() {
        super();
    }

}
