package com.li.travel.dao;

import com.li.travel.domain.User;

public interface UserDao {
    public  User SelectByName(User user);
    public void save(User user);

    User selectByCode(String code);

    void updateCode(User user);

    User fingByUsernameAndPassword(User user);
}
