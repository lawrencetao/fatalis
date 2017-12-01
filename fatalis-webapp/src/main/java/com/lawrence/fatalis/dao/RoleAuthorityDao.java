package com.lawrence.fatalis.dao;

import com.lawrence.fatalis.model.RoleAuthority;

public interface RoleAuthorityDao {

    int deleteByPrimaryKey(RoleAuthority key);
    int insert(RoleAuthority record);
    int insertSelective(RoleAuthority record);
    RoleAuthority selectByPrimaryKey(RoleAuthority key);
    int updateByPrimaryKeySelective(RoleAuthority record);
    int updateByPrimaryKey(RoleAuthority record);

}