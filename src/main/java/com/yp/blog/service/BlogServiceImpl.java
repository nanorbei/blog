package com.yp.blog.service;

import com.yp.blog.dao.BlogRepository;
import com.yp.blog.exception.NotFoundException;
import com.yp.blog.pojo.Blog;
import com.yp.blog.pojo.Type;
import com.yp.blog.util.MarkdownUtils;
import com.yp.blog.util.MyBeanUtils;
import com.yp.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

//开启事务
@Transactional
@Service
public class BlogServiceImpl implements BlogService{
    @Autowired
    BlogRepository blogRepository;
    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getOne(id);
    }
    //动态分页查询
    //根据标题是否传值
    //分类是否传值
    //推荐是否
    //进而查询，并且进行分页展示
    //不建议进行SQL语句的直接拼接
    //JPA中帮我们封装好了这种组合查询的接口JpaSpecificationExecutor
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            //条件的动态组合
            //Root<Blog> root将Blog映射成Root,我们可以从root中拿到Blog对应表的字段
            //CriteriaBuilder用来生成一个条件表达式，等于 like
            //CriteriaQuery条件表达式的容器，将多个条件表达式进行组合
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                //将封装的条件放在list中
                List<Predicate> predicates = new ArrayList<>();
                //标题
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    //拿到Blog属性title所对应的字段，like,blog.getTitle()
                    predicates.add(cb.like(root.<String>get("title"),"%" + blog.getTitle() + "%"));
                }
                //分类
                if (blog.getTypeId() != null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"),blog.getTypeId()));
                }
                //当推荐时
                if (blog.isRecommend()) {
                    predicates.add(cb.equal(root.<Boolean>get("recommend"),blog.isRecommend()));
                }
                //list转换成数组
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Blog saveBlog(Blog blog) {
        //需要初始化一些值
        blog.setCreateTime(new Date());
        blog.setUpdateDate(new Date());
        blog.setViews(0);//浏览次数
        return blogRepository.save(blog);
    }

    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog getBlog = blogRepository.getOne(id);
        if (getBlog == null) {
            throw new NotFoundException("该博客不存在");
        }
        blog.setUpdateDate(new Date());
        BeanUtils.copyProperties(blog,getBlog, MyBeanUtils.getNullPropertyNames(blog));
        return blogRepository.save(getBlog);
    }

    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public List<Blog> listRecommendBlogsTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"updateDate");
        Pageable pageable = PageRequest.of(0,size,sort);
        return blogRepository.findTopBy(pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, String query) {
        return blogRepository.findAllByQuery(pageable, "%" + query + "%");
    }
    //
    @Override
    public Blog getBlogAndConvert(Long id) {
        Blog blog = blogRepository.getOne(id);
        if (blog == null) {
            throw new NotFoundException("该博客不存在...");
        }
        /*该操作会改变Hibernate session里面blog的属性值，有可能会操作数据库
         *String content = blog.getContent();
         *blog.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
         */
        //所以我们常见一个新的Blog,将blog里非空的传递给b
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b,MyBeanUtils.getNullPropertyNames(blog));
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        //设置view增一
        blogRepository.updateViews(b.getId());
        Integer views = blogRepository.getOne(id).getViews();
        b.setViews(views);
        return b;
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, Long id) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                //连接,join 将t_blog表，通过tags进行连接 t_tags
                Join<Object, Object> join = root.join("tags");
                //where判断，找到满足标签id的博客
                return cb.equal(join.get("id"),id);
            }
        },pageable);
    }
    //获取博客数量
    public Long count() {
        return blogRepository.count();
    }
    //获取年份以及对应的博客列表，封装成Map
    @Override
    public Map<String, List<Blog>> archivesBlog() {
        Map<String, List<Blog>> archivesBlog = new HashMap<>();
        //获得年份列表
        List<String> allYear = blogRepository.findAllYear();
        //将每一个年份当作键，查找其对应的博客
        for (String year : allYear) {
            archivesBlog.put(year, blogRepository.findAllByYear());
        }
        return archivesBlog;
    }
}
