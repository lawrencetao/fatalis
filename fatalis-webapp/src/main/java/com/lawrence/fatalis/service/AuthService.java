package com.lawrence.fatalis.service;

import com.lawrence.fatalis.model.FataAuthority;

import java.util.List;

public interface AuthService {

    List<FataAuthority> queryAuthList(String userId);
    List<FataAuthority> queryAllAuth();

}
