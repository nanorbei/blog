package com.yp.blog.service;

import com.yp.blog.dao.CommentRepository;
import com.yp.blog.pojo.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Transactional
@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    private CommentRepository commentRepository;
    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        //按照时间倒叙排序
        Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
        //我们评论的展示需要体现出层级结构
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId, sort);
        List<Comment> copyComments = copyComments(comments);
        //为copyComments顶级节点设置子级节点集合
        combineChildren(copyComments);
        return copyComments;
    }

    //根据当前评论是否有父级评论进行判断，进而保存该评论
    @Override
    public Comment saveComment(Comment comment) {
        //获取该评论父评论id是否为-1
        Long parentCommentId = comment.getParentComment().getId();
        if (parentCommentId != -1) {
            //如果不是-1，表明该评论有真正的父级评论，建立子父级关系,设置当前评论的父级评论
            comment.setParentComment(commentRepository.getOne(parentCommentId));
        } else {
            //如果为-1，该评论没有父级评论
            //设置该评论的父级评论为null
            comment.setParentComment(null);
        }
        //设置该评论的创建时间
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }

    //循环顶级评论,将从数据库取出来的数据与数据库隔离
    List<Comment> copyComments(List<Comment> comments) {
        List<Comment> copyComments = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            //将顶级评论添加进copyComments中
            copyComments.add(c);
        }
        return copyComments;
    }
    //存放子级评论
    private List<Comment> tempReplyComments = new ArrayList<>();

    //遍历顶级节点对每个顶级节点的子节点进行赋值
    private void combineChildren(List<Comment> comments) {
        for (Comment comment : comments) {
            List<Comment> replyComments = comment.getReplyComments();
            //当顶级评论有子级评论时，就遍历子级评论
            for (Comment reply : replyComments) {
                //对于子级评论进行递归，直到没有子级评论
                recursively(reply);
            }
            //将递归得到的子级评论设置到顶级评论的子级节点中
            comment.setReplyComments(tempReplyComments);
            //设置顶级节点之后，将存放子级节点的集合清空，以便下次使用
            tempReplyComments = new ArrayList<>();
        }


    }
    //被combineChildren调用，使用递归将顶级节点地下的所有节点存放到一个集合中
    private void recursively(Comment comment) {
        tempReplyComments.add(comment);
        if (comment.getReplyComments().size() > 0) {
            List<Comment> replyComments = comment.getReplyComments();
            for (Comment reply : replyComments) {
                tempReplyComments.add(reply);
                if (reply.getReplyComments().size() > 0) {
                    recursively(reply);
                }
            }
        }
    }

}
