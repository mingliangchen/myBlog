/**
 * 文件名：IndexController.java
 * 版权： Copyright 2017-2022 HMDM All Rights Reserved.
 * 描述：主数据管理系统
 */
package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author or mingliang.chen@hand-china.com
 * @version 1.0
 * @date 2019/4/23 17:24
 */
@Controller
public class IndexController extends BaseController  {
    @RequestMapping({"", "/", "/index"})
    public String index () {
        return "index";
    }
}
