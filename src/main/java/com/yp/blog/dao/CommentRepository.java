package com.yp.blog.dao;

import com.yp.blog.pojo.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    //根据博客id获得评论列表，并且对评论进行排序，按照时间
    List<Comment> findByBlogId(Long blogId, Sort sort);
    //只获取没有父级评论的顶层评论
    List<Comment> findByBlogIdAndParentCommentNull(Long blogId,Sort sort);
}
