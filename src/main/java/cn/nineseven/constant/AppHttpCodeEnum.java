package cn.nineseven.constant;

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"操作成功"),
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    CODE_FALSE(402, "验证码错误"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    CLOCK_TIMEOUT(405, "打卡时长超过六小时，打卡无效！"),
    FILE_TYPE_ERROR(406, "文件类型错误"),

    EMPTY_PARAMS_ERROR(407, "参数不能为空"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(411,"用户名已存在"),
    PHONENUMBER_EXIST(412,"手机号已存在"),
    EMAIL_EXIST(413, "邮箱已存在"),
    REQUIRE_USERNAME(414, "必需填写用户名"),
    LOGIN_ERROR(415,"用户名或密码错误"),
    CODE_SEND_ERROR(416, "验证码发送失败"),
    DIFFERENT_PASSWORD(417, "两次密码不一致"),
    FILE_EXIST(408, "文件已存在"),

    FILE_SIZE_ERROR(409, "文件过大"),
    ANALYSE_ERROR(510, "分析出现错误，请稍后重试"),

    ;
    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}