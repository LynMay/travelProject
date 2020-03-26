package com.li.travel.web.servlet;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.li.travel.domain.ResultInfo;
import com.li.travel.domain.User;
import com.li.travel.service.UserService;
import com.li.travel.service.impl.UserServiceImpl;
import com.li.travel.util.MailUtils;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
   private UserService userService = new UserServiceImpl();
   //由于继承了BaseServlet,因此该servlet具有service方法，会执行service方法
    //登录功能
   public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       Map<String, String[]> parameterMap = request.getParameterMap();
       ResultInfo resultInfo = new ResultInfo();

       User user = new User();
       try {
           BeanUtils.populate(user,parameterMap);
       } catch (IllegalAccessException e) {
           e.printStackTrace();
       } catch (InvocationTargetException e) {
           e.printStackTrace();
       }
       //判断验证码
       //1.1从session中获取验证码正确值
       String checkcode_right = (String)request.getSession().getAttribute("CHECKCODE_SERVER");
       //1.2获取验证码参数值
       String check = request.getParameter("check");
       request.getSession().removeAttribute("CHECKCODE_SERVER");
       //判断验证码错误
       if(check==null || !check.equalsIgnoreCase(checkcode_right)){
           resultInfo.setFlag(false);
           resultInfo.setErrorMsg("验证码错误");
           ObjectMapper objectMapper   = new ObjectMapper();
           String json = objectMapper.writeValueAsString(resultInfo);
           response.setContentType("Application/json;charset=utf-8");
           response.getWriter().write(json);
           return;
       }

       User user_login =  userService.login(user);
       if(user_login == null){
           resultInfo.setErrorMsg("用户名或密码错误");
           resultInfo.setFlag(false);
       }
       else if(user_login!=null && !"Y".equals(user_login.getStatus())){
           resultInfo.setErrorMsg("未激活账户");
           resultInfo.setFlag(false);
       }
       else if(user_login!=null && "Y".equals(user_login.getStatus())){
           resultInfo.setErrorMsg("登陆成功");
           //把用户信息存入session
           request.getSession().setAttribute("user",user_login);
           resultInfo.setFlag(true);
       }
       //把结果信息序列化输出
       ObjectMapper objectMapper = new ObjectMapper();
       String json = objectMapper.writeValueAsString(resultInfo);
       response.setContentType("Application/json;charset=utf-8");
       response.getWriter().write(json);
   }

   //注册功能
   public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       //1 接收参数值
       Map<String, String[]> parameterMap = request.getParameterMap();
       //2 封装对象：把获取到的参数封装的user对象中
       User user = new User();
       ResultInfo resultInfo = new ResultInfo();
       try {
           BeanUtils.populate(user,parameterMap);
       } catch (IllegalAccessException e) {
           e.printStackTrace();
       } catch (InvocationTargetException e) {
           e.printStackTrace();
       }
       //判断验证码是否正确
       //1.1从session中获取验证码正确值
       String checkcode_right = (String)request.getSession().getAttribute("CHECKCODE_SERVER");
       //1.2获取验证码参数值
       String check = request.getParameter("check");
       request.getSession().removeAttribute("CHECKCODE_SERVER");
       //判断验证码错误
       if(check==null || !check.equalsIgnoreCase(checkcode_right)){
           resultInfo.setFlag(false);
           resultInfo.setErrorMsg("验证码错误");
           writeValue(resultInfo,response);
           return;
       }

       //3 调用Service完成注册

       boolean flag = userService.register(user);

       //4 响应结果

       if(flag==true){
           //注册成功
           resultInfo.setFlag(true);

       }else{
           //注册失败

           resultInfo.setFlag(false);
           resultInfo.setErrorMsg("注册失败，用户名已存在");

       }
       writeValue(resultInfo,response);
       if(flag==true){
           //发送激活邮件
           String context="<a href='http://localhost:80/travel/activeUserServlet?code="+user.getCode()+"'>点击激活</a>";
           MailUtils.sendMail(user.getEmail(),context,"您好，请激活您的邮件");
       }
   }

   //激活功能
   public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       //用户激活邮件成功后跳转到这里
       //判断用户的激活码是否有效
       String code = request.getParameter("code");

       boolean flag = userService.active(code);
       String msg = null;
       if(flag)
           msg = "激活成功，前往<a href='login.html'>登录</a>";
       else
           msg = "激活失败";
       response.setContentType("text/html;charset=utf-8");
       response.getWriter().write(msg);
   }

   //退出功能
   protected void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       //1.销毁session
       request.getSession().invalidate();

       //2.跳转登录页面
       response.sendRedirect(request.getContextPath()+"/login.html");

   }

   //查询单个对象
   public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       Object user = request.getSession().getAttribute("user");
       //将userName序列化写回到客户端
      writeValue(user,response);
   }

}
