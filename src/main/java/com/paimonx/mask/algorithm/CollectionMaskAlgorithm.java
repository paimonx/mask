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
        return LABEL+maskAlgorithm.type();
    }

    @Override
    public Object encrypt(Object plaintext) {
        final Collection plainList;
        if (plaintext.getClass().isArray()) {
            plainList = Arrays.asList((Object[]) plaintext);
        } else if (plaintext instanceof Collection) {
            plainList = (Collection) plaintext;
        } else {
            throw new RuntimeException("错误类型");
        }
        List<Object> list = new ArrayList<>();
        for (Object o : plainList) {
            list.add(maskAlgorithm.encrypt(o));
        }
        return list;
    }

}
