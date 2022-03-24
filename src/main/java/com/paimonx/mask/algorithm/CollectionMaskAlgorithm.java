package com.paimonx.mask.algorithm;

import com.paimonx.mask.spi.MaskAlgorithm;

import java.util.*;

/**
 * 为了兼容集合类型 List<String>而做的实现
 * 不通过 spi 实例化 ,不是单例
 * *name
 *
 * @author xu
 * @date 2022/3/22
 */
public final class CollectionMaskAlgorithm implements MaskAlgorithm {

    public static final String LABEL = "*";

    private final MaskAlgorithm maskAlgorithm;

    public CollectionMaskAlgorithm(MaskAlgorithm maskAlgorithm) {
        this.maskAlgorithm = maskAlgorithm;
    }

    @Override
    public String type() {
        return LABEL;
    }

    @Override
    public String encrypt(Object plaintext) {
        final Collection plainList;
        if (plaintext.getClass().isArray()) {
            Object[] aa = (Object[]) plaintext;
             plainList = Arrays.asList(aa);
        }else if (plaintext instanceof Collection){
             plainList = (Collection) plaintext;
        }else {
            throw new RuntimeException("错误类型");
        }
        StringBuilder builder = new StringBuilder("[");
        for (Object o : plainList) {
            builder.append("\"");
            String encrypt = maskAlgorithm.encrypt(o);
            builder.append(encrypt);
            builder.append("\",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("]");
        return builder.toString();
    }

}
