package com.jc.service;

import com.jc.entity.User;

public interface UserService {

    User findByUsername(String username);

    void register(String username, String password);
}
