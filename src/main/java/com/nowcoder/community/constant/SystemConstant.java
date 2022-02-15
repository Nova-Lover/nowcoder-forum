package com.nowcoder.community.constant;

/**
 * 系统常量枚举类
 * @author Alex
 * @version 1.0
 * @date 2022/2/15 14:59
 */
public enum SystemConstant {

    SYSTEM_USER_ID(1,"系统用户ID");

    private int code;
    private String desc;

    SystemConstant(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
