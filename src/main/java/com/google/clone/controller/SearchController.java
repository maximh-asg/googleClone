package com.google.clone.controller;

import com.google.clone.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by mhuziv on 24-Sep-15.
 */
@Controller
public class SearchController {

    @Autowired
    SearchService searchService;

    @RequestMapping(method= RequestMethod.GET, value = "/")
    String home() {
        return "search";
    }

    @RequestMapping("/search")
    String search(@RequestParam(value="q", required=false) String q, Model model) {
        model.addAttribute("result", searchService.doSearch(q));
        return "SearchResult";
    }
}
