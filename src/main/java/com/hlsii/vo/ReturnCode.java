package com.hlsii.vo;

import java.io.Serializable;

/**
 * @author ScXin
 * @date 4/30/2020 11:33 PM
 */
public class ReturnCode implements Serializable {

    private boolean success;

    private String message;

    public ReturnCode(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
