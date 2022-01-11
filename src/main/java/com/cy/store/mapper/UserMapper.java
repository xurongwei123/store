package com.cy.store.mapper;

import com.cy.store.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xrw
 * @create 2022-01-05 15:52
 * 用户模块的持久层
 */
//@Mapper
public interface UserMapper {
    /**
     * 插入用户数据
     * @param user 用户的数据
     * @return受影响的行数（增删改都受影响的行数作为返回值，可以根据返回值来判断是否执行成功）
     */
   Integer insert(User user);

    /**
     * 根据用户名来查询用户的数据
     * @param username 用户名
     * @return 如果找到对应用户则返回用户的数据，如果没有找到则返回null
     */
   User findByUsername(String username);
}
