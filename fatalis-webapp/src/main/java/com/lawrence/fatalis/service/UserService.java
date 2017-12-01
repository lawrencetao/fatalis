package com.lawrence.fatalis.service;

import com.lawrence.fatalis.model.FataUser;

public interface UserService {

    int updateUser(FataUser user);
    FataUser getUserByMobile(String mobile);

}
