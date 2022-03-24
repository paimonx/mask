package com.paimonx.mask.util;

import com.paimonx.mask.spi.MaskAlgorithm;
import com.paimonx.mask.support.PropertyKeyConst;

import java.util.Locale;

/**
 * @author xu
 * @date 2022/3/17
 */
public class NameUtils {

    public static final int SUFFIX_LENGTH = PropertyKeyConst.MASK_ALGORITHM.length();

    private NameUtils() {
    }

    public static String normalizeMaskAlgorithmName(Class<? extends MaskAlgorithm> clazz) {
        String simpleName = clazz.getSimpleName();
        return simpleName.substring(0, simpleName.length() - SUFFIX_LENGTH).toLowerCase(Locale.ROOT);
    }
}
