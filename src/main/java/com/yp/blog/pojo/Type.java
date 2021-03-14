package com.yp.blog.pojo;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="t_type")//修改自动生成表的名称
public class Type {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank(message ="请输入分类名称")
    private String name;

    /******************************************************/
    @OneToMany(mappedBy = "type") //关系被维护端 mappedBy="type" 被动的维护Blog类中属性type与Type类中blogs属性之间的关系
    private List<Blog> blogs = new ArrayList<>();
    public Type() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
