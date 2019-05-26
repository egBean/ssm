package cn.eg.Response;

import java.util.HashMap;
import java.util.Map;

public class ResponseData {

    private final int SUCCESS=1;
    private final int FAIL=0;

    private int code ;
    private String message;
    private final Map<String,Object> data = new HashMap<>();

    private ResponseData(int code,String message){
        this.code=code;
        this.message=message;
    }

    public static ResponseData success(){
        return  new ResponseData(1,null);
    }

    public static  ResponseData fail(String message){
        return new ResponseData(0,message);
    }



    public void putKeyAndValue(String key,Object value){
        data.put(key,value);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }


}
