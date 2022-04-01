package com.paimonx.mask.config.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.paimonx.mask.MaskConfigProperties;
import com.paimonx.mask.MaskManager;
import com.paimonx.mask.config.MaskSerializeTemplate;
import com.paimonx.mask.util.EmptyUtils;

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
        String maskType = getMaskType(clazz, fieldName,null);
        if (EmptyUtils.isNotEmpty(maskType)) {
            // 当前值
            Object value = writer.getMember().getValue(pojo);
            jgen.writeObjectField(fieldName, doMask(value,maskType));
        } else {
            super.serializeAsField(pojo, jgen, provider, writer);
        }

    }

    @Override
    protected String doGetMaskType(Class<?> clazz, String fieldName, String uri) {
        MaskConfigProperties maskConfigProperties = maskManager.getMaskConfigProperties();
        return maskConfigProperties.getClassDefinitions().get(clazz.getName()).get(fieldName);
    }


}
