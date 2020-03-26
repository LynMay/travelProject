package com.li.travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.li.travel.domain.ResultInfo;
import com.li.travel.domain.User;
import com.li.travel.service.UserService;
import com.li.travel.service.impl.UserServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet( "/loginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        ResultInfo resultInfo = new ResultInfo();
        UserService userService = new UserServiceImpl();
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
            request.getSession().setAttribute("user",user_login);
            resultInfo.setFlag(true);
        }
        //把结果信息序列化输出
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(resultInfo);
        response.setContentType("Application/json;charset=utf-8");
        response.getWriter().write(json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}
