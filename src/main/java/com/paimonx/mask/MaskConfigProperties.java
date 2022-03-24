package com.paimonx.mask;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author xu
 * @date 2022/3/17
 */
@ConfigurationProperties(prefix = MaskConfigProperties.PREFIX)
@Data
public class MaskConfigProperties {

    public static final String PREFIX = "spring.api.mask.config";

    /**
     * 全局开关
     */
    private boolean enabled = true;

    /**
     * 默认对所有uri 进行处理
     * 故 设置 不进行处理的uri
     */

    private Set<String> skipUri = new HashSet<>();
    /**
     * 我认为 微服务间的 访问是可信的；
     * 信任的ip 需要手动进行配置
     * 如果每次序列化时，都去请求注册中心，这个网络通信是不值得的
     * 哪怕是开始时请求一次，并缓存下来，也仅仅是那一时刻的服务列表
     * 在后面有新的服务加入时，依然无法识别其为 可信ip，故采取手动配置的方式
     */
    private Set<String> trustIp =new HashSet<>();

    /**
     * 算法初始化信息（该信息不会实时改动）
     */
    private Map<String, Object> algorithmMetadata = new HashMap<>();

    /**
     * 支持 uri 模式
     * 为了解决那些 不进行bean 序列化的对象
     */
    private Map<String, String> uriType = new HashMap<>();

    /**
     * 类 对应关系
     */
    private Map<String, Map<String, String>> classDefinitions = new HashMap<>();


}
