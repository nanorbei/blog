package com.yp.blog.web;


import com.yp.blog.service.BlogService;
import com.yp.blog.service.CommentService;
import com.yp.blog.service.TagService;
import com.yp.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    @Autowired
    BlogService blogService;
    @Autowired
    TypeService typeService;
    @Autowired
    TagService tagService;
    @Autowired
    CommentService commentService;
    //进入前端首页，进行分页展示
    @GetMapping("/")
    public String index(@PageableDefault(size = 2) Pageable pageable, Model model) {
        model.addAttribute("page",blogService.listBlog(pageable));
        //展示分类,根据分类对应博客个数额多少进行排序，之后取前几个进行展示
        model.addAttribute("types",typeService.listTypeTop(5));
        //展示标签
        model.addAttribute("tags", tagService.listTagTop(6));
        //展示推荐
        model.addAttribute("recommendBlogs",blogService.listRecommendBlogsTop(8));
        return "index";
    }
    //搜索框触发的方法，依旧分页展示数据
    //query对应form表单中的name="query"的输入框，拿到里面的值
    @PostMapping("/search")
    public String search(@PageableDefault(size = 2) Pageable pageable, Model model, @RequestParam String query) {
        model.addAttribute("page", blogService.listBlog(pageable, query));
        model.addAttribute("query", query);
        return "search";
    }
    //博客详情页
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model) {
        model.addAttribute("blog", blogService.getBlogAndConvert(id));
        return "blog";
    }

    //对footer进行最新推荐进行处理
    @GetMapping("/footer/recommendBlog")
    public String recommendBlog(Model model) {
        model.addAttribute("recommendBlog",blogService.listRecommendBlogsTop(3));
        return "_fragments :: recommendBlog";

    }


}
