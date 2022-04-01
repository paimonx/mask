package com.paimonx.mask.config.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.paimonx.mask.MaskConfigProperties;
import com.paimonx.mask.support.PropertyKeyConst;
import com.paimonx.mask.util.EmptyUtils;

/**
 * @author xu
 * @date 2022/3/17
 */
public class MaskBeanSerializerModifier extends BeanSerializerModifier {

    private MaskConfigProperties maskConfigProperties;

    public MaskBeanSerializerModifier(MaskConfigProperties maskConfigProperties) {
        this.maskConfigProperties = maskConfigProperties;
    }

    @Override
    public BeanSerializerBuilder updateBuilder(SerializationConfig config, BeanDescription beanDesc, BeanSerializerBuilder builder) {
        /**
         * builder 没有 filterId
         *  全局开关开启
         *  该class有相应配置
         *  对满足所有条件的 builder 添加mask filterId
         */
        if (EmptyUtils.isEmpty(builder.getFilterId()) &&
                maskConfigProperties.isEnabled() &&
                maskConfigProperties.getClassDefinitions().containsKey(beanDesc.getBeanClass().getName())) {
            builder.setFilterId(PropertyKeyConst.DEFAULT_MASK_FILTER_ID);
        }
        return super.updateBuilder(config, beanDesc, builder);
    }
}
