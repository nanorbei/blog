package com.yp.blog.service;

import com.yp.blog.dao.TypeRepository;
import com.yp.blog.exception.NotFoundException;
import com.yp.blog.pojo.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TypeServiceImpl implements TypeService{
    @Autowired
    TypeRepository typeRepository;
    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    @Override
    public Type getType(Long id) {
        return typeRepository.getOne(id);
    }

    //查看分页展示的分类
    @Override
    public Page<Type> listType(Pageable pageable) {
        //查询数据库，将数据包装在Page<Type>对象中
        Page<Type> typePage = typeRepository.findAll(pageable);
        return typePage;
    }

    @Override
    public Type updateType(Long id, Type type) {
        Type getType = typeRepository.findById(id).get();
        if (getType == null) {
            throw new NotFoundException("该分类在t_types表中不存在");
        }
        //因为我们前端type-input.html页面中只有type的name,没有id即type里面只有name
        //把第一个参数赋值给第二个参数，对于非空的
        BeanUtils.copyProperties(type,getType);
        return typeRepository.save(getType);
    }

    @Override
    public void deleteType(Long id) {
        typeRepository.deleteById(id);

    }

    @Override
    public Type findByName(String name) {
        return typeRepository.findByName(name);
    }

    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    //根据分类对应博客个数进行排序，取size个展示1
    @Override
    public List<Type> listTypeTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"blogs.size");
        Pageable pageable = PageRequest.of(0,size,sort);
        return typeRepository.findTopBy(pageable);
    }
}
