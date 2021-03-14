package com.yp.blog.service;

import com.yp.blog.pojo.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {
    //新增标签
    Tag saveTag(Tag tag);
    Tag getTag(Long id);
    Page<Tag> listTag(Pageable pageable);
    Tag updateTag(Long id, Tag tag);
    void deleteTag(Long id);
    //根据name查询
    Tag findByName(String name);
    List<Tag> listTag();
    //根据多个Tag的id获得Tag
    List<Tag> listTag(String ids);
    //根据每个标签对应博客个数进行排序，进而进行展示
    List<Tag> listTagTop(Integer size);
}
