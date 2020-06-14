package com.hlsii.vo;

/**
 * @author ScXin
 * @date 4/26/2020 3:24 PM
 */

import com.alibaba.fastjson.JSONObject;
import com.hlsii.commdef.Constants;

import java.io.Serializable;
import java.util.List;

public class ReturnWrap implements Serializable {
    private static final long serialVersionUID = 8678608357622838337L;

    private String code;
    private String msg;
    private Object data;

    public ReturnWrap(boolean success, Object data) {
        super();
        this.code = success ? Constants.RETURN_SUCCESS
                : Constants.RETURN_FAILURE;
        if (success) {
            this.data = data;
        } else {
            this.msg = data.toString();
        }
    }

    public ReturnWrap(String code, String msg, Object data) {
        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ReturnWrap(String code, Object data) {
        super();
        this.code = code;
        this.data = data;
    }

    public ReturnWrap(String code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public ReturnWrap(String code) {
        super();
        this.code = code;
    }

    public ReturnWrap() {
        this.code = Constants.RETURN_SUCCESS;
    }

    public ReturnWrap(boolean success) {
        super();
        this.code = success ? Constants.RETURN_SUCCESS
                : Constants.RETURN_FAILURE;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        if (data instanceof List) {
            setList(data);
        } else {
            this.data = data;
        }
    }

    public String toJSONString() {
        return JSONObject.toJSONString(this);
    }

    private void setList(Object list) {
        JSONObject dataObj = new JSONObject();
        dataObj.put("list", list);
        setData(dataObj);
    }
}
