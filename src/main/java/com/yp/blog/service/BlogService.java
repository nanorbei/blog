package com.yp.blog.service;

import com.yp.blog.pojo.Blog;
import com.yp.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

public interface BlogService {
    Blog getBlog(Long id);
    //带有拼接查询条件的方法
    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);
    //单纯的分页查询
    Page<Blog> listBlog(Pageable pageable);
    Blog saveBlog(Blog blog);
    Blog updateBlog(Long id, Blog blog);
    void deleteBlog(Long id);
    //根据更新时间，推荐博客
    List<Blog> listRecommendBlogsTop(Integer size);
    //全局搜索框根据query进行like查询
    Page<Blog> listBlog(Pageable pageable, String query);
    //专门用于前端blog.html展示
    Blog getBlogAndConvert(Long id);
    //根据标签id获取博客列表
    Page<Blog> listBlog(Pageable pageable,Long id);
    //获取博客数量
    Long count();
    //获取年份与对应的博客列表封装成map
    Map<String, List<Blog>> archivesBlog();

}
