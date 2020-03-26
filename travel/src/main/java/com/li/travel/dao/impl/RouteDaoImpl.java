package com.li.travel.dao.impl;

import com.li.travel.dao.RouteDao;
import com.li.travel.domain.Route;
import com.li.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public int findCount(int cid,String rname) {
        System.out.println(rname);
        String sql = "select count(*) from tab_route where 1=1 ";
        StringBuilder builder = new StringBuilder(sql);
        //存放？的列表
        List list = new ArrayList();
        //cid 和 rname参数不一定都有
        if(cid!=0){
            builder.append(" and cid=? ");
            list.add(cid);
        }
        if(rname!=null && rname.length()>0 && !"null".equals(rname)){
            builder.append(" and rname like ?");
            list.add("%"+rname+"%");
        }
        sql = builder.toString();
        System.out.println(sql);
        int count =  jdbcTemplate.queryForObject(sql,Integer.class,list.toArray());
        System.out.println(cid+":"+count);
        return count;
    }


    public List<Route> findByPage(int cid, int start, int pageSize,String rname) {
        String sql = "select * from tab_route where 1=1 ";
        StringBuilder builder = new StringBuilder(sql);
        List list = new ArrayList();
        if(cid!=0){
            builder.append(" and cid=? ");
            list.add(cid);
        }
        if(rname!=null && rname.length()>0 && !"null".equals(rname)){
            builder.append(" and rname like ? ");
            list.add("%"+rname+"%");
        }
        builder.append(" limit ?,?");
        list.add(start);
        list.add(pageSize);
        sql = builder.toString();
        List<Route> query = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Route>(Route.class),list.toArray());
        System.out.println(query);
        return query;
    }

    //根据rid查找详情
    @Override
    public Route findOne(int rid) {
        String sql = "select * from tab_route where rid = ?";

        return jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<Route>(Route.class),rid);
    }


}
