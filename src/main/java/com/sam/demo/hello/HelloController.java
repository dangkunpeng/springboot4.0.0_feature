package com.sam.demo.hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/api")
public class HelloController {

    /**
     * 4. 使用ModelAndView
     * 访问: http://localhost:8080/hello/mav
     */
    @GetMapping("/hello")
    public String helloModelAndView(ModelMap model) {

        return "/hello/world";
    }
}
