package com.paimonx.mask.config.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.paimonx.mask.MaskConfigProperties;
import com.paimonx.mask.MaskManager;
import com.paimonx.mask.config.MaskSerializeTemplate;

/**
 * @author xu
 * @date 2022/3/17
 */
public class MaskBeanPropertyFilter extends MaskSerializeTemplate {


    public MaskBeanPropertyFilter(MaskManager maskManager) {
        super(maskManager);
    }


    @Override
    public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer) throws Exception {
        // 当前key
        String fieldName = writer.getName();
        // 当前序列化的pojo class
        Class<?> clazz = pojo.getClass();
        if (canMask(clazz, fieldName)) {
            // 当前值
            Object value = writer.getMember().getValue(pojo);
            jgen.writeObjectField(fieldName, doMask(value, clazz,fieldName,null));
        } else {
            super.serializeAsField(pojo, jgen, provider, writer);
        }

    }

    @Override
    protected boolean matchRule(MaskConfigProperties maskConfigProperties, String uri, Class<?> clazz, String fieldName) {
        String className = clazz.getName();
        return (!(maskConfigProperties.getUriType().containsKey(uri))) &&
                (maskConfigProperties.getClassDefinitions().containsKey(className)) &&
                maskConfigProperties.getClassDefinitions().get(className).containsKey(fieldName);
    }

    @Override
    protected String getType(MaskConfigProperties maskConfigProperties, String uri, String fieldName, Class<?> clazz) {
        return maskConfigProperties.getClassDefinitions().get(clazz.getName()).get(fieldName);
    }

}
