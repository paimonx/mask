package com.paimonx.mask.config;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.paimonx.mask.MaskConfigProperties;
import com.paimonx.mask.MaskManager;
import com.paimonx.mask.algorithm.CollectionMaskAlgorithm;
import com.paimonx.mask.spi.MaskAlgorithm;
import com.paimonx.mask.support.MaskProcessor;
import com.paimonx.mask.util.EmptyUtils;
import com.paimonx.mask.util.RequestUtils;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author xu
 * @date 2022/3/17
 */
public abstract class MaskSerializeTemplate extends SimpleBeanPropertyFilter {

    protected MaskManager maskManager;

    protected MaskSerializeTemplate(MaskManager maskManager) {
        this.maskManager = maskManager;
    }


    /**
     * 是否能进行 mock
     *
     * @param clazz 正在序列化的类信息
     * @return true-mask;false-notMask
     */
    protected final boolean canMask(final Class<?> clazz, final String fieldName) {
        // 1全局开关
        MaskConfigProperties maskConfigProperties = maskManager.getMaskConfigProperties();
        if (!maskConfigProperties.isEnabled()) {
            return false;
        }
        // 2 检验web 环境
        HttpServletRequest request = RequestUtils.getRequest();
        // 获取请求uri
        String uri = request.getRequestURI();
        // 3 校验 skipUri
        if (maskConfigProperties.getSkipUri().contains(uri)) {
            return false;
        }
        // 4 规则 匹配前判断   满足其中一个就不进行mask
        if (!beforeMatchRule()) {
            return false;
        }
        // 5规则匹配 由子类实现
        if (!matchRule(maskConfigProperties, uri, clazz, fieldName)) {
            return false;
        }
        // 6 规则匹配后在修改  原本需要 mask，在满足全部条件后，便不再需要进行mask了
        return afterMatchRule();
    }


    /**
     * 存在一个为 false 就不进行false
     *
     * @return boolean
     */
    private boolean beforeMatchRule() {
        List<MaskProcessor> maskProcessors = maskManager.getMaskProcessors();
        for (MaskProcessor maskProcessor : maskProcessors) {
            boolean b = maskProcessor.beforeMatchRule(maskManager);
            if (!b) {
                return false;
            }
        }
        return true;
    }

    /**
     * 全部为 false 就不进行了
     *
     * @return boolean
     */
    private boolean afterMatchRule() {
        List<MaskProcessor> maskProcessors = maskManager.getMaskProcessors();
        if (EmptyUtils.isEmpty(maskProcessors)){
            return true;
        }
        for (MaskProcessor maskProcessor : maskProcessors) {
            boolean b = maskProcessor.afterMatchRule(maskManager);
            if (b) {
                return true;
            }
        }
        return false;
    }

    /**
     * 匹配规则
     * 具体如何匹配 由子类决定
     *
     * @param maskConfigProperties 全局配置信息
     * @param uri                  当前线程的uri
     * @param clazz                正在序列化的类信息
     * @param fieldName            fieldName
     * @return true-mask; false-noMask
     */
    protected abstract boolean matchRule(MaskConfigProperties maskConfigProperties, String uri, Class<?> clazz, String fieldName);


    protected final String doMask(final String fieldName, final Object value, final Class<?> clazz) {
        String maskType = getType(maskManager.getMaskConfigProperties(), RequestUtils.getRequest().getRequestURI(), fieldName, clazz);
        Assert.notNull(maskType, "maskType is null");
        MaskAlgorithm algorithm;
        if (maskType.startsWith(CollectionMaskAlgorithm.LABEL)) {
            maskType = maskType.substring(1);
            algorithm = MaskManager.MASK_ALGORITHM.get(maskType);
            Assert.notNull(algorithm, "未找到type为" + maskType + "实现");
            algorithm = new CollectionMaskAlgorithm(algorithm);
        } else {
            algorithm = MaskManager.MASK_ALGORITHM.get(maskType);
            Assert.notNull(algorithm, "未找到type为" + maskType + "实现");
        }
        return algorithm.encrypt(value);

    }

    /**
     * 获取配置的 maskType
     *
     * @param maskConfigProperties 全局配置
     * @param uri                  请求uri
     * @param fieldName            fieldName
     * @param clazz                类信息
     * @return maskType
     */
    protected abstract String getType(MaskConfigProperties maskConfigProperties, String uri, String fieldName, Class<?> clazz);


}
