package com.paimonx.mask.util;

import java.util.HashSet;
import java.util.Set;

/**
 * @author xu
 * @date 2022/3/31
 */
public class EncryptUtils {

    private EncryptUtils() {
    }

    private static final Set<String> COMPOUND_SURNAME = new HashSet<>();

    private static final String DEFAULT_COMPOUND_SURNAME =   "百里、北堂、北野、北宫、辟闾、淳于、成公、陈生、褚师、" +
            "端木、东方、东郭、东野、东门、第五、大狐、段干、段阳、带曰、第二、东宫、南宫、南郭、女娲、南伯、南容、南门、南野、" +
            "公孙、公冶、公羊、公良、公西、公孟、高堂、高阳、公析、公肩、公坚、郭公、谷梁、毌将、公乘、毌丘、公户、公广、公仪、" +
            "夹谷、九方、即墨、梁丘、闾丘、洛阳、陵尹、冷富、龙丘、令狐、林彭、欧阳、欧侯、濮阳、青阳、漆雕、亓官、渠丘、壤驷、" +
            "上官、少室、少叔、司徒、司马、司空、司寇、士孙、申屠、申徒、申鲜、申叔、夙沙、叔先、叔仲、侍其、叔孙、澹台、太史、" +
            "闻人、巫马、微生、王孙、无庸、夏侯、西门、信平、鲜于、轩辕、相里、新垣、徐离、羊舌、羊角、延陵、於陵、伊祁、吾丘、" +
            "诸葛、颛孙、仲孙、仲长、钟离、宗政、主父、中叔、左人、左丘、宰父、长儿、仉督、单于、叱干、叱利、车非、唐古、乐正、" +
            "独孤、大野、独吉、达奚、哥舒、赫连、呼延、贺兰、黑齿、斛律、斛粟、贺若、夹谷、吉胡、可频、慕容、万俟、抹捻、纳兰、" +
            "普周、仆固、仆散、蒲察、屈突、屈卢、钳耳、是云、索卢、厍狄、拓跋、同蹄、秃发、完颜、宇文、尉迟、耶律、长孙、公祖、" +
            "皇甫、黄龙、胡母、何阳、太叔、太公、屠岸";

    private static final int COMPOUND = 2;

    private static final String SPECIAL_IDENTIFICATION = "·";

    private static final String SUFFIX = "**";

    public static Object encryptIdNo(Object plaintext) {
        String idNo = String.valueOf(plaintext) ;
        return idNo.substring(0,6)+"******"+idNo.substring(idNo.length()-3);
    }

    public static Object encryptName(Object plaintext) {
        if (plaintext instanceof String) {
            String name = (String) plaintext;
            // 按 · 切割
            String processName = name.split(SPECIAL_IDENTIFICATION)[0];
            // 不包含
            if (name.equals(processName)) {
                if (name.length() > COMPOUND) {
                    processName = name.substring(0, COMPOUND);
                    // 是否复姓
                    if (COMPOUND_SURNAME.contains(processName)) {
                        return processName + SUFFIX;
                    }
                }
                return name.charAt(0) + SUFFIX;
            } else {
                return processName + SPECIAL_IDENTIFICATION + SUFFIX;
            }
        }
        throw new RuntimeException("错误类型");
    }

    public static Object encryptPhone(Object plaintext) {
        String phone = String.valueOf(plaintext) ;
        return phone.substring(0,3)+"****"+phone.substring(7);
    }

    public static Object encryptCommon(Object plaintext) {
        return "***";
    }
}
