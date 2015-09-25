package com.google.clone.controller;

import com.google.clone.service.IndexService;
import com.google.clone.service.IndexServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by mhuziv on 24-Sep-15.
 */
@Controller
public class IndexController {

    @Autowired
    IndexService indexService;

    private int count = 3;

    @RequestMapping("/index")
    String index(@RequestParam(value="q", required=false) String q, Model model) {
        if(q != null) {
            model.addAttribute("result", indexService.indexPage(q, count));
            return "indexDone";
        }
        return "index";
    }
}
