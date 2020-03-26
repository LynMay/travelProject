package com.li.travel.web.servlet;

import com.li.travel.service.UserService;
import com.li.travel.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet( "/activeUserServlet")
public class ActiveUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //用户激活邮件成功后跳转到这里
        //判断用户的激活码是否有效
        String code = request.getParameter("code");
        UserService userService = new UserServiceImpl();
        boolean flag = userService.active(code);
        String msg = null;
        if(flag)
            msg = "激活成功，前往<a href='login.html'>登录</a>";
        else
            msg = "激活失败";
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(msg);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}
