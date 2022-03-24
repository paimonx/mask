package com.paimonx.mask.spi;

import com.paimonx.mask.util.NameUtils;

/**
 * @author xu
 * @date 2022/3/17
 */
public interface MaskAlgorithm {

    /**
     * 算法 唯一标识
     *
     * @return 唯一标识
     */
    default String type() {
        return NameUtils.normalizeMaskAlgorithmName(getClass());
    }

    /**
     * 加密算法
     *
     * @param plaintext 明文
     * @return ciphertext
     */
    String encrypt(Object plaintext);
}
