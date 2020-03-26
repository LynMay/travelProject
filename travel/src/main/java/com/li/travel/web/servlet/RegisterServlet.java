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

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            ObjectMapper objectMapper   = new ObjectMapper();
            String json = objectMapper.writeValueAsString(resultInfo);
            response.setContentType("Application/json;charset=utf-8");
            response.getWriter().write(json);
            return;
        }

        //3 调用Service完成注册
        UserService service = new UserServiceImpl();
        boolean flag = service.register(user);

        //4 响应结果

        if(flag==true){
            //注册成功
            resultInfo.setFlag(true);

        }else{
            //注册失败

            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("注册失败，用户名已存在");

        }
        //序列化，将info对象序列化
        ObjectMapper objectMapper   = new ObjectMapper();
        String json = objectMapper.writeValueAsString(resultInfo);
        response.setContentType("Application/json;charset=utf-8");
        response.getOutputStream().write(json.getBytes());
        if(flag==true){
            //发送激活邮件
            String context="<a href='http://localhost:80/travel/activeUserServlet?code="+user.getCode()+"'>点击激活</a>";
            MailUtils.sendMail(user.getEmail(),context,"您好，请激活您的邮件");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}
