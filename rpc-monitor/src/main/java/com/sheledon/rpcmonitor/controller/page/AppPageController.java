package com.sheledon.rpcmonitor.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Sheledon
 * @date 2022/2/24
 * @Version 1.0
 */
@Controller
public class AppPageController {

    @GetMapping("/")
    public String index(){
        return "index";
    }
}
