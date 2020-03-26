package com.li.travel.service;

import com.li.travel.domain.PageBean;
import com.li.travel.domain.Route;

import java.util.List;

public interface RouteService {
    public PageBean<Route> pageQuery(int cid, int currentSize, int pageSize,String rname);

    public Route findOne(int rid);

    public boolean isFlag(int uid, int rid);

    public void addFavorite(int rid,int uid);
}
