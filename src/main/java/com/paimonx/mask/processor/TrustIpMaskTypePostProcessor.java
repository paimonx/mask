package com.paimonx.mask.processor;

import com.paimonx.mask.MaskConfigProperties;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author xu
 * @date 2022/3/31
 */
public class TrustIpMaskTypePostProcessor implements MaskTypePostProcessor {

    @Override
    public String webPostProcessor(MaskConfigProperties maskConfigProperties, RequestAttributes requestAttributes, String maskType) {
        String requestIp = ((ServletRequestAttributes) requestAttributes).getRequest().getRemoteAddr();
        return maskConfigProperties.getTrustIp().contains(requestIp) ? null : maskType;
    }

}
