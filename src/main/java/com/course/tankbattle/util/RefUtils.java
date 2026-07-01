package com.course.tankbattle.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public final class RefUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(RefUtils.class);

    private RefUtils() {
    }

    public static Object executeByMethodName(Object target, String methodName, Object[] args, Class<?>[] paramTypes) {
        try {
            if (args == null) {
                Method method = target.getClass().getMethod(methodName);
                return method.invoke(target);
            } else {
                Method method = target.getClass().getMethod(methodName, paramTypes);
                return method.invoke(target, args);
            }
        } catch (NoSuchMethodException e) {
            LOGGER.warn("Method not found: {} on {}", methodName, target.getClass().getSimpleName());
        } catch (Exception e) {
            LOGGER.error("Failed to execute method: {}", methodName, e);
        }
        return null;
    }
}
