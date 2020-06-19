package com.lyh.admin_template.back.common.utils;

import com.google.gson.Gson;

/**
 * Gson 工具类，用于 Object 与 Json 字符串形式互转
 */
public class GsonUtil {
    private final static Gson GSON = new Gson();

    /**
     * Object 转 String 数据（JSON 字符串）
     */
    public static String toJson(Object object) {
        if (object instanceof Integer || object instanceof Short || object instanceof Byte
            || object instanceof Long || object instanceof Character || object instanceof Boolean
            || object instanceof Double || object instanceof String || object instanceof Float) {
            return String.valueOf(object);
        }
        return GSON.toJson(object);
    }

    /**
     * string（Json 字符串） 转 Object。
     */
    public static <T> T fromJson(String json, Class<T> tClass) {
        return GSON.fromJson(json, tClass);
    }
}
