package com.li.travel.web.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取uri
        String requestURI = req.getRequestURI();
        //获取方法名
        String methodName = requestURI.substring(requestURI.lastIndexOf("/") + 1);
        System.out.println(this);//可以看到Userservlet调用了它
        //获取方法对象
        try {
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            //执行方法
            method.invoke(this, req, resp);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

        //将传入的对象序列化
        public void writeValue(Object object,HttpServletResponse response) throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            response.setContentType("Application/json;charset=utf-8");
            mapper.writeValue(response.getOutputStream(),object);
        }




}
