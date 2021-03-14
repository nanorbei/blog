package com.yp.blog.service;

import com.yp.blog.pojo.Comment;

import java.util.List;

public interface CommentService {
    //获取该博客下的评论列表
    List<Comment> listCommentByBlogId(Long id);
    //保存评论
    Comment saveComment(Comment comment);
}
