package com.yp.blog.dao;

import com.yp.blog.pojo.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Long> {
    //根据标签名进行查找
    Tag findByName(String name);
    //根据每个标签对应的博客个数进行排序，取前几个进行展示
    @Query("select t from Tag t")
    List<Tag> findTopBy(Pageable pageable);
}
