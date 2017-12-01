package com.lawrence.fatalis.service;

import com.lawrence.fatalis.model.FataRole;

import java.util.List;

public interface RoleService {

    List<FataRole> queryRoleList(String userId);

}
