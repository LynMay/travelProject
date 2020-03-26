package com.li.travel.service.impl;

import com.li.travel.dao.FavoriteDao;
import com.li.travel.dao.RouteDao;
import com.li.travel.dao.RouteImgDao;
import com.li.travel.dao.RouteSellerDao;
import com.li.travel.dao.impl.FavoriteDaoImpl;
import com.li.travel.dao.impl.RouteDaoImpl;
import com.li.travel.dao.impl.RouteImgDaoImpl;
import com.li.travel.dao.impl.RouteSellerDaoImpl;
import com.li.travel.domain.*;
import com.li.travel.service.RouteService;
import sun.jvm.hotspot.debugger.Page;

import java.util.List;

public class RouteServiceImpl implements RouteService {
   private RouteDao routeDao = new RouteDaoImpl();
   private RouteImgDao routeImgDao = new RouteImgDaoImpl();
   private RouteSellerDao routeSellerDao = new RouteSellerDaoImpl();
   private FavoriteDao favoriteDao = new FavoriteDaoImpl();
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize,String rname){
        PageBean pageBean = new PageBean();
        int start = (currentPage-1) * pageSize;
        int count = routeDao.findCount(cid,rname);
        System.out.println("service得到cid、count是："+count);
        List<Route> routes = routeDao.findByPage(cid, start, pageSize,rname);
        //封装PageBean
        pageBean.setTotalCount(count);
        pageBean.setCurrentPage(currentPage);
        pageBean.setPageSize(pageSize);
        pageBean.setList(routes);
        int totalPage = count%pageSize==0?count/pageSize:count/pageSize+1;
        pageBean.setTotalPage(totalPage);
        return pageBean;

    }

    @Override
    //根据rid查找详情
    public Route findOne(int rid) {

        Route route = this.routeDao.findOne(rid);
        List<RouteImg> routeImgs = routeImgDao.findOne(rid);
        route.setRouteImgList(routeImgs);
        Seller seller = routeSellerDao.findOne(route.getSid());
        route.setSeller(seller);
        //设置收藏次数
        int count = 0;
         count =favoriteDao.findCountByRid(rid);
        route.setCount(count);
        return route;
    }

    @Override
    public boolean isFlag(int uid, int rid) {
        Favorite favorite = favoriteDao.isFlag(uid, rid);
        System.out.println(favorite!=null);
            return favorite!=null;
    }

    @Override
    public void addFavorite(int rid,int uid) {
        favoriteDao.addFavorite(rid,uid);
    }
}
