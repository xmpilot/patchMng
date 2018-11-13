package com.patchmng.utils;

import java.util.HashMap;
import java.util.Map;

public class RestResult {
    private Map<String, Object> data = new HashMap<>();

    public static int SUCCESS_CODE = 0;
    public static int ERROR_CODE = 5000;

    private int code;
    private String message;

    /**
     * 用于函数处理中间结果
     */
    private Object returnValue;

    public static RestResult create(){
        RestResult restResult = new RestResult();
        restResult.addStatus(true, SUCCESS_CODE,"");
        return restResult;
    }

    private boolean success;

    public void addStatus(boolean success){
        this.success = success;
    }

    public void addStatus(boolean success, int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public void addError(String messsage){
        addStatus(false, ERROR_CODE, messsage);
    }

    public void addSuccess(String messsage){
        addStatus(false, ERROR_CODE, messsage);
    }

    public void setSuccess(){
        addStatus(true, SUCCESS_CODE,"");
    }

    public void addData(String name, Object value){
        data.put(name, value);
    }

    public Map<String, Object> getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 返回到web前端使用
     * @return
     */
    public Map<String, Object> outResult(){
        Map<String, Object> result = new HashMap<>();
        result.put("code", code);
        result.put("message", message);
        result.put("data", data);
        return result;
    }



}
