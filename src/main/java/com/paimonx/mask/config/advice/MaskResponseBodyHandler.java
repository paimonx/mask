package com.paimonx.mask.config.advice;

import com.paimonx.mask.MaskConfigProperties;
import com.paimonx.mask.MaskManager;
import com.paimonx.mask.config.MaskSerializeTemplate;
import com.paimonx.mask.util.RequestUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 对基础类型的补充
 * @author xu
 * @date 2022/3/18
 */
@RestControllerAdvice
public class MaskResponseBodyHandler extends MaskSerializeTemplate implements ResponseBodyAdvice<Object> {


    protected MaskResponseBodyHandler(MaskManager maskManager) {
        super(maskManager);
    }


    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return canMask(null,null);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        return doMask(body, null,null, RequestUtils.getRequest().getRequestURI());
    }

    @Override
    protected boolean matchRule(MaskConfigProperties maskConfigProperties, String uri, Class<?> clazz, String fieldName) {
        return maskConfigProperties.getUriType().containsKey(uri);
    }

    @Override
    protected String getType(MaskConfigProperties maskConfigProperties, String uri, String fieldName, Class<?> clazz) {
        return maskConfigProperties.getUriType().get(uri);
    }
}
