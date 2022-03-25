package com.paimonx.mask.algorithm;

import com.paimonx.mask.spi.MaskAlgorithm;

/**
 * @author xu
 * @date 2022/3/21
 */
public class IdNoMaskAlgorithm implements MaskAlgorithm {
    /**
     * 遮掩 前3位和后4位的内容
     * @param plaintext 明文
     * @return 密文
     */
    @Override
    public Object encrypt(Object plaintext) {
        String idNo = String.valueOf(plaintext) ;
        return idNo.substring(0,6)+"******"+idNo.substring(idNo.length()-3);
    }
}
