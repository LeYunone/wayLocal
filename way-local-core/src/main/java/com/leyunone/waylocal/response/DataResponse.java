package com.leyunone.waylocal.response;

import lombok.*;

/**
 * @author pengli
 * @date  2021-08-10 15:05
 *   一般响应结果集
 */
@Getter
@ToString
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class DataResponse<T> {

    private T data;

    private boolean status;

    private String message;

    private String code;

    public DataResponse() {
    }

    public static <T> DataResponse<T> of(T data) {
        DataResponse<T> response = new DataResponse();
        response.setStatus(true);
        response.setData(data);
        response.setCode(ResponseCode.SUCCESS.getCode());
        response.setMessage(ResponseCode.SUCCESS.getDesc());
        return response;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> DataResponse<T> of(boolean status, ResponseCode responseCode, T data) {
        DataResponse<T> response = new DataResponse();
        response.setStatus(status);
        response.setData(data);
        response.setCode(responseCode.getCode());
        response.setMessage(responseCode.getDesc());
        return response;
    }

    public static DataResponse buildFailure(ResponseCode responseCode) {
        return of(false, responseCode, (Object)null);
    }

    public static DataResponse buildFailure(String message) {
        DataResponse response = of(false, ResponseCode.ERROR, (Object)null);
        response.setMessage(message);
        return response;
    }

    public static DataResponse buildFailure(){
        DataResponse response = new DataResponse();
        response.setStatus(false);
        response.setCode(ResponseCode.ERROR.getCode());
        response.setMessage(ResponseCode.ERROR.getDesc());
        return response;
    }

    public static DataResponse buildSuccess() {
        DataResponse response = new DataResponse();
        response.setStatus(true);
        response.setCode(ResponseCode.SUCCESS.getCode());
        response.setMessage(ResponseCode.SUCCESS.getDesc());
        return response;
    }

}
