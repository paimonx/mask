package com.paimonx.mask.config.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.paimonx.mask.support.PropertyKeyConst;
import com.paimonx.mask.util.EmptyUtils;

/**
 * @author xu
 * @date 2022/3/17
 */
public class MaskBeanSerializerModifier extends BeanSerializerModifier {

    @Override
    public BeanSerializerBuilder updateBuilder(SerializationConfig config, BeanDescription beanDesc, BeanSerializerBuilder builder) {
        // 对没有filter的builder添加 mask默认的filter
        if (EmptyUtils.isEmpty(builder.getFilterId())){
            builder.setFilterId(PropertyKeyConst.DEFAULT_MASK_FILTER_ID);
        }
        return super.updateBuilder(config, beanDesc, builder);
    }
}
