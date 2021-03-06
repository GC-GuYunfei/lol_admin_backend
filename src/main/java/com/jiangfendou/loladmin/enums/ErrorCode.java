package com.jiangfendou.loladmin.enums;

/**
 * Error code.
 *
 * @author lin.duan
 */
public enum ErrorCode {
    //common
    SYSTEM_ERROR("e.jfd.1001", "系统异常"),
    ACCOUNT_PASSWORD_ERROR("e.jfd.1002", "账户或者密码输入错误"),
    CODE_ERROR("e.jfd.1003", "验证码输入错误"),
    INVALID_TOKEN("e.jfd.1004", "无效的token"),
    EXPIRED_TOKEN("e.jfd.1005", "token已过期"),
    NOT_LOGIN_ERROR("e.jfd.1006", "对不起您尚未登录"),
    NO_ACCESS_ALLOWED_ERROR("e.jfd.1007", "不允许访问"),
    NOT_FOUND("e.jfd.1008", "找不到目标数据");

    /** error code. */
    private final String code;
    /** error message. */
    private final String message;

    /**
     * Constructor.
     *
     * @param code code of error code
     */
    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
