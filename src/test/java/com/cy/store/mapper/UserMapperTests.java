package com.cy.store.mapper;

import com.cy.store.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xrw
 * @create 2022-01-06 15:29
 */
//@SpringBootTest:表示标注当前的类是一个测试类，不会随同项目一块打包发送
@SpringBootTest
//@RunWith:表示启动这个单元测试类，不写不能运行，需要传递一个参数必须是SpringRunner的实例类型
@RunWith(SpringRunner.class)
public class UserMapperTests {
    // Could not autowire. No beans of 'UserMapper' type found
    //idea具有检测功能，接口不能直接创建Bean的（动态代理来解决）
    @Autowired
    private UserMapper userMapper;

    //单元测试方法可以独立运行，不用启动整个项目，可以做单元测试
    //1.必须被@Test修饰
    //2.返回值类型必须是void
    //3.方法的参数列表不指定任何类型
    //4.方法的访问修饰符必须是public
    @Test
    public void insert() {
        User user = new User();
        user.setUsername("user01");
        user.setPassword("123456");
        Integer rows = userMapper.insert(user);
        System.out.println("rows=" + rows);
    }

    @Test
    public void findByUsername() {
        String username = "user01";
        User result = userMapper.findByUsername(username);
        System.out.println(result);
    }
}
