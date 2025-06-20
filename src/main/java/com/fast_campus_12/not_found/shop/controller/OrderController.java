
package com.fast_campus_12.not_found.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

    @Controller
    public class OrderController {
        @GetMapping("/order/{pageName}")
        public String renderPage(@PathVariable("pageName") String pageName, Model model) {
            model.addAttribute("contentPath", "order/" + pageName);
            return "layout/base";
        }
    }



