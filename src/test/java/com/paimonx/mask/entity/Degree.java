package com.paimonx.mask.entity;

import lombok.Data;

import java.util.List;

/**
 * @author xu
 * @date 2022/3/24
 */
@Data
public class Degree {

    private String schoolName;

    private Integer start;

    private Integer end;

    private List<String> honor;
}
