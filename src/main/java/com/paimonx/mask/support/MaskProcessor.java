package com.paimonx.mask.support;

import com.paimonx.mask.MaskManager;

/**
 * true-mask / false-notMask
 * @author xu
 * @date 2022/3/17
 */
public interface MaskProcessor {
    /**
     * 前置处理
     * 存在 false 则不进行mask
     * @param maskManager 管理器
     * @return mask 与否
     */
    default boolean beforeMatchRule(MaskManager maskManager) {
        return true;
    }


    /**
     *后置处理
     * 全为 false 则不进行mask
     * @param maskManager 管理器
     * @return mask 与否
     */
    default boolean afterMatchRule(MaskManager maskManager) {
        return true;
    }

}
