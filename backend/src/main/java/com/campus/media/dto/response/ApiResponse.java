package com.campus.media.dto.response;

import lombok.Data;

@Data
public class ApiResponse<T> {
    
    private Integer code;
    
    private String message;
    
    private T data;
    
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("success");
        response.setData(data);
        return response;
    }
    
    public static <T> ApiResponse<T> success() {
        return success(null);
    }
    
    public static <T> ApiResponse<T> error(Integer code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setData(null);
        return response;
    }
    
    public static <T> ApiResponse<T> error(String message) {
        return error(500, message);
    }
    
    public static <T> ApiResponse<T> unauthorized(String message) {
        return error(401, message);
    }
    
    public static <T> ApiResponse<T> forbidden(String message) {
        return error(403, message);
    }
}
