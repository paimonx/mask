package com.paimonx.mask.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author xu
 * @date 2022/3/24
 */
@Data
public class User {

    private String idNo;

    private String name;

    private String[] onceName;

    private Long phone;

    private AlternateContact alternateContact;

    private String gender;

    private String profession;

    private int age;

    private Date birthday;

    private Address address;

    private BigDecimal height;

    private BigDecimal weight;

    private String maritalStatus;

    private Set<Degree> educational;

    private String certificateValidity;

    private List<String> likes;
}
