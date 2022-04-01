package com.paimonx.mask.config.advice;

import com.paimonx.mask.MaskManager;
import com.paimonx.mask.config.MaskSerializeTemplate;
import com.paimonx.mask.util.EmptyUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * 对基础类型的补充
 *
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
        return maskManager.getMaskConfigProperties().isEnabled();
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 一定不会为空
        String uri = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getRequestURI();
        String maskType = getMaskType(null, null, uri);
        return EmptyUtils.isNotEmpty(maskType) ? doMask(body, maskType) : body;
    }

    @Override
    protected String doGetMaskType(Class<?> clazz, String fieldName, String uri) {
        return maskManager.getMaskConfigProperties().getUriType().get(uri);
    }

}
