package com.yp.blog.dao;

import com.yp.blog.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

//User 要操作对象所对应那个表，Long是表中主键的类型
public interface UserRepository extends JpaRepository<User,Long> {
    //会根据命名规则去数据库中根据username和password去查询数据库，并且将查询的记录封装成User
    User findByUsernameAndPassword(String username, String password);
}
