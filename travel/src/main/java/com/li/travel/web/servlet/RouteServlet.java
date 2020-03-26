package com.li.travel.web.servlet;

import com.li.travel.domain.PageBean;
import com.li.travel.domain.Route;
import com.li.travel.domain.User;
import com.li.travel.service.RouteService;
import com.li.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService routeService = new RouteServiceImpl();

    //查询所有route
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收客户端传来的三个参数
        String currentPagestr = request.getParameter("currentPage");
        String pageSizestr = request.getParameter("pageSize");
        String cidstr = request.getParameter("cid");
        String rname = request.getParameter("rname");
        if (rname != null)
            rname = new String(rname.getBytes("iso-8859-1"), "utf-8");
        //处理参数
        int cid = 0;
        if (cidstr != null && cidstr.length() > 0 && !"null".equals(cidstr)) {
            cid = Integer.parseInt(cidstr);
            //System.out.println("得到参数数："+cid);
        }
        int currentPage = 0;
        if (currentPagestr != null && currentPagestr.length() > 0) {
            currentPage = Integer.parseInt(currentPagestr);
        } else {
            currentPage = 1;//默认从第一页开始
        }

        int pageSize = 0;
        if (pageSizestr != null && pageSizestr.length() > 0) {
            pageSize = Integer.parseInt(pageSizestr);
        } else {
            pageSize = 5;//默认每页显示5条数据
        }

        //调用service

        PageBean<Route> pb = routeService.pageQuery(cid, currentPage, pageSize, rname);
        writeValue(pb, response);


    }

    //根据rid显示route详情
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ridStr = request.getParameter("rid");
        int rid = 0;
        rid = Integer.parseInt(ridStr);
        Route route = routeService.findOne(rid);
        writeValue(route, response);

    }

    //判断用户是否收藏过

    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取user的id,需要判断用户有无登录
        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if (user == null) {
            //没有登录
            uid = 0;
        } else {
            //登录
            uid = user.getUid();
        }

        //获取线路的rid，已经将rid传递
        String rid = request.getParameter("rid");
        //调用service的方法，判断是否已经收藏
        boolean flag = routeService.isFlag(uid, Integer.parseInt(rid));
        writeValue(flag, response);
    }

    //将用户收藏添加进数据库
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if (user == null) {
            //没有登录
           return;
        } else {
            //登录
            uid = user.getUid();
        }
        routeService.addFavorite(Integer.parseInt(rid),uid);

    }
}