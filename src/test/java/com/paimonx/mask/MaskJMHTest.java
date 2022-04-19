package com.paimonx.mask;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.paimonx.mask.config.jackson.MaskBeanPropertyFilter;
import com.paimonx.mask.config.jackson.MaskBeanSerializerModifier;
import com.paimonx.mask.entity.Address;
import com.paimonx.mask.entity.AlternateContact;
import com.paimonx.mask.entity.Degree;
import com.paimonx.mask.entity.User;
import com.paimonx.mask.support.PropertyKeyConst;
import com.paimonx.mask.util.EncryptUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author xu
 * @date 2022/3/24
 */
@BenchmarkMode(Mode.SampleTime)
@Warmup(iterations = 3)
@Threads(4)
@Fork(1)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Measurement(iterations = 10, time = 5, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
public class MaskJMHTest {

    private static final User USER = new User();

    private static final ObjectMapper INITIAL_MAPPER = new ObjectMapper();
    private static final ObjectMapper MASK_MAPPER = new ObjectMapper();

    private static MaskManager maskManager;


    static {
        USER.setIdNo("13052519971008129X");
        USER.setName("刘赵煦");
        USER.setOnceName(new String[]{"赵旭", "刘旭"});
        USER.setPhone(18801179637L);
        AlternateContact alternateContact = new AlternateContact();
        alternateContact.setNickName("贾先生");
        alternateContact.setPhone("18801179639");
        USER.setAlternateContact(alternateContact);
        USER.setGender("男");
        USER.setProfession("学生");
        USER.setAge(26);
        USER.setBirthday(new Date());
        Address address = new Address();
        address.setCountry("中国");
        address.setProvince("北京市");
        address.setCity("北京市");
        address.setArea("朝阳区");
        address.setDetailed("哈哈哈小区18号楼1单元905");
        USER.setAddress(address);
        USER.setHeight(new BigDecimal("70"));
        USER.setWeight(new BigDecimal("180"));
        USER.setMaritalStatus("未婚");

        HashSet<Degree> set = new HashSet<>();
        Degree university = new Degree();
        university.setSchoolName("理工大学");
        university.setStart(2015);
        university.setEnd(2019);
        ArrayList<String> list = new ArrayList<>();
        for (int j = 1; j < 4; j++) {
            list.add(j + "等奖");
        }
        university.setHonor(list);
        Degree highSchool = new Degree();
        highSchool.setSchoolName("石家庄市第二中学");
        highSchool.setStart(2012);
        highSchool.setEnd(2015);
        set.add(university);
        set.add(highSchool);
        USER.setEducational(set);

        USER.setCertificateValidity("2035-10-25");
        ArrayList<String> likes = new ArrayList<>();
        likes.add("eating");
        likes.add("watch tv");
        USER.setLikes(likes);
    }

    static {
        MaskConfigProperties maskConfigProperties = new MaskConfigProperties();

        HashMap<String, Map<String, String>> map = new HashMap<>();
        // 配置类
        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("idNo", "idno");
        userMap.put("name", "name");
        // userMap.put("phone", "phone");
        // userMap.put("onceName","*name");
        map.put("com.paimonx.mask.entity.User", userMap);

        HashMap<String, String> alternateContactMap = new HashMap<>();
        alternateContactMap.put("phone", "phone");
        map.put("com.paimonx.mask.entity.AlternateContact", alternateContactMap);

        HashMap<String, String> addressMap = new HashMap<>();
        addressMap.put("detailed", "common");
        map.put("com.paimonx.mask.entity.Address", addressMap);

        maskConfigProperties.setClassDefinitions(map);
        maskConfigProperties.setEnabled(true);
        maskManager = new MaskManager(maskConfigProperties);
    }

    static {
        // 添加 module
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.setSerializerModifier(new MaskBeanSerializerModifier(maskManager.getMaskConfigProperties()));
        MASK_MAPPER.registerModule(simpleModule);

        // 添加 filter
        SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();
        simpleFilterProvider.addFilter(PropertyKeyConst.DEFAULT_MASK_FILTER_ID, new MaskBeanPropertyFilter(maskManager));
        MASK_MAPPER.setFilterProvider(simpleFilterProvider);
    }


    @Benchmark
    public void testOriginal() throws JsonProcessingException {
        USER.setName((String) EncryptUtils.encryptName(USER.getName()));
        USER.setIdNo((String) EncryptUtils.encryptIdNo(USER.getIdNo()));
        AlternateContact alternateContact = USER.getAlternateContact();
        alternateContact.setPhone((String) EncryptUtils.encryptPhone(alternateContact.getPhone()));
        Address address = USER.getAddress();
        address.setDetailed((String) EncryptUtils.encryptCommon(address.getDetailed()));
        String s = INITIAL_MAPPER.writeValueAsString(USER);
    }

    /**
     * 需要去除代码中对uri 的判断
     *  1.  com.paimonx.mask.config.MaskSerializeTemplate#getMaskType(java.lang.Class, java.lang.String, java.lang.String)  第37-39行注掉
     * @throws JsonProcessingException
     */
    @Benchmark
    public void testMask() throws JsonProcessingException {
        String s = MASK_MAPPER.writeValueAsString(USER);
    }

/*    @Benchmark
    public void testNameAlgorithm() {
        MaskAlgorithm algorithm = MaskManager.MASK_ALGORITHM.get("name");
        Object encrypt = algorithm.encrypt(USER.getName());
    }

    @Benchmark
    public void testPhoneAlgorithm() {
        MaskAlgorithm algorithm = MaskManager.MASK_ALGORITHM.get("phone");
        Object encrypt = algorithm.encrypt(USER.getPhone());
    }

    @Benchmark
    public void testIdNoAlgorithm() {
        MaskAlgorithm algorithm = MaskManager.MASK_ALGORITHM.get("idno");
        Object encrypt = algorithm.encrypt(USER.getIdNo());
    }
    @Benchmark
    public void testCommonAlgorithm() {
        MaskAlgorithm algorithm = MaskManager.MASK_ALGORITHM.get("common");
        Object encrypt = algorithm.encrypt(USER.getLikes());
    }

    @Benchmark
    public void testCollectionMaskAlgorithm() {
        MaskAlgorithm algorithm = MaskManager.MASK_ALGORITHM.get("name");
        CollectionMaskAlgorithm collectionMaskAlgorithm = new CollectionMaskAlgorithm(algorithm);
        Object encrypt = collectionMaskAlgorithm.encrypt(USER.getOnceName());
    }*/


    public static void main(String[] args) throws RunnerException {
        Options build = new OptionsBuilder()
                .include(MaskJMHTest.class.getSimpleName())
                .result("mask-SampleTime-1.json")
                .resultFormat(ResultFormatType.JSON)
                .build();

        new Runner(build).run();
    }
}
