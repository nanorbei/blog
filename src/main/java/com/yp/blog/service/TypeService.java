package com.yp.blog.service;

import com.yp.blog.pojo.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {
    //新增分类
    Type saveType(Type type);
    Type getType(Long id);
    Page<Type> listType(Pageable pageable);
    Type updateType(Long id, Type type);
    void deleteType(Long id);
    //根据name查询
    Type findByName(String name);
    //获取所有的type
    List<Type> listType();
    //获取type前几个
    List<Type> listTypeTop(Integer size);
}
