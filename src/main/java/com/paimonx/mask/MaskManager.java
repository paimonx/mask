package com.paimonx.mask;

import com.paimonx.mask.spi.MaskAlgorithm;
import com.paimonx.mask.spi.MaskServiceLoader;
import com.paimonx.mask.support.InitIal;
import com.paimonx.mask.support.MaskProcessor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author xu
 * @date 2022/3/17
 */
@Data
public class MaskManager {

    private Logger log = LoggerFactory.getLogger(MaskManager.class);

    private final MaskConfigProperties maskConfigProperties;

    public final static Map<String, MaskAlgorithm> MASK_ALGORITHM = new HashMap<>(8);

    private final List<MaskProcessor> maskProcessors = new ArrayList<>();



    public MaskManager(MaskConfigProperties maskConfigProperties, MaskProcessor... maskProcessors) {
        this(maskConfigProperties, Arrays.asList(maskProcessors));
    }

    public MaskManager(MaskConfigProperties maskConfigProperties, Collection<MaskProcessor> maskProcessors) {
        this.maskConfigProperties = maskConfigProperties;
        this.maskProcessors.addAll(maskProcessors);
        init(maskConfigProperties);
    }

    public void setMaskProcessor(MaskProcessor maskProcessor) {
        maskProcessors.add(maskProcessor);
    }

    private void init(MaskConfigProperties maskConfigProperties) {
        Properties properties = new Properties();
        maskConfigProperties.getAlgorithmMetadata().forEach(properties::putIfAbsent);
        // spi 加载算法
        initMaskAlgorithm(properties);
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
            if (MASK_ALGORITHM.containsKey(type)) {
                // 终止启动
                throw new RuntimeException("There Are Duplicate Algorithm Configurations，class:" + maskAlgorithm.getClass());
            } else {
                MASK_ALGORITHM.put(type, maskAlgorithm);
            }
        }
    }
}
