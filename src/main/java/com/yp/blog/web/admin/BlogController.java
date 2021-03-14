package com.yp.blog.web.admin;

import com.yp.blog.pojo.Blog;
import com.yp.blog.pojo.User;
import com.yp.blog.service.BlogService;
import com.yp.blog.service.TagService;
import com.yp.blog.service.TypeService;
import com.yp.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/admin/blogs")
public class BlogController {
    @Autowired
    BlogService blogService;
    @Autowired
    TypeService typeService;
    @Autowired
    TagService tagService;


    @GetMapping
    public String blogs(@PageableDefault(size = 2, sort =  {"updateDate"}, direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blog, Model model) {
        model.addAttribute("blogPage",blogService.listBlog(pageable, blog));
        model.addAttribute("types",typeService.listType());
        return "admin/blogs";
    }
    @PostMapping("/search")
    public String search(@PageableDefault(size = 2, sort =  {"updateDate"}, direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blog, Model model) {
        model.addAttribute("blogPage",blogService.listBlog(pageable, blog));
        //返回blogs.html页面中的blogList这个fragment布局
        return "admin/blogs :: blogList";
    }
    //点击新增跳转到发布博客的页面blog-input页面
    @GetMapping("/input")
    public String input(Model model) {
        //初始化分类
        model.addAttribute("types",typeService.listType());
        //初始化标签
        model.addAttribute("tags", tagService.listTag());
        //初始化blog是为了与修改公用一个页面
        model.addAttribute("blog",new Blog());
        return "admin/blog-input";
    }
    //点击blogs.html页面中的编辑跳转到blog-input页面
    @GetMapping("/input/{id}")
    public String editorInput(@PathVariable Long id, Model model) {
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
        //将blog放进model之间将tagIds进行初始化
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        return "admin/blog-input";
    }

    //博客新增
    //blog封装表单提交过来的数据，Session中有存放的user信息，而对于t_blog表来说是需要外键user_id的
    @PostMapping
    public String post(Blog blog, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User)session.getAttribute("user");
        blog.setUser(user);
        //从表单获取type.id进而在这里初始化Blog中Type属性
        blog.setType(typeService.getType(blog.getType().getId()));
        //从表单获得tagIds，进而获取该博客所对应的多个Tag
        blog.setTags(tagService.listTag(blog.getTagIds()));
        //当前面传递过来地blog的id为空时，表明为点击新增之后进行的保存或者发布
        //当传递过来的blog的id不为空时，表明为点击编辑之后进行的保存或者发布
        Blog getBlog = null;
        if (blog.getId() == null) {
            getBlog = blogService.saveBlog(blog);
        } else {
            getBlog = blogService.updateBlog(blog.getId(),blog);
        }
        if (getBlog == null) {
            redirectAttributes.addFlashAttribute("message","操作失败");
        } else {
            redirectAttributes.addFlashAttribute("message", "操作成功");
        }
        //新增之后重定向到blogs.html列表页面
        return "redirect:/admin/blogs";
    }
    //点击删除按钮触发的方法
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        blogService.deleteBlog(id);
        redirectAttributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/blogs";
    }
}
