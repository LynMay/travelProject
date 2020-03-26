package com.li.travel.service.impl;

import com.li.travel.dao.UserDao;
import com.li.travel.dao.impl.UserDaoImpl;
import com.li.travel.domain.User;
import com.li.travel.service.UserService;
import com.li.travel.util.MailUtils;
import com.li.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();
    @Override
    public boolean register(User user) {

        User regUser = userDao.SelectByName(user);
        if(regUser==null){
            //注册
            //设置激活状态及激活码
            user.setStatus("N");
            user.setCode(UuidUtil.getUuid());
            userDao.save(user);
            //发送激活邮件
            String context="<a href='http://localhost:80/travel/user/active?code="+user.getCode()+"'>点击激活</a>";
            MailUtils.sendMail(user.getEmail(),context,"您好，请激活您的邮件");
            return true;
        }else{
            //已经存在
            return false;
        }

    }

    @Override
    public boolean active(String code) {


        UserDao userDao = new UserDaoImpl();
        User user = userDao.selectByCode(code);
        if(user!=null){
            userDao.updateCode(user);
            return true;
        }


        return false;
    }

    @Override
    public User login(User user) {

        return userDao.fingByUsernameAndPassword(user);
    }
}
