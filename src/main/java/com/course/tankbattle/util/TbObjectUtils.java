package com.course.tankbattle.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public final class TbObjectUtils {
    private TbObjectUtils() {
    }

    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof CharSequence) {
            return ((CharSequence) object).length() == 0;
        }
        if (object instanceof Collection) {
            return ((Collection<?>) object).isEmpty();
        }
        if (object instanceof Map) {
            return ((Map<?, ?>) object).isEmpty();
        }
        return object.getClass().isArray() && Array.getLength(object) == 0;
    }

    public static boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }
}
