package org.daijing.big.ticket.vo;

/**
 * Created by fengll on 17/1/3.
 */
public class ResponseData {

    private int code;

    private String msg;

    private Object data;

    /**
     * 获取失败的返回对象
     * @param msg
     * @return
     */
    public static ResponseData getFailedResponse(String msg)
    {
        ResponseData responseData =  new ResponseData();
        responseData.setCode(300);
        responseData.setMsg(msg);
        return responseData;
    }

    /**
     * 获取失败的返回对象
     * @param msg
     * @return
     */
    public static ResponseData getResponse(int code,String msg)
    {
        ResponseData responseData =  new ResponseData();
        responseData.setCode(code);
        responseData.setMsg(msg);
        return responseData;
    }

    /**
     * 获取成功的返回对象
     * @param data
     * @return
     */
    public static ResponseData getSuccessResponse(Object data)
    {
        ResponseData responseData =  new ResponseData();
        responseData.setCode(200);
        responseData.setData(data);
        return responseData;
    }

    /**
     * 获取成功的返回对象
     * @param data data
     * @param msg msg
     * @return returnVal
     */
    public static ResponseData getSuccessResponse(Object data, String msg)
    {
        ResponseData responseData =  new ResponseData();
        responseData.setCode(200);
        responseData.setData(data);
        responseData.setMsg(msg);
        return responseData;
    }

    public static ResponseData getNoUserResponse(Object data, String msg)
    {
        ResponseData responseData =  new ResponseData();
        responseData.setCode(403);
        responseData.setData(data);
        responseData.setMsg(msg);
        return responseData;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
