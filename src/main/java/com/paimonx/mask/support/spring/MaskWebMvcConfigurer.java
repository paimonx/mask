package com.paimonx.mask.support.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.paimonx.mask.MaskManager;
import com.paimonx.mask.config.jackson.MaskBeanPropertyFilter;
import com.paimonx.mask.config.jackson.MaskBeanSerializerModifier;
import com.paimonx.mask.support.PropertyKeyConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author xu
 * @date 2022/3/17
 */
@AutoConfigureBefore(MaskManager.class)
@EnableWebMvc
@Configuration
public class MaskWebMvcConfigurer implements WebMvcConfigurer {
    @Autowired
    private MaskManager maskManager;


    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        converters.forEach(c -> {
            Class<AbstractJackson2HttpMessageConverter> clazz = AbstractJackson2HttpMessageConverter.class;
            if (clazz.isAssignableFrom(c.getClass())) {
                AbstractJackson2HttpMessageConverter httpMessageConverter = clazz.cast(c);
                ObjectMapper objectMapper = httpMessageConverter.getObjectMapper();
                // 添加 module
                SimpleModule simpleModule = new SimpleModule();
                simpleModule.setSerializerModifier(new MaskBeanSerializerModifier());
                objectMapper.registerModule(simpleModule);

                // 添加 filter
                SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();
                simpleFilterProvider.addFilter(PropertyKeyConst.DEFAULT_MASK_FILTER_ID,new MaskBeanPropertyFilter(maskManager));
                objectMapper.setFilterProvider(simpleFilterProvider);
            }
        });
    }
}