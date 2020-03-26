package com.li.travel.dao;

import com.li.travel.domain.Seller;

public interface RouteSellerDao {
    //根据route的sid查询seller
    public Seller findOne(int sid);
}
