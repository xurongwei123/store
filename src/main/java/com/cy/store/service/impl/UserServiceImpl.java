package com.cy.store.service.impl;

import com.cy.store.entity.User;
import com.cy.store.mapper.UserMapper;
import com.cy.store.service.IUserService;
import com.cy.store.service.ex.InsertException;
import com.cy.store.service.ex.PasswordNotMatchException;
import com.cy.store.service.ex.UserNotFoundException;
import com.cy.store.service.ex.UsernameDuplicatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.UUID;

/**
 * @author xrw
 * @create 2022-01-07 9:49
 * 用户模块业务层实现类
 */
@Service//将当前类的对象交给Spring来管理，自动创建对象以及对象的维护
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void reg(User user) {
        //通过user参数来获取传递过爱的username
        String username =  user.getUsername();
        //调用 findByUsername(username)判断用户是否被注册过
         User result =  userMapper.findByUsername(username);
        // 判断结果集是否为null，为null可以注册
        //部位null抛出被占用的异常
        if(result != null){
            //抛出异常
           throw new UsernameDuplicatedException("用户名被占用");
        }

        //密码加密处理的实现：md5算法的形式
        //（串+password+串） ---- md5算法进行加密，连续加载3次
        //盐值 + password + 盐值 ---- 盐值就是随机的字符串
        String oldPassword =  user.getPassword();
        //获取盐值(随机生成一个盐值)
        String salt = UUID.randomUUID().toString().toUpperCase();
        //补全数据：盐值记录
        user.setSalt(salt);
        //将密码和盐值作为一个整体进行加密处理,忽略原有密码强度提升了数据的安全性
        String md5Password = getMD5Password(oldPassword, salt);
        //将加密之后的密码重新补全设置到user对象中
        user.setPassword(md5Password);

        //补全数据：is_delete设置成0
        user.setIsDelete(0);
        //补全数据：4个日志字段信息
        user.setCreatedUser(user.getUsername());
        user.setModifiedUser(user.getUsername());
        Date date = new Date();
        user.setCreatedTime(date);
        user.setModifiedTime(date);
        //执行注册业务功能的实现(row==1)
        Integer rows = userMapper.insert(user);
        if(rows != 1){
            throw new InsertException("在用户注册过程中产生了未知异常");
        }
    }

    @Override
    public User login(String username, String password) {
        //根据用户名称来查询用户名的数据是否存在,若不存在则抛出异常
        User result =  userMapper.findByUsername(username);
        if(result == null){
              throw new UserNotFoundException("用户数据不存在");
        }
        //检测用户密码是否匹配
        //1.先获取到数据库中的加密密码之后的密码
        String oldPassword = result.getPassword();
        //2.和用户的传递过来的密码进行比较
        //2.1 先获取盐值：上一次在自动注册生成的盐值
        String salt = result.getSalt();
        //2.2 将用户的密码按照相同的md5算法规则进行加密
        String newMd5Password = getMD5Password(password, salt);
        //3.将密码进行比较
        if(!newMd5Password.equals(oldPassword)){
            throw new PasswordNotMatchException("用户密码错误");
        }

        //判断is_delete字段值是否为1表示被标记删除
        if(result.getIsDelete() == 1){
               throw new UserNotFoundException("用户数据不存在");
        }
        //调用mapper层的findByUsername来查询用户的数据,提升了系统的性能
        User user = new User();
        user.setUid(result.getUid());
        user.setUsername(result.getUsername());
        user.setAvatar(result.getAvatar());

        // 将当前的用户数据返回，返回的数据是为了辅助其他页面做数据展示
        return user;
    }

    /**
     * 定义一个md5算法的加密处理
     */
    private String getMD5Password(String password, String salt){
        for(int i = 1; i<3; i++){
            //md5加密算法方法的调用(进行三次加密)
         password  =  DigestUtils.md5DigestAsHex((salt+password+salt).getBytes()).toUpperCase();
        }
        //返回加密之后的密码
        return password;
    }
}
