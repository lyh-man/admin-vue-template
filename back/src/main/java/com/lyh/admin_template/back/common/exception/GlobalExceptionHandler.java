package com.lyh.admin_template.back.common.exception;

import com.lyh.admin_template.back.common.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理类。
 * 使用 slf4j 保存日志信息。
 * 此处使用了 统一结果处理 类 Result 用于包装异常信息。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 处理 Exception 异常
     * @param e 异常
     * @return 处理结果
     */
    @ExceptionHandler(Exception.class)
    public Result handlerException(Exception e) {
        logger.error(e.getMessage(), e);
        return Result.error().message("系统异常");
    }

    /**
     * 处理空指针异常
     * @param e 异常
     * @return 处理结果
     */
    @ExceptionHandler(NullPointerException.class)
    public Result handlerNullPointerException(NullPointerException e) {
        logger.error(e.getMessage(), e);
        return Result.error().message("空指针异常");
    }

    /**
     * 处理自定义异常
     * @param e 异常
     * @return 处理结果
     */
    @ExceptionHandler(GlobalException.class)
    public Result handlerGlobalException(GlobalException e) {
        logger.error(e.getMessage(), e);
        return Result.error().message(e.getMessage()).code(e.getCode());
    }

    /**
     * 处理 JSR303 校验的异常
     * @param e 异常
     * @return 处理结果
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handlerValidException(MethodArgumentNotValidException e) {
        logger.error(e.getMessage(), e);
        BindingResult result = e.getBindingResult();
        Map<String, String> map = new HashMap<>();
        // 获取校验结果，遍历获取捕获到的每个校验结果
        result.getFieldErrors().forEach(item ->{
            // 存储得到的校验结果
            map.put(item.getField(), item.getDefaultMessage());
        });
        return Result.error().message("数据校验不合法").data("items", map);
    }
}
