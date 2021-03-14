package com.yp.blog.dao;

import com.yp.blog.pojo.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeRepository extends JpaRepository<Type,Long> {
    //根据name查询
    Type findByName(String name);
    //根据分类对应博客多少取前几个
    @Query("select t from Type t")
    List<Type> findTopBy(Pageable pageable);

}
