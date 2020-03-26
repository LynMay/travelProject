package com.li.travel.dao;

import com.li.travel.domain.RouteImg;

import java.util.List;

public interface RouteImgDao {
    //根据rid查询route的图片
    public List<RouteImg> findOne(int rid);
}
