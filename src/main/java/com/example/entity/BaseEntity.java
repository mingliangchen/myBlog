/**
 * 文件名：BaseEntity.java
 * 版权： Copyright 2017-2022 HMDM All Rights Reserved.
 * 描述：主数据管理系统
 */
package com.example.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author or mingliang.chen@hand-china.com
 * @version 1.0
 * @date 2019/4/23 16:50
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Date created;
    private Date modified;
}
