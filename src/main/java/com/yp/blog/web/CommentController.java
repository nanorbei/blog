package com.yp.blog.web;

import com.yp.blog.pojo.Comment;
import com.yp.blog.pojo.User;
import com.yp.blog.service.BlogService;
import com.yp.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private BlogService blogService;
    //从数据库获得评论，将数据放进model中，以便在blog.html页面中获取评论信息
    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {
        model.addAttribute("comments",commentService.listCommentByBlogId(blogId));
        return "blog :: commentList";

    }
    @Value("${comment.avatar}")
    private String avatar;
    //处理评论提交，并且在将评论保存好之后展示评论列表，重定向到"/comments/{blogId}"
    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session) {
        //建立博客与评论之间的关系
        comment.setBlog(blogService.getBlog(comment.getBlog().getId()));
        //判断当前用户是否登录后台，进而判断是否为管理员
        User user = (User)session.getAttribute("user");
        if (user != null) {
            //设置adminComment为true,默认值为false,表明该评论对应的用户是管理员
            comment.setAdminComment(true);
            //将评论者的头像，以及发布输入框里面的昵称与邮箱进行填充
            comment.setAvatar(user.getAvatar());
            comment.setNickname(user.getNickname());
            comment.setEmail(user.getEmail());
        } else {
            //用户评论的图片是静态默认的图片
            //comment.setAvatar("/images/用户.jpg");
            comment.setAvatar(avatar);
        }
        commentService.saveComment(comment);
        return "redirect:/comments/" + comment.getBlog().getId();
    }
}
