package com.cy.store.controller;

import com.cy.store.entity.BaseEntity;
import com.cy.store.entity.User;
import com.cy.store.service.IUserService;
import com.cy.store.service.ex.InsertException;
import com.cy.store.service.ex.UsernameDuplicatedException;
import com.cy.store.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author xrw
 * @create 2022-01-07 11:31
 */
//@Controller
@RestController//@Controller+@RequestBody
@RequestMapping("users")
public class UserController extends BaseController{
    @Autowired
    private IUserService userService;

    /***
     * 约定大于配置：开发思想来完成，省略掉大量配置甚至注解
     * 1.接收数据方式：请求处理方法的参数列表设置为pojo类型来接收前端的数据
     * SpringBoot会将前端的url地址中的参数名和pojo类的属性名进行比较，如果
     * 这两个名称相同，则将值注入到pojo类中能中对应的属性上
     *
     */

    @RequestMapping("reg")
    //@RequestBody //表示此方法的响应结果以json格式进行数据的响应给前端
    public JsonResult<Void> reg(User user){
            userService.reg(user);
        return new JsonResult<>(OK);
    }
    /*
    @RequestMapping("reg")
    //@RequestBody //表示此方法的响应结果以json格式进行数据的响应给前端
    public JsonResult<Void> reg(User user){
        JsonResult<Void> result = new JsonResult<>();
        try {
            userService.reg(user);
            result.setState(200);
            result.setMessage("用户注册成功");
        } catch (UsernameDuplicatedException e) {
           result.setState(4000);
           result.setMessage("用户名被占用");
        } catch (InsertException e) {
            result.setState(5000);
            result.setMessage("注册时产生未知的异常");
        }
         return result;
    }*/


    /***
     * 2..接收数据方式：请求处理方法的参数列表设置为非自己封装的pojo类型
     * SpringBoot会将请求的参数名和方法的参数名进行直接比较，如果
     * 这两个名称相同则自动完成依赖注入
     *
     */
    @RequestMapping("login")
    public JsonResult<User> login(String username, String password, HttpSession session){

       User data =  userService.login(username,password);
       // 向session对象中完成数据党的绑定（Session全局的）
        session.setAttribute("uid",data.getUid());
        session.setAttribute("username",data.getUsername());

        // 获取session中绑定的数据
        System.out.println(getuidFromSession(session));
        System.out.println(getUsernameFromSession(session));
        return new JsonResult<User>(OK,data);
    }


}
