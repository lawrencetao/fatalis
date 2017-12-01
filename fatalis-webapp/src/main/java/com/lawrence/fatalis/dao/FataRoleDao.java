package com.lawrence.fatalis.dao;

import com.lawrence.fatalis.model.FataRole;

import java.util.List;

public interface FataRoleDao {

    int deleteByPrimaryKey(String roleId);
    int insert(FataRole record);
    int insertSelective(FataRole record);
    FataRole selectByPrimaryKey(String roleId);
    int updateByPrimaryKeySelective(FataRole record);
    int updateByPrimaryKey(FataRole record);

    List<FataRole> queryRoleByUserId(String userId);

}