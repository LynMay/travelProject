package com.li.travel.service.impl;

import com.li.travel.dao.CategoryDao;
import com.li.travel.dao.impl.CategoryDaoImpl;
import com.li.travel.domain.Category;
import com.li.travel.service.CategoryService;
import com.li.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    CategoryDao categoryDao = new CategoryDaoImpl();
    Jedis jedis = JedisUtil.getJedis();

    @Override
    public List<Category> findAll() {
        //先从redis查找
        Set<Tuple> categories = jedis.zrangeWithScores("category", 0, -1);
        List<Category> lists = null;
       if(categories == null || categories.size()==0){
           System.out.println("从数据库查询");
           lists = categoryDao.findAll();
           //把数据存入redis
           for (Category list : lists) {
               jedis.zadd("category",(int)list.getCid(),list.getCname());
           }
       }else{
           System.out.println("缓存中查询数据");
           //将set类型的categories转为List类型。
           lists = new ArrayList<Category>();
           for (Tuple category : categories) {
               Category c = new Category();
               c.setCname(category.getElement());
               c.setCid((int)category.getScore());
               lists.add(c);
           }
       }

        return lists;

    }
}
