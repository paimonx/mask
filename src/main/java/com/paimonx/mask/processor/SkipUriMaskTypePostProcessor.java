package com.paimonx.mask.processor;

import com.paimonx.mask.MaskConfigProperties;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author xu
 * @date 2022/4/1
 */
public class SkipUriMaskTypePostProcessor implements MaskTypePostProcessor {

    @Override
    public String webPostProcessor(MaskConfigProperties maskConfigProperties, RequestAttributes requestAttributes, String maskType) {
        String uri = ((ServletRequestAttributes) requestAttributes).getRequest().getRequestURI();
        return maskConfigProperties.getSkipUri().contains(uri) ? null : maskType;
    }


}
