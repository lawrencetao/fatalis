package com.lawrence.fatalis.dao;

import com.lawrence.fatalis.model.FataUser;

import java.util.List;

public interface FataUserDao {

    int deleteByPrimaryKey(String userId);
    int insert(FataUser record);
    int insertSelective(FataUser record);
    FataUser selectByPrimaryKey(String userId);
    int updateByPrimaryKeySelective(FataUser record);
    int updateByPrimaryKey(FataUser record);
    List<FataUser> selectUserByMobile(String mobile);

}