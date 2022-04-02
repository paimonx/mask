package com.paimonx.mask.config.fastjson;

import com.alibaba.fastjson.serializer.ValueFilter;
import com.paimonx.mask.MaskManager;
import com.paimonx.mask.config.jackson.MaskBeanPropertyFilter;
import com.paimonx.mask.util.EmptyUtils;

/**
 * @author xu
 * @date 2022/3/23
 */
public class MaskFastjsonValueFilter extends MaskBeanPropertyFilter implements ValueFilter {

    protected MaskFastjsonValueFilter(MaskManager maskManager) {
        super(maskManager);
    }

    // TODO: 2022/3/23
    @Override
    public Object process(Object object, String name, Object value) {
        String maskType = getMaskType(object.getClass(), name, null);
        if (EmptyUtils.isNotEmpty(maskType)){
            return doMask(value,maskType);
        }
        return value;
    }
}
