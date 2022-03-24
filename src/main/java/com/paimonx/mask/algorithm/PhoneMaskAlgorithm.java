package com.paimonx.mask.algorithm;

import com.paimonx.mask.spi.MaskAlgorithm;

/**
 * @author xu
 * @date 2022/3/21
 */
public class PhoneMaskAlgorithm implements MaskAlgorithm {

    /**
     * 中间4位用*代替
     * @param plaintext 明文
     * @return 密文
     */
    @Override
    public String encrypt(Object plaintext) {
        String phone = String.valueOf(plaintext) ;
        return phone.substring(0,3)+"****"+phone.substring(7);
    }
}
