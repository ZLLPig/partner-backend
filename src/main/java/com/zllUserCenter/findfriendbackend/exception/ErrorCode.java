package com.zllUserCenter.findfriendbackend.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    SUCCESS(0, "ok",""),
    PARAMS_ERROR(40000, "请求参数错误",""),
    NOT_LOGIN(40100, "未登录",""),
    NULL_ERROR(40001, "请求数据为空",""),
    NO_AUTH(40101, "无权限",""),
    NOT_FOUND_ERROR(40400, "请求数据不存在",""),
    FORBIDDEN_ERROR(40300, "禁止访问",""),
    SYSTEM_ERROR(50000, "系统内部异常",""),
    OPERATION_ERROR(50001, "操作失败","");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    /**
     * 描述
     */
    private final String description;

    ErrorCode(int code, String message,String description) {
        this.code = code;
        this.description = description;
        this.message = message;
    }

}
