package com.yp.blog.web;

import com.yp.blog.pojo.Blog;
import com.yp.blog.pojo.Type;
import com.yp.blog.service.BlogService;
import com.yp.blog.service.TypeService;
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
public class TypeShowController {
    @Autowired
    TypeService typeService;
    @Autowired
    BlogService blogService;
    @GetMapping("/types/{id}")
    public String types(@PageableDefault(size = 2)Pageable pageable, Model model,@PathVariable Long id) {
        //根据分类所对应博客多少进行排序，返回分类列表，size=10000，是取前1000个分类，一般情况下
        //分类个数不会超过1000，所以可以认为是获取到所有的排序后的分类列表
        List<Type> types = typeService.listTypeTop(1000);
        model.addAttribute("types",types);
        if (id == -1) {
            //因为我们在导航栏的链接里设置默认请求的type的id=-1,即此时如果是-1，就是点击导航栏的分类进入分类页面
            //此时我们就获得分类下有最多博客的博客列表进行分页展示
            //获得排序后第一个分类的id
            id = types.get(0).getId();

        }
        //如果传递过来的id不等于-1，即是在分类页面点击响应的分类标签所触发的请求，就展示相应的分类下的博客即可
        //构建查询博客需要的对象BlogQuery
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        //该方法是根据标题与分类进行SQL动态组合查询
        Page<Blog> page = blogService.listBlog(pageable, blogQuery);
        model.addAttribute("page",page);
        //为了判断当前选中的分类与其他分类进行区分，我们将id在封装进model中
        model.addAttribute("activeTypeId", id);
        return "types";
    }
}
