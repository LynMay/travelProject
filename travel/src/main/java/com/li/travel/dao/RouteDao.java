package com.li.travel.dao;

import com.li.travel.domain.Route;

import java.util.List;

public interface RouteDao {
    //查询总条数
    public int findCount(int cid,String rname);
    //查询每页的数据
    public List<Route> findByPage(int cid,int start,int pageSize,String rname);


    public Route findOne(int rid);
}
