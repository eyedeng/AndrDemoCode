package com.example.andrdemocode.utils;

import com.example.andrdemocode.base.XLog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtil {
    private static final String TAG = "ReflectUtil";

    /**
     * 对象方法调用
     *
     * @param target 调用目标对象
     * @param returnType 返回类型
     * @param method 方法名称
     * @param parameterTypes 方法参数类型
     * @param values 参数
     * @return 反射调用返回值
     */
    public static <T> T callObjectMethod(Object target,
                                         Class<T> returnType, String method, Class<?>[] parameterTypes, Object... values) {
        if (target == null) {
            return null;
        }
        try {
            Class<? extends Object> clazz = target.getClass();
            return (T) callObjectMethod(target, method, clazz, parameterTypes, values);
        } catch (Exception e) {
            XLog.e(TAG, "Failed to call method:" + method, e);
        }
        return null;
    }

    public static Object callObjectMethod(Object target,
                                          String method, Class<?> clazz, Class<?>[] parameterTypes, Object... values) {
        if (target == null) {
            return null;
        }
        try {
            Method declaredMethod = clazz.getMethod(method, parameterTypes);
            declaredMethod.setAccessible(true);
            return declaredMethod.invoke(target, values);
        } catch (Exception e) {
            XLog.e(TAG, "Failed to call method:" + method, e);
        }
        return null;
    }

}
