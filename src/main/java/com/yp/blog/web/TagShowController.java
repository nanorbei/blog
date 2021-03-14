package com.yp.blog.web;

import com.yp.blog.pojo.Blog;
import com.yp.blog.pojo.Tag;
import com.yp.blog.service.BlogService;
import com.yp.blog.service.TagService;
import com.yp.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TagShowController {
    @Autowired
    TagService tagService;
    @Autowired
    BlogService blogService;
    @GetMapping("/tags/{id}")
    public String tags(@PageableDefault(size = 2) Pageable pageable, Model model, @PathVariable Long id) {
        //根据标签所对应博客多少进行排序，返回标签列表，size=10000，是取前1000个标签，一般情况下
        //标签个数不会超过1000，所以可以认为是获取到所有的排序后的标签列表
        List<Tag> tags = tagService.listTagTop(1000);
        model.addAttribute("tags",tags);
        if (id == -1) {
            //因为我们在导航栏的链接里设置默认请求的tag的id=-1,即此时如果是-1，就是点击导航栏的标签进入标签页面
            //此时我们就获得标签下有最多博客的博客列表进行分页展示
            //获得排序后第一个标签的id
            id = tags.get(0).getId();

        }
        //该方法是根据标题与标签进行SQL动态组合查询
        Page<Blog> page = blogService.listBlog(pageable, id);
        model.addAttribute("page",page);
        //为了判断当前选中的标签与其他标签进行区分，我们将id在封装进model中
        model.addAttribute("activeTagId", id);
        return "tags";
    }
}
