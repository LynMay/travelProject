package com.li.travel.service;

import com.li.travel.domain.User;

public interface UserService {
    public boolean register(User user);
    public boolean active(String code);

    User login(User user);
}
