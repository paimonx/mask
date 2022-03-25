package com.paimonx.mask.algorithm;

import com.paimonx.mask.spi.MaskAlgorithm;

/**
 * @author xu
 * @date 2022/3/21
 */
public class UniversalMaskAlgorithm implements MaskAlgorithm {
    @Override
    public String type() {
        return "common";
    }

    @Override
    public Object encrypt(Object plaintext) {
        return "***";
    }
}
