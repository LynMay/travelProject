package com.li.travel.dao.impl;

import com.li.travel.dao.FavoriteDao;
import com.li.travel.domain.Favorite;
import com.li.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.zip.DataFormatException;

public class FavoriteDaoImpl implements FavoriteDao {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public int findCountByRid(int rid) {
        String sql = "select count(*) from  tab_favorite where rid = ?";
        return jdbcTemplate.queryForObject(sql,Integer.class,rid);

    }

    @Override
    public Favorite isFlag(int uid, int rid) {
        Favorite favorite = null;
        try{
            String sql = "select * from tab_favorite where uid=? and rid=?";
             favorite = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), uid, rid);
            System.out.println("favorite"+favorite);
        }catch (Exception e){
            e.printStackTrace();
        }
        return favorite;
    }

    @Override
    public void addFavorite(int rid,int uid) {

        String sql = "insert into tab_favorite values(?,?,?)";

        jdbcTemplate.update(sql,rid,new Date(),uid);

    }
}
