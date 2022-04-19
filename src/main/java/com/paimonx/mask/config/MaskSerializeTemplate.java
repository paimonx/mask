package com.paimonx.mask.config;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.paimonx.mask.MaskConfigProperties;
import com.paimonx.mask.MaskManager;
import com.paimonx.mask.processor.MaskTypePostProcessor;
import com.paimonx.mask.spi.MaskAlgorithm;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

/**
 * @author xu
 * @date 2022/3/17
 */
public abstract class MaskSerializeTemplate extends SimpleBeanPropertyFilter {

    protected MaskManager maskManager;

    protected MaskSerializeTemplate(MaskManager maskManager) {
        this.maskManager = maskManager;
    }


    protected final String getMaskType(final Class<?> clazz, final String fieldName, final String uri) {
        // 获取 相应的type
        String maskType = doGetMaskType(clazz, fieldName, uri);
        if (null == maskType) {
            return null;
        }
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        MaskConfigProperties maskConfigProperties = maskManager.getMaskConfigProperties();
        // 如果当前正在进行的是class模式 并且该请求已经被 uri模式处理过，就不再进行处理
        if (null != clazz && doChooseUriOrClass(maskConfigProperties, requestAttributes)) {
            return null;
        }
        // 一些后置处理
        maskType = doWebPostProcessor(maskConfigProperties, requestAttributes, maskType);
        return maskType;
    }

    protected boolean doChooseUriOrClass(MaskConfigProperties maskConfigProperties, RequestAttributes requestAttributes) {
        Assert.notNull(requestAttributes, "当前非web环境");
        return maskConfigProperties.getUriType().containsKey(((ServletRequestAttributes) requestAttributes).getRequest().getRequestURI());
    }

    protected String doWebPostProcessor(MaskConfigProperties maskConfigProperties, RequestAttributes requestAttributes, String maskType) {
        List<MaskTypePostProcessor> maskTypePostProcessors = maskManager.getMaskTypePostProcessors();
        for (MaskTypePostProcessor maskTypePostProcessor : maskTypePostProcessors) {
            maskType = maskTypePostProcessor.webPostProcessor(maskConfigProperties, requestAttributes, maskType);
            if (null == maskType) {
                return null;
            }
        }
        return maskType;
    }

    protected abstract String doGetMaskType(final Class<?> clazz, final String fieldName, final String uri);


    protected final Object doMask(final Object value, final String maskType) {
        final MaskAlgorithm algorithm = MaskManager.MASK_ALGORITHM.get(maskType);
        return algorithm.encrypt(value);
    }

}
