package com.paimonx.mask.util;

import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xu
 * @date 2022/3/18
 */
public class RequestUtils {


    /**
     * 检测当前环境是否为 web 环境
     * 并返回请求
     */

    public static HttpServletRequest getRequest() {
        // 获取 当前 uri
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Assert.notNull(requestAttributes, "当前环境并非为web环境");
        return requestAttributes.getRequest();
    }
}
