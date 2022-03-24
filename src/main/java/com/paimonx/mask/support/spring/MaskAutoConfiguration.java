package com.paimonx.mask.support.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paimonx.mask.MaskConfigProperties;
import com.paimonx.mask.MaskManager;
import com.paimonx.mask.config.advice.MaskResponseBodyHandler;
import com.paimonx.mask.support.TrustIpMaskProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

/**
 * @author xu
 * @date 2022/3/17
 */
@Configuration
@ConditionalOnProperty(name = "spring.api.mask.config.enabled",matchIfMissing = true)
@EnableConfigurationProperties(MaskConfigProperties.class)
@AutoConfigureBefore(AbstractJackson2HttpMessageConverter.class)
@ConditionalOnClass(ObjectMapper.class)
@Import({MaskWebMvcConfigurer.class, MaskResponseBodyHandler.class})
public class MaskAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public MaskManager maskManager(MaskConfigProperties maskConfigProperties){
        TrustIpMaskProcessor trustIpMaskProcessor = new TrustIpMaskProcessor();
        return new MaskManager(maskConfigProperties,trustIpMaskProcessor);
    }
}

