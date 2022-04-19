package com.paimonx.mask;

import com.paimonx.mask.algorithm.CollectionMaskAlgorithm;
import com.paimonx.mask.processor.MaskTypePostProcessor;
import com.paimonx.mask.spi.MaskAlgorithm;
import com.paimonx.mask.spi.MaskServiceLoader;
import com.paimonx.mask.support.InitIal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @author xu
 * @date 2022/3/17
 */
public class MaskManager {

    private Logger log = LoggerFactory.getLogger(MaskManager.class);

    private final MaskConfigProperties maskConfigProperties;

    private final static Map<String, MaskAlgorithm> SPI_MASK_ALGORITHM = new HashMap<>(2 << 3);

    public final static Map<String, MaskAlgorithm> MASK_ALGORITHM = new HashMap<>(2 << 4);

    private final List<MaskTypePostProcessor> maskTypePostProcessors = new ArrayList<>();


    public MaskManager(MaskConfigProperties maskConfigProperties, MaskTypePostProcessor... maskTypePostProcessors) {
        this(maskConfigProperties, Arrays.asList(maskTypePostProcessors));
    }

    public MaskManager(MaskConfigProperties maskConfigProperties, Collection<MaskTypePostProcessor> maskTypePostProcessors) {
        this.maskConfigProperties = maskConfigProperties;
        this.maskTypePostProcessors.addAll(maskTypePostProcessors);
        init(maskConfigProperties);
    }

    public void addMaskTypePostProcessors(MaskTypePostProcessor maskTypePostProcessor) {
        this.maskTypePostProcessors.add(maskTypePostProcessor);
    }

    public List<MaskTypePostProcessor> getMaskTypePostProcessors() {
        return maskTypePostProcessors;
    }

    public MaskConfigProperties getMaskConfigProperties() {
        return maskConfigProperties;
    }

    private void init(MaskConfigProperties maskConfigProperties) {
        Properties properties = new Properties();
        maskConfigProperties.getAlgorithmMetadata().forEach(properties::putIfAbsent);
        // spi 加载算法
        initMaskAlgorithm(properties);
        // 拓展集合兼容
        MASK_ALGORITHM.putAll(SPI_MASK_ALGORITHM);
        expandMaskAlgorithm(maskConfigProperties);
    }

    private void expandMaskAlgorithm(MaskConfigProperties maskConfigProperties) {
        Map<String, String> uriType = maskConfigProperties.getUriType();
        for (String uriMaskType : uriType.values()) {
            addMaskAlgorithm(uriMaskType);

        }

        Map<String, Map<String, String>> classDefinitions = maskConfigProperties.getClassDefinitions();
        for (Map<String, String> classDefinition : classDefinitions.values()) {
            for (String classMaskType : classDefinition.values()) {
                addMaskAlgorithm(classMaskType);
            }
        }

    }

    private void addMaskAlgorithm(String maskType) {
        if (!MASK_ALGORITHM.containsKey(maskType)) {
            if (maskType.startsWith(CollectionMaskAlgorithm.LABEL)) {
                String specificMaskType = maskType.substring(1);
                MaskAlgorithm specificAlgorithm = SPI_MASK_ALGORITHM.get(specificMaskType);
                Assert.notNull(specificAlgorithm, "未找到type为:" + maskType + "的实现");
                MASK_ALGORITHM.put(maskType, new CollectionMaskAlgorithm(specificAlgorithm));
            } else {
                throw new RuntimeException("未找到type为:" + maskType + "的实现");
            }
        }
    }

    private void initMaskAlgorithm(Properties properties) {
        // 算法实例化
        MaskServiceLoader.register(MaskAlgorithm.class);
        // 初始化
        Collection<MaskAlgorithm> maskAlgorithmCollection = MaskServiceLoader.getSingletonServiceInstances(MaskAlgorithm.class);
        for (MaskAlgorithm maskAlgorithm : maskAlgorithmCollection) {
            if (maskAlgorithm instanceof InitIal) {
                ((InitIal) maskAlgorithm).init(properties);
            }
            String type = maskAlgorithm.type();
            if (type.startsWith(CollectionMaskAlgorithm.LABEL)) {
                throw new RuntimeException("Customize Algorithm Type Cannot Start With " + CollectionMaskAlgorithm.LABEL + "，class:" + maskAlgorithm.getClass());
            }
            if (SPI_MASK_ALGORITHM.containsKey(type)) {
                // 终止启动
                throw new RuntimeException("There Are Duplicate Algorithm Configurations，class:" + maskAlgorithm.getClass());
            } else {
                log.debug("{}实例化成功，type:{}", maskAlgorithm.getClass().getName(), type);
                SPI_MASK_ALGORITHM.put(type, maskAlgorithm);
            }
        }
    }
}
