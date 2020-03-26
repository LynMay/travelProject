package com.li.travel.dao.impl;

import com.li.travel.dao.UserDao;
import com.li.travel.domain.User;
import com.li.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements UserDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    //注册功能
    public User SelectByName(User user) {
        //dao操作 JDBCTemplate
        User regUser = null;
        String sql = "select * from tab_user where username=? ";
        try{
            regUser = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), user.getUsername());
        }
        catch (Exception e){

        }

        return regUser;

    }

    @Override
    //保存功能
    public void save(User user) {
        String sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code) values(?,?,?,?,?,?,?,?,?)";
        int update = jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getName(), user.getBirthday(), user.getSex()
                , user.getTelephone(), user.getEmail()
                ,user.getStatus(),user.getCode());

    }

    @Override
    public User selectByCode(String code) {
        User user = null;
        try {
            String sql = "select * from tab_user where code=?";
            user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), code);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public void updateCode(User user) {
            String sql = "update tab_user set status='Y' where code=?";
            jdbcTemplate.update(sql,user.getCode());

    }

    @Override
    public User fingByUsernameAndPassword(User user) {
        User regUser = null;
        String sql = "select * from tab_user where username=? and password=? ";
        try{
            regUser = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), user.getUsername(),user.getPassword());
        }
        catch (Exception e){

        }

        return regUser;
    }



}
