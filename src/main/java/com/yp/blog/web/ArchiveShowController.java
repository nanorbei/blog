package com.yp.blog.web;

import com.yp.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArchiveShowController {
    @Autowired
    BlogService blogService;
    @GetMapping("/archives")
    public String archives(Model model) {
        //需要总博客数量
        model.addAttribute("blogCounts", blogService.count());
        //将Service中封装的年份与博客列表的map放进model中
        model.addAttribute("archivesBlog", blogService.archivesBlog());
        return "archives";
    }
}
