package com.yp.blog.dao;

import com.yp.blog.pojo.Blog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


//JpaSpecificationExecutor是JPA提供的高级查询的接口，例如SQL拼接等
public interface BlogRepository extends JpaRepository<Blog,Long>, JpaSpecificationExecutor<Blog> {
    @Query("select b from Blog as b where b.recommend = true")
    List<Blog> findTopBy(Pageable pageable);

    //全局搜索 ?1匹配第一个参数
    @Query("select b from Blog b where b.title like ?1 or b.content like ?1")
    Page<Blog> findAllByQuery(Pageable pageable, String query);

    //对views增一
    @Modifying
    @Query("update Blog b set b.views = b.views + 1 where b.id = ?1")
    int updateViews(Long id);

    //获取所有的年份
    @Query("select distinct function('date_format', b.updateDate,'%y' ) as year from Blog b")
    List<String> findAllYear();

    //获取对应年份的博客
    @Query("select b from Blog b order by function('date_format', b.updateDate,'%y' )")
    List<Blog> findAllByYear();
}
