package com.netty.netty.vo;

import lombok.Data;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author cxks
 * @Date 2021/8/23 0:52
 */
public enum MethodType {
    TEST(0, "test")
    ;

    private int value;
    private String methodName;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    MethodType(int i, String test) {
    }

    public static MethodType getMethodType(int num){
        switch (num){
            case 0:
                return MethodType.TEST;
        }
        return null;
    }
}
