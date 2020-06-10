package com.lyh.admin_template.back.common.exception;

import lombok.Data;
import org.apache.http.HttpStatus;

/**
 * 自定义异常，
 * 可以自定义 异常信息 message 以及 响应状态码 code（默认为 500）。
 *
 * 依赖信息说明：
 *      此处使用 @Data 注解，需导入 lombok 相关依赖文件。
 *      使用 HttpStatus 的常量表示 响应状态码，需导入 httpcore 相关依赖文件。
 */
@Data
public class GlobalException extends RuntimeException {
    /**
     * 保存异常信息
     */
    private String message;

    /**
     * 保存响应状态码
     */
    private Integer code = HttpStatus.SC_INTERNAL_SERVER_ERROR;

    /**
     * 默认构造方法，根据异常信息 构建一个异常实例对象
     * @param message 异常信息
     */
    public GlobalException(String message) {
        super(message);
        this.message = message;
    }

    /**
     * 根据异常信息、响应状态码构建 一个异常实例对象
     * @param message 异常信息
     * @param code 响应状态码
     */
    public GlobalException(String message, Integer code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    /**
     * 根据异常信息，异常对象构建 一个异常实例对象
     * @param message 异常信息
     * @param e 异常对象
     */
    public GlobalException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    /**
     * 根据异常信息，响应状态码，异常对象构建 一个异常实例对象
     * @param message 异常信息
     * @param code 响应状态码
     * @param e 异常对象
     */
    public GlobalException(String message, Integer code, Throwable e) {
        super(message, e);
        this.message = message;
        this.code = code;
    }
}
