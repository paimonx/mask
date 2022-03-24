package com.paimonx.mask.support;

import com.paimonx.mask.MaskManager;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author xu
 * @date 2022/3/17
 */
public class TrustIpMaskProcessor implements MaskProcessor{
    /**
     *  这是一个 折中的实现
     *  我认为 所有的 微服务都是部署在内网环境中，只有网关对外露，故 我认为 服务之间是可信的
     *  在项目运行过程中，序列化会十分频繁， 每一次都访问注册中心获取其来源可信，这样的网络消耗相较于序列化本身 是十分巨大的
     *  即使在项目初始化时，访问注册中心将可信列表缓存到本地，则只能获取到那一时刻的列表，对于后续的无法感知。
     *
     *  如果 在你的项目中可以通过其他方式来校验请求来源（比如header），那么 请替换掉该实现
     *
     */
    @Override
    public boolean beforeMatchRule(MaskManager maskManager) {
        // 获取 当前 ip
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // 断言当前环境 为web 环境
        Assert.notNull(requestAttributes,"当前环境异常，未获取到request");
        String remoteAddr = requestAttributes.getRequest().getRemoteAddr();

        return !maskManager.getMaskConfigProperties().getTrustIp().contains(remoteAddr);
    }

}
