package com.li.travel.web.servlet;

import com.li.travel.domain.Category;
import com.li.travel.service.CategoryService;
import com.li.travel.service.impl.CategoryServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/category/*")
public class CategoryServlet extends BaseServlet  {
    CategoryService categoryService = new CategoryServiceImpl();
    public void findAll(HttpServletRequest request,HttpServletResponse response) throws IOException {
        List<Category> lists = categoryService.findAll();
        //序列化位为json
        writeValue(lists,response);
    }
}
