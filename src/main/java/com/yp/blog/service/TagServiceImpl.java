package com.yp.blog.service;

import com.yp.blog.dao.TagRepository;
import com.yp.blog.exception.NotFoundException;
import com.yp.blog.pojo.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class TagServiceImpl implements TagService{
    @Autowired
    TagRepository tagRepository;
    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public Tag getTag(Long id) {
        return tagRepository.getOne(id);
    }

    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag getTag = tagRepository.findById(id).get();
        if (getTag == null) {
            throw new NotFoundException("该标签在t_tags表中不存在");
        }
        //因为我们前端tag-input.html页面中只有tag的name,没有id即tag里面只有name
        BeanUtils.copyProperties(tag,getTag);
        return tagRepository.save(getTag);
    }

    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    @Override
    public Tag findByName(String name) {
        return tagRepository.findByName(name);
    }

    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }
    //根据id集合获取多个Tag
    @Override
    public List<Tag> listTag(String ids) {
        //将String转换成一个集合https://www.cnblogs.com/s1221/p/12562597.html

        return tagRepository.findAllById(convertToList(ids));
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"blogs.size");
        Pageable pageable = PageRequest.of(0, size, sort);
        return tagRepository.findTopBy(pageable);
    }

    //将字符串转换成一个集合
    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] splitIds = ids.split(",");
            for (String id : splitIds) {
                list.add(new Long(id));
            }
        }
        return list;
    }


}
