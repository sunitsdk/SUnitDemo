package com.shareit.sunit;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * create by huanting on 2018/9/5 下午8:09
 */
public class TokenBean {

    /**
     * bizCode : 0000
     * message : success
     * data : qkG1+h+z9CQccuET9zyIOLp2aDtKXEbuspUuzi9dl6y4XEY81QM+kvitvk7E7/lkGmqo+f0So8KkDI1/gb7/zIrps5TLBk+1iIXj5n657Kuu20O/LZqzOT0xUaZrrw/T
     */

    private String bizCode;
    private String message;
    private String data;

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void parseData(String jsonStr) {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            bizCode = jsonObject.optString("bizCode");
            message = jsonObject.optString("message");
            data = jsonObject.optString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        return "bizCode:" + bizCode + " message: " + message + " data:" + data;
    }
}
