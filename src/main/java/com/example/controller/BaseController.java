/**
 * 文件名：BaseController.java
 * 版权： Copyright 2017-2022 HMDM All Rights Reserved.
 * 描述：主数据管理系统
 */
package com.example.controller;

import com.example.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * @author or mingliang.chen@hand-china.com
 * @version 1.0
 * @date 2019/4/23 16:52
 */
public class BaseController {

    @Autowired
    HttpServletRequest req;

    @Autowired
    RedisUtil redisUtil;

}
