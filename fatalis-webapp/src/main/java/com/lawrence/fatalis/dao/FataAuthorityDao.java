package com.lawrence.fatalis.dao;

import com.lawrence.fatalis.model.FataAuthority;

import java.util.List;

public interface FataAuthorityDao {

    int deleteByPrimaryKey(String authorityId);
    int insert(FataAuthority record);
    int insertSelective(FataAuthority record);
    FataAuthority selectByPrimaryKey(String authorityId);
    int updateByPrimaryKeySelective(FataAuthority record);
    int updateByPrimaryKeyWithBLOBs(FataAuthority record);
    int updateByPrimaryKey(FataAuthority record);

    List<FataAuthority> queryAuthByUserId(String userId);
    List<FataAuthority> queryAllAuth();

}