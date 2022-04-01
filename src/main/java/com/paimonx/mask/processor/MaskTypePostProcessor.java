package com.paimonx.mask.processor;

import com.paimonx.mask.MaskConfigProperties;
import org.springframework.web.context.request.RequestAttributes;

/**
 * @author xu
 * @date 2022/3/31
 */
public interface MaskTypePostProcessor {

    /**
     *  web 对mask的处理
     *
     * @param maskConfigProperties
     * @param requestAttributes
     * @param maskType
     * @return
     */
    default String webPostProcessor(MaskConfigProperties maskConfigProperties, RequestAttributes requestAttributes, String maskType) {
        return maskType;
    }
}
