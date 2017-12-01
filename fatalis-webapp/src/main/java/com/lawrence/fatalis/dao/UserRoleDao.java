package com.lawrence.fatalis.dao;

import com.lawrence.fatalis.model.UserRole;

public interface UserRoleDao {

    int deleteByPrimaryKey(UserRole key);
    int insert(UserRole record);
    int insertSelective(UserRole record);
    UserRole selectByPrimaryKey(UserRole key);
    int updateByPrimaryKeySelective(UserRole record);
    int updateByPrimaryKey(UserRole record);

}