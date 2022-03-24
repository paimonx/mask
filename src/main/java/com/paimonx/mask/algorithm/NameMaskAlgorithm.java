package com.paimonx.mask.algorithm;

import com.paimonx.mask.spi.MaskAlgorithm;
import com.paimonx.mask.support.InitIal;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * @author xu
 * @date 2022/3/18
 */
public class NameMaskAlgorithm implements MaskAlgorithm, InitIal {

    private static final Set<String> COMPOUND_SURNAME = new HashSet<>();

    private static final int COMPOUND = 2;

    private static final String SPECIAL_IDENTIFICATION = "·";

    private static final String SUFFIX = "**";


    /**
     * 值显示前几位姓或名 ，对后面的进行遮掩
     *
     * @param plaintext 明文
     * @return ciphertext
     */
    @Override
    public String encrypt(Object plaintext) {
        if (plaintext instanceof String) {
            String name = (String) plaintext;
            // 按 · 切割
            String processName = name.split(SPECIAL_IDENTIFICATION)[0];
            // 不包含
            if (name.equals(processName)) {
                if (name.length() > COMPOUND) {
                    processName = name.substring(0, COMPOUND);
                    // 是否复姓
                    if (COMPOUND_SURNAME.contains(processName)) {
                        return processName + SUFFIX;
                    }
                }
                return name.charAt(0) + SUFFIX;
            } else {
                return processName + SPECIAL_IDENTIFICATION + SUFFIX;
            }
        }
        throw new RuntimeException("错误类型");
    }

    @Override
    public void init(Properties properties) {
        String nameProperties = properties.getProperty(type(), "");
        COMPOUND_SURNAME.addAll(Arrays.asList(nameProperties.split(";")));
    }

}
