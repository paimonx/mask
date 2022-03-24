package com.paimonx.mask.entity;

import lombok.Data;

/**
 * @author xu
 * @date 2022/3/24
 */
@Data
public class Address {
    private String country;
    private String province;
    private String city;
    private String area;
    private String detailed;
}
