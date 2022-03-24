package com.paimonx.mask.util;

import java.util.Collection;
import java.util.Map;

/**
 * @author xu
 * @date 2022/3/17
 */
public class EmptyUtils {

    private EmptyUtils() {
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null || "null".equals(obj.toString()) || "".equals(obj.toString())) {
            return true;
        }

        if (obj instanceof String) {
            return ((String) obj).trim().length() == 0;
        }

        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }

        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }

        return false;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }
}
